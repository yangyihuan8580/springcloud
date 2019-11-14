package com.yyh.util;

import com.yyh.common.base.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 *  validator 校验异常拦截
 */
@RestControllerAdvice
public class BadRequestExceptionHandler {


    private static final Logger logger = LoggerFactory.getLogger(BadRequestExceptionHandler.class);

    /**
    * 校验错误拦截处理
    *
    * @param exception 错误信息集合
    * @return 错误信息
    */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result validationBodyException(MethodArgumentNotValidException exception){

        BindingResult result = exception.getBindingResult();
        String errorMessage = "请填写正确信息";
        if (result.hasErrors()) {

            List<ObjectError> errors = result.getAllErrors();
            for (ObjectError error : errors) {
                FieldError fieldError = (FieldError) error;
                logger.error("Data check failure : object[{}],field[{}],errorMessage[{}]", fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage());
                errorMessage = fieldError.getDefaultMessage();
                break;
            }
        }
        return Result.error(Result.ERROR_CODE, errorMessage);
    }

    /**
      * 参数类型转换错误
      *
      * @param exception 错误
      * @return 错误信息
      */
    @ExceptionHandler(HttpMessageConversionException.class)
    public Result parameterTypeException(HttpMessageConversionException exception){
        logger.error(exception.getCause().getLocalizedMessage());
        return Result.error(Result.ERROR_CODE, "类型转换错误");
    }

}