package com.sunseagear.common.utils;

import java.util.Arrays;

/**
 * 加载基础
 */
public abstract class ObjectSwitchHelper {

    public void setString(String key, String value) {
        set(key, value);
    }

    public void setInt(String key, int value) {
        setString(key, String.valueOf(value));
    }

    public void setBoolean(String key, Boolean value) {
        setString(key, String.valueOf(value));
    }

    public void setByte(String key, byte[] value) {
        setString(key, Arrays.toString(value));
    }

    public void setShort(String key, short value) {

        setString(key, String.valueOf(value));
    }

    public void setLong(String key, long value) {

        setString(key, String.valueOf(value));
    }

    public void setFloat(String key, float value) {
        setString(key, String.valueOf(value));
    }

    public void setDouble(String key, double value) {
        setString(key, String.valueOf(value));
    }

    public String getString(String key) {
        if (get(key) == null) {
            return "";
        }
        return String.valueOf(get(key));
    }

    public int getInt(String key) {
        return Integer.parseInt(getString(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(getString(key));
    }

    public byte[] getByte(String key) {
        return getString(key).getBytes();
    }

    public short getShort(String key) {
        return Short.parseShort(getString(key, ""));
    }

    public long getLong(String key) {
        return Long.parseLong(getString(key));
    }

    public float getFloat(String key) {
        return Float.parseFloat(getString(key));
    }

    public double getDouble(String key) {
        return Double.parseDouble(getString(key));
    }

    public String getString(String key, String defaultValue) {
        return get(key) != null && !StringUtils.isEmpty((String) get(key)) ? String.valueOf(get(key)) : defaultValue;
    }

    public int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(getString(key, ""));
        } catch (Exception ignored) {

        }
        return defaultValue;
    }

    public boolean getBoolean(String key, Boolean defaultValue) {
        try {
            return Boolean.parseBoolean(getString(key, ""));
        } catch (Exception ignored) {

        }
        return defaultValue;
    }

    public byte[] getByte(String key, byte[] defaultValue) {
        try {
            return getString(key, "").getBytes();
        } catch (Exception ignored) {

        }
        return defaultValue;
    }

    public short getShort(String key, Short defaultValue) {
        try {
            return Short.parseShort(getString(key, ""));
        } catch (Exception ignored) {

        }
        return defaultValue;
    }

    public long getLong(String key, Long defaultValue) {

        try {
            return Long.parseLong(getString(key, ""));
        } catch (Exception ignored) {

        }
        return defaultValue;
    }

    public float getFloat(String key, Float defaultValue) {

        try {
            return Float.parseFloat(getString(key, ""));
        } catch (Exception ignored) {

        }
        return defaultValue;
    }

    public double getDouble(String key, Double defaultValue) {
        try {
            return Double.parseDouble(getString(key, ""));
        } catch (Exception ignored) {

        }
        return defaultValue;
    }

    public void remove(String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    public abstract Object get(String key);

    public abstract void set(String key, Object value);

    public abstract void remove(String key);

}
