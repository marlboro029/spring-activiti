package com.atm.spring.activiti.utils;

import java.util.UUID;

/**
 * Created by admin on 27/11/2016.
 */
public class GenUtils {

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


}
