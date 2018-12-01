package com.css.utils;

import com.google.gson.Gson;

public class JsonUtils {

    /**
     * 对象转字符串
     * */
    public static String toJson(Object object){
        String json = new Gson().toJson(object);
        return json;
    }

    /**
     * 字符串转对象
     * */
    public static <T> T parse(String json, Class<T> clazz){
        T t = (T)new Gson().fromJson(json, clazz);
        return t;
    }
}
