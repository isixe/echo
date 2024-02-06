package dev.itea.echo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.io.Serial;

import dev.itea.echo.validation.AddValidationGroup;
import dev.itea.echo.validation.UpdateValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

/**
 * 用户表
 *
 * @author isixe
 * @since 2024-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("user")
@Schema(description = "用户表")
public class User extends Model<User> {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "不能为空", groups = UpdateValidationGroup.class)
    private Integer id;

    @Schema(description = "用户名称")
    @TableField("name")
    @NotNull(message = "不能为空", groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    @NotBlank(message = "不能为空字符串", groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    @Length(message = "长度不能小于4个字符和大于16个字符", min = 4, max = 16, groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    private String name;

    @Schema(description = "用户密码")
    @TableField("password")
    @NotNull(message = "不能为空", groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    @NotBlank(message = "不能为空字符串", groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    @Length(message = "长度不能小于6个字符", min = 6, groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    private String password;

    @Schema(description = "邮箱")
    @TableField("email")
    @NotNull(message = "不能为空", groups = {UpdateValidationGroup.class})
    @Email(message = "邮箱格式错误", groups = {UpdateValidationGroup.class})
    @Pattern(regexp = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "非法邮箱格式", groups = {UpdateValidationGroup.class})
    private String email;

    @Schema(description = "个性签名")
    @TableField("description")
    private String description;

    @Schema(description = "头像")
    @TableField("avatar")
    private String avatar;

    @Schema(description = "注册时间")
    @TableField(value = "created_time", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NOT_NULL)
    private LocalDateTime createdTime;

    @Schema(description = "最后活跃时间")
    @TableField(value = "last_active_time", fill = FieldFill.INSERT_UPDATE, updateStrategy = FieldStrategy.NOT_NULL)
    private LocalDateTime lastActiveTime;

    @Schema(description = "是否删除")
    @TableField
    private Byte isDeleted;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}