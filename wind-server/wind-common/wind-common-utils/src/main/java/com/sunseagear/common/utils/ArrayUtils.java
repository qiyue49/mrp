package com.sunseagear.common.utils;

import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayUtils extends org.apache.commons.lang3.ArrayUtils {
    public static List<String> split(final String str, final String separatorChar) {
        String[] strs = StringUtils.split(str, separatorChar);
        return new ArrayList<>(Arrays.asList(strs));
    }

    public static String join(final List<String> str, final String separatorChar) {
        final StringBuffer strList = new StringBuffer();
        str.forEach(item -> strList.append(separatorChar).append(item));
        return strList.substring(1);
    }
}
