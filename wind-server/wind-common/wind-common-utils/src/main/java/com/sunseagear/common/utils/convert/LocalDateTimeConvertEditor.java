package com.sunseagear.common.utils.convert;

import com.sunseagear.common.utils.DateUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 时间处理类
 *
 * @version V 1.0
 * @date 2016-12-21
 */
public class LocalDateTimeConvertEditor extends PropertyEditorSupport {

    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.hasText(text)) {
            try {
                Date date = DateUtils.parseDate(text);
                ZoneId zone = ZoneId.systemDefault();
                LocalDateTime localDate = date.toInstant().atZone(zone).toLocalDateTime();
                if (date == null) {
                    long value = Long.parseLong(text);
                    localDate = Instant.ofEpochMilli(value).atZone(zone).toLocalDateTime();
                }
                setValue(localDate);
            } catch (Exception ex) {
                try {
                } catch (Exception e) {
                    IllegalArgumentException iae = new IllegalArgumentException("Could not parse date: " + ex.getMessage());
                    iae.initCause(ex);
                    throw iae;
                }
            }
        } else {
            setValue(null);
        }
    }
}
