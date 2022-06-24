package com.example.praca.service;

import lombok.Data;

import java.util.Collections;
import java.util.Map;

/**
 * @author Szymon Królik
 */
@Data
public class ReturnService<T> {
    private String message;
    private int status;
    private Map<String, String> errList;
    private T value;

    public static <T> ReturnService returnError(String errMsg, T obj, int status) {
        ReturnService ret = new ReturnService();
        ret.setErrList(Collections.singletonMap("user", errMsg));
        ret.setValue(obj);
        ret.setStatus(0);

        return ret;
    }

    public static <T> ReturnService returnError(String errMsg, Map<String, String> errList, T obj, int status) {
        ReturnService ret = new ReturnService();
        ret.setErrList(errList);
        ret.setValue(obj);
        ret.setValue(status);

        return ret;
    }

    public static ReturnService returnError(String errMsg, int status) {
        ReturnService ret = new ReturnService();
        ret.setMessage(errMsg);
        ret.setStatus(status);

        return ret;
    }

    public static <T> ReturnService returnInformation(String msg, T obj, int status) {
        ReturnService ret = new ReturnService();

        ret.setMessage(msg);
        ret.setStatus(status);
        ret.setValue(obj);

        return ret;
    }
}
