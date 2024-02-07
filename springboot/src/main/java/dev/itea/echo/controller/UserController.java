package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckOr;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.SaTokenInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.annotation.SaUserCheckLogin;
import dev.itea.echo.entity.User;
import dev.itea.echo.entity.StpUserUtil;
import dev.itea.echo.entity.UserProfile;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.service.UserService;
import dev.itea.echo.validation.AddValidationGroup;
import dev.itea.echo.validation.UpdateValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 用户前端控制器
 *
 * @author isixe
 * @since 2024-01-15
 */
@Tag(name = "User", description = "用户接口")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 用户登录
     *
     * @param name       用户名或邮箱
     * @param password   密码
     * @param rememberMe 记住密码
     * @return token 凭证信息
     */
    @Operation(summary = "用户登录", description = "前台用户登录", tags = "User", method = "POST",
            parameters = {
                    @Parameter(name = "name", description = "用户名", required = true, example = "user"),
                    @Parameter(name = "password", description = "密码", required = true, example = "123456"),
                    @Parameter(name = "remeberMe", description = "记住密码", required = true, example = "true"),
            })
    @SaIgnore
    @PostMapping("/login")
    public SaTokenInfo login(String name, String password, @RequestParam(defaultValue = "false") boolean rememberMe) {
        //get data
        User loginUser = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getName, name)
                .or()
                .eq(User::getEmail, name));
        //check user
        if (ObjectUtils.isEmpty(loginUser)) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        //check password
        boolean flag = BCrypt.checkpw(password, loginUser.getPassword());
        if (!flag) {
            throw new BusinessException(ResultCode.USER_LOGIN_ERROR);
        }
        //update status
        loginUser.setLastActiveTime(LocalDateTime.now());
        userService.updateById(loginUser);
        //save session
        int id = loginUser.getId();
        StpUserUtil.login(id, rememberMe);
        return StpUserUtil.getTokenInfo();
    }

    /**
     * 用户退出
     *
     * @param token 用户token
     */
    @Operation(summary = "用户退出", description = "前台用户退出", tags = "User", method = "POST",
            parameters = {
                    @Parameter(name = "token",
                            description = "用户token",
                            required = true,
                            example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOjEsInJuU3RyIjoiZ041S3pqTWRVWDBrQW80dXh1aDl4M2ZES0wwVHFidDEifQ.1qQAxChyCEy-kDNVKYALbEWsCfiO4ns2h0t01qZUFCk"),
            })
    @SaUserCheckLogin
    @PostMapping("/logout")
    public void logout(@CookieValue(value = "satokenuser") String token) {
        //get user login id
        String result = (String) StpUserUtil.getLoginIdByToken(token);
        if (ObjectUtils.isEmpty(result)) {
            throw new BusinessException(ResultCode.USER_NOT_LOGGED_IN);
        }
        Integer id = Integer.parseInt(result);
        //logout
        boolean flag = StpUserUtil.isLogin(id);
        if (!flag) {
            throw new BusinessException(ResultCode.USER_NOT_LOGGED_IN);
        }
        StpUserUtil.logout(id);
    }

    /**
     * 用户注册
     *
     * @param name     用户名
     * @param password 密码
     * @param email    邮箱
     */
    @Operation(summary = "用户注册", description = "前台用户注册", tags = "User", method = "POST",
            parameters = {
                    @Parameter(name = "name", description = "用户名", required = true, example = "user"),
                    @Parameter(name = "password", description = "密码", required = true, example = "123456"),
                    @Parameter(name = "email", description = "邮箱", required = true, example = "email@example.com"),
            })
    @SaIgnore
    @PostMapping("/register")
    public void register(String name, String password, String email) {
        User checkUser = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getName, name).or().eq(User::getEmail, email));
        //check user
        if (!ObjectUtils.isEmpty(checkUser)) {
            throw new BusinessException(ResultCode.USER_HAS_EXISTED);
        }
        //encrypt
        String pwHash = BCrypt.hashpw(password, BCrypt.gensalt(12));
        //insert
        User user = new User();
        user.setName(name);
        user.setPassword(pwHash);
        user.setEmail(email);
        userService.save(user);
    }

    /**
     * 用户新增
     *
     * @param user 用户实体
     */
    @Operation(summary = "用户新增", description = "后台用户新增", tags = "User", method = "POST",
            parameters = {
                    @Parameter(name = "user", description = "用户实体", required = true),
            })
    @SaCheckLogin
    @PostMapping
    public void add(@Validated(AddValidationGroup.class) User user) {
        User checkUser = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getName, user.getName()));
        //check user
        if (!ObjectUtils.isEmpty(checkUser)) {
            throw new BusinessException(ResultCode.USER_HAS_EXISTED);
        }
        //encrypt
        String pwHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
        user.setPassword(pwHash);
        //insert
        userService.save(user);
    }

    /**
     * 用户更新
     *
     * @param user 用户实体
     */
    @Operation(summary = "用户更新", description = "后台用户更新", tags = "User", method = "PUT",
            parameters = {
                    @Parameter(name = "user", description = "用户实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PutMapping
    public void update(@Validated(UpdateValidationGroup.class) User user) {
        //check user
        User checkUser = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, user.getId()));
        if (ObjectUtils.isEmpty(checkUser)) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        //encrypt
        if (!checkUser.getPassword().equals(user.getPassword())) {
            String pwHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
            user.setPassword(pwHash);
        }
        //update
        userService.updateById(user);
    }

    /**
     * 用户删除
     *
     * @param id 用户ID
     */
    @Operation(summary = "用户删除", description = "后台用户删除", tags = "User", method = "DELETE",
            parameters = {
                    @Parameter(name = "id", description = "用户ID", required = true, example = "2"),
            })
    @SaCheckLogin
    @DeleteMapping
    public void delete(Integer id) {
        //check user
        User checkUser = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, id));
        if (ObjectUtils.isEmpty(checkUser)) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        //delete
        userService.removeById(id);
    }

    /**
     * 用户查询（ID）
     *
     * @param id 用户ID
     */
    @Operation(summary = "用户查询", description = "后台用户查询", tags = "User", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "用户ID", required = true, example = "2"),
            })
    @SaCheckLogin
    @GetMapping
    public User getById(Integer id) {
        //get user
        User user = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, id));
        //check user
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        //renew timeout token
        if (StpUserUtil.getTokenTimeout() < 86400) {
            StpUserUtil.renewTimeout(2592000);
        }
        return user;
    }

    /**
     * 用户查询（分页&关键词）
     *
     * @param pageNum  页数
     * @param pageSize 每个页的数据量
     * @param keyword  模糊搜索关键词
     * @return IPage 分页对象
     */
    @Operation(summary = "用户分页与关键词查询", description = "后台用户分页与关键词查询", tags = "User", method = "GET",
            parameters = {
                    @Parameter(name = "pageNum", description = "页数", required = true, example = "1"),
                    @Parameter(name = "pageSize", description = "每个页的数据量", required = true, example = "10"),
                    @Parameter(name = "keyword", description = "模糊搜索关键词", required = true, example = "user"),
            })
    @SaCheckLogin
    @GetMapping("/queryAll")
    public IPage<User> getByName(@RequestParam(defaultValue = "1") Integer pageNum,
                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                 @RequestParam(required = false) String keyword) {
        if (pageNum < 0 || pageSize < 0) {
            throw new BusinessException(ResultCode.PARAMETER_IS_INVALID);
        }
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return userService.getUserByPage(pageable, keyword);
    }


    /**
     * 用户信息查询
     *
     * @param id 用户ID
     */
    @Operation(summary = "用户信息查询", description = "前台用户信息查询", tags = "User", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "用户ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping("/profile")
    public UserProfile getByUserInfoId(Integer id) {
        UserProfile userProfile = userService.selectUserInfoById(id);
        if (ObjectUtils.isEmpty(userProfile)) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        return userProfile;
    }

}
