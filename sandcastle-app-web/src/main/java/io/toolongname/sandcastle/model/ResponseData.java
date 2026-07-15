package io.toolongname.sandcastle.model;

import java.util.HashMap;

public final class ResponseData extends HashMap<String, Object> {
    public ResponseData(Status status, int code, String message, Object data) {
        put("status", status);
        put("code", code);
        put("message", message);
        if (null != data) {
            put("data", data);
        }
    }

    public static ResponseData SUCCEEDED() {
        return new ResponseData(Status.SUCCEEDED, 0, "", null);
    }

    public static ResponseData SUCCEEDED(String message) {
        return new ResponseData(Status.SUCCEEDED, 0, message, null);
    }

    public static ResponseData SUCCEEDED_WITH_DATA(Object data) {
        return new ResponseData(Status.SUCCEEDED, 0, "", data);
    }

    public static ResponseData SUCCEEDED(String message, Object data) {
        return new ResponseData(Status.SUCCEEDED, 0, message, data);
    }

    public static ResponseData FAILED(String message, int code, Object data) {
        return new ResponseData(Status.FAILED, code, message, data);
    }

    public static ResponseData FAILED(String message, int code) {
        return new ResponseData(Status.FAILED, code, message, null);
    }

    public static ResponseData FAILED(String message, Object data) {
        return new ResponseData(Status.FAILED, -1, message, data);
    }

    public static ResponseData FAILED(String message) {
        return new ResponseData(Status.FAILED, -1, message, null);
    }

    public enum Status {
        SUCCEEDED,
        FAILED
    }
}
