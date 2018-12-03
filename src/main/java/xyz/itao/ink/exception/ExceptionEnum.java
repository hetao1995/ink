package xyz.itao.ink.exception;

public enum ExceptionEnum {
    PERSISTENCE_FAIL("持久化失败",-1);
    private String message;
    private Integer code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    ExceptionEnum(String message, Integer code){
        this.message = message;
        this.code = code;
    }
}
