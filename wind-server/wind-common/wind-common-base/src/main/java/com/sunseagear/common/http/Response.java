package com.sunseagear.common.http;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.sunseagear.common.utils.DateUtils;
import com.sunseagear.common.utils.ServletUtils;
import com.sunseagear.common.utils.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.text.StringEscapeUtils;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.common.http
 * @title:
 * @description: 数据响应实体 * @date: 2018/6/24 21:41
 * @copyright: 2017 www.sunseagear.com Inc. All rights reserved.
 */
public class Response {
    private static final int OK_CODE = 0;
    private static final int ERROR_CODE = 500;

    private static final Gson gson;

    static {
        gson = createGson(null, false);
    }


    public static Gson getGson() {
        return getGson(null, false);
    }

    public static Gson getGson(String fields) {
        return getGson(fields, true);
    }

    public static Gson getGson(String fields, boolean isInclude) {
        if (StringUtils.isEmpty(fields)) {
            return gson;
        }
        return createGson(fields, isInclude);

    }

    //如果isInclude为true，保留fields中的字段，否则是不保留
    private static Gson createGson(String fields, boolean isInclude) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLongSerializationPolicy(LongSerializationPolicy.STRING);
        gsonBuilder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(DateUtils.formatDateTime(src));
            }
        });
        gsonBuilder.registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
            public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(DateUtils.formatDateTime(src));
            }
        });
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(DateUtils.formatDateTime(src));
            }
        });

        gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return DateUtils.parseDate(json.getAsJsonPrimitive().getAsString());
            }
        });
        gsonBuilder.registerTypeAdapter(LocalDate.class, new JsonDeserializer() {
            public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                Date date = DateUtils.parseDate(json.getAsJsonPrimitive().getAsString());
                ZoneId zone = ZoneId.systemDefault();
                return date.toInstant().atZone(zone).toLocalDate();
            }
        });
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new JsonDeserializer() {
            public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                Date date = DateUtils.parseDate(json.getAsJsonPrimitive().getAsString());
                ZoneId zone = ZoneId.systemDefault();
                return date.toInstant().atZone(zone).toLocalDateTime();
            }
        });

        gsonBuilder.registerTypeAdapter(String.class, new JsonSerializer<String>() {
            public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(StringEscapeUtils.unescapeHtml4(StringEscapeUtils.unescapeHtml4(src)));
            }
        });

        gsonBuilder.setPrettyPrinting();
        gsonBuilder.disableHtmlEscaping();
        gsonBuilder.enableComplexMapKeySerialization();

        if (!StringUtils.isEmpty(fields)) {

            String[] fieldArray = fields.split(",");
            HashSet<String> fieldSet = new HashSet<>(Arrays.asList(fieldArray));

            gsonBuilder.setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                    if (fieldAttributes.getDeclaringClass() == Result.class) {
                        return false;
                    }
                    if (fieldSet.contains(fieldAttributes.getName())) {
                        return !isInclude;
                    }

                    return isInclude;
                }

                @Override
                public boolean shouldSkipClass(Class<?> aClass) {
                    return false;
                }
            });
        }


//        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
//        gsonBuilder.serializeNulls();
        return gsonBuilder.create();
    }


    public static String ok() {
        return successJson("");
    }

    public static String ok(String msg) {
        return successJson(msg);
    }


    public static String error(String msg) {
        return failJson(ERROR_CODE, msg);
    }

    public static String error(int code, String msg) {
        return failJson(code, msg);
    }

    public static String successJson(String message) {
        return gson.toJson(new Result(message));
    }

    public static <T> String successJson(T object) {
        return successJson(object, null);
    }

    public static <T> String successJson(String msg, T object) {
        return getGson(null).toJson(new Result(object));
    }

    public static <T> String successJson(T object, String includesProperties) {
        return getGson(includesProperties).toJson(new Result(object));
    }

    public static String successPageJson(Page object) {
        return successPageJson(object, null);
    }

    public static String successPageJson(Page object, String includesProperties) {
        return getGson(includesProperties).toJson(new PageResult(object));
    }

    public static <T> String successJson(T object, String includesProperties, boolean isInclude) {
        return getGson(includesProperties, isInclude).toJson(new Result(object));
    }

    public static String successPageJson(Page object, String includesProperties, boolean isInclude) {
        return getGson(includesProperties, isInclude).toJson(new PageResult(object));
    }

    public static <T> String toJson(T object, String mesage) {
        return gson.toJson(new Result(mesage, object));

    }

    public static String failJson(String msg) {
        return failJson(ERROR_CODE, msg);
    }

    public static String failJson(int code, String msg) {
        return gson.toJson(new Result(code, false, msg));
    }

    @Data
    public static class Result {

        private boolean success;
        private int code;
        private String msg;
        private Object data;

        private Result() {
        }

        public Result(Object data) {
            this.code = OK_CODE;
            this.success = true;
            this.msg = "操作成功";
            this.data = data;
        }

        public Result(String msg) {
            this.code = OK_CODE;
            this.success = true;
            this.msg = msg;
        }

        public Result(int code, boolean success, String msg) {
            this.code = code;
            this.success = success;
            this.msg = msg;
        }

        public Result(int code, boolean success, String msg, Object data) {
            this.code = code;
            this.success = success;
            this.msg = msg;
            this.data = data;
        }

        public Result(String msg, Object data) {
            this.code = OK_CODE;
            this.success = true;
            this.msg = msg;
            this.data = data;
        }


    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class PageResult extends Result {

        @JsonAdapter(LongAdapter.class)
        private long current;
        @JsonAdapter(LongAdapter.class)
        private long pages;
        @JsonAdapter(LongAdapter.class)
        private long total;
        @JsonAdapter(LongAdapter.class)
        private long size;

        private PageResult() {
        }

        public PageResult(Page data) {
            super(data.getRecords());
            this.current = data.getCurrent();
            this.pages = data.getPages();
            this.total = data.getTotal();
            this.size = data.getSize();
        }

        public PageResult(int code, boolean success, String msg) {
            super(code, success, msg);
        }

        public PageResult(int code, boolean success, String msg, Page data) {
            super(code, success, msg, data.getRecords());
            this.pages = data.getPages();
            this.total = data.getTotal();
            this.size = data.getSize();

        }

        public PageResult(String msg, Page data) {
            super(msg, data.getRecords());
            this.pages = data.getPages();
            this.total = data.getTotal();
            this.size = data.getSize();
        }

    }

    /*
     * 系统默认会将Long类型转为string，如果需要保持long类型，在使用此适配器
     * */
    public static class LongAdapter implements JsonSerializer<Long> {

        @Override
        public JsonElement serialize(Long s, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(s);
        }

    }

    /*
     * 自动拼接Url，如果返回的地址不是全路径，那么会根据请求进行拼接。
     * 主要用于内外网不同网址的场景。
     * 例如：一个文件在服务器的路径是/file/aaa.docx
     * 如果内网请求返回的是http://内网地址/file/aaa.docx
     * 如果外网请求返回的是http://外网地址/file/aaa.docx
     * */
    public static class ContextUrlAdapter implements JsonSerializer<String>{

        @Override
        public JsonElement serialize(String s, Type type, JsonSerializationContext jsonSerializationContext) {
            if (!StringUtils.isEmpty(s) && !StringUtils.startsWith(s, "http")) {
                return new JsonPrimitive(ServletUtils.getContextUrl(ServletUtils.getRequest()) + s);
            }
            return new JsonPrimitive(s);
        }
    }


}
