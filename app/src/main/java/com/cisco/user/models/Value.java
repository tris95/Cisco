package com.cisco.user.models;

import java.util.List;

public class Value<T> {
    private int success;
    private String message;
    private List<T> data;

    public Value(int success, String message, List<T> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public int getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<T> getData() {
        return data;
    }
}
