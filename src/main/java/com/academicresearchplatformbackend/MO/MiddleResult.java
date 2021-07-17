package com.academicresearchplatformbackend.MO;

import lombok.Data;

@Data
public class MiddleResult<T> {
    private String message;//related message
    int code;//code;
    private T data;
    boolean isSuccess;

    public MiddleResult(String message, int code, T data, boolean isSuccess) {
        this.message = message;
        this.code = code;
        this.data = data;
        this.isSuccess = isSuccess;
    }

    public MiddleResult(String message, T data) {
        this(message, 0, data, true);
    }

    public MiddleResult(String message) {
        this(message, 0, null, true);
    }

    public MiddleResult(T data) {
        this("", 0, data, true);
    }

    //public MiddleResult(MiddleResult<T> )

    public MiddleResult<T> isFailed() {
        this.isSuccess = false;
        return this;
    }
}
