package xyz.itao.ink.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hetao
 * @date 2018-12-01 22:18
 * @description
 */
@Slf4j
public class TipException extends RuntimeException {


    private Integer code;
    public TipException() {
    }

    public TipException(String message) {
        super(message);
    }

    public TipException(String message, Throwable cause) {
        super(message, cause);
    }

    public TipException(Throwable cause) {
        super(cause);
    }

    public TipException(ExceptionEnum exceptionEnum){
        super(exceptionEnum.getMessage());
        this.code = exceptionEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }
}