package dev.itea.echo.advice;

import cn.dev33.satoken.exception.NotLoginException;
import dev.itea.echo.entity.result.ErrorResult;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author: isixe
 * @create: 2023-06-19 14:35
 * @description: 全局Controller异常处理类
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult doViolationException(BindException ex) {
        StringBuilder errorMessage = new StringBuilder();
        List<FieldError> allErrors = ex.getFieldErrors();
        StringJoiner joiner = new StringJoiner(", ");
        allErrors.forEach(fieldError -> joiner.add(fieldError.getField() + fieldError.getDefaultMessage()));
        errorMessage.append(joiner);
        log.error("请求参数异常信息 ex={}", errorMessage);
        return ErrorResult.fail(ResultCode.PARAMETER_IS_INVALID, errorMessage.toString());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult doMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();
        List<FieldError> allErrors = ex.getFieldErrors();
        StringJoiner joiner = new StringJoiner(", ");
        allErrors.forEach(fieldError -> joiner.add(fieldError.getField() + fieldError.getDefaultMessage()));
        errorMessage.append(joiner);
        log.error("数据校验异常信息 ex={}", errorMessage);
        return ErrorResult.fail(ResultCode.PARAMETER_IS_INVALID, errorMessage.toString());
    }

    @ExceptionHandler(NotLoginException.class)
    public ErrorResult doLoginException(NotLoginException ex) {
        log.error("登录异常信息 ex={}", ex.getMessage(), ex);
        return ErrorResult.fail(ResultCode.TOKEN_IS_INVALID);
    }

    @ExceptionHandler(BusinessException.class)
    public ErrorResult doBusinessException(BusinessException ex) {
        log.error("业务异常信息 ex={}", ex.getMessage(), ex);
        return ErrorResult.fail(ex.getResultCode());
    }

    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResult doSystemException(SystemException ex) {
        log.error("系统异常信息 ex={}", ex.getMessage(), ex);
        return ErrorResult.fail(ex.getResultCode());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResult doOtherException(Exception ex) {
        log.error("全局异常信息 ex={}", ex.getMessage(), ex);
        return ErrorResult.fail(ResultCode.SYSTEM_ERROR);
    }
}
