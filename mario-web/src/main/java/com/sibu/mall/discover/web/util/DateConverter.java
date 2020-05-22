package com.mall.discover.web.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author taowei
 */
public class DateConverter {

    private static final ThreadLocal<DateFormat> DF = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    public static String formatDateTime(Date date) {
        if (date == null) {
            return "";
        }
        return DF.get().format(date);
    }
}
