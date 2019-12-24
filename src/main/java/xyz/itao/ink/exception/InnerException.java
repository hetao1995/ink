package xyz.itao.ink.exception;

/**
 * 内部的错误，需要记录日志，不能暴露给用户
 *
 * @author hetao
 * @date 2018-12-03
 */
public class InnerException extends RuntimeException {
    private Integer code;
    private Object data;

    public Integer getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }

    public InnerException(ExceptionEnum exceptionEnum, Object data, Throwable cause) {
        super(exceptionEnum.getMessage(), cause);
        this.code = exceptionEnum.getCode();
        this.data = data;
    }

    public InnerException(ExceptionEnum exceptionEnum, Object data) {
        this(exceptionEnum, data, null);
    }

    public InnerException(ExceptionEnum exceptionEnum) {
        this(exceptionEnum, null);
    }

    public InnerException(ExceptionEnum exceptionEnum, Throwable cause) {
        this(exceptionEnum, null, cause);
    }
}
