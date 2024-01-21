package com.sunseagear.common.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * Properties文件工具类
 *
 * @version 2017-01-19
 */
public class PropertiesUtil extends ObjectSwitchHelper {
    /**
     * 日志对象
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private Properties properties;
    private String[] resourcesPaths;
    private String properiesName = "";

    public PropertiesUtil() {

    }

    public static PropertiesUtil create(String... resourcesPaths) {
        return new PropertiesUtil(resourcesPaths);
    }

    public PropertiesUtil(String... resourcesPaths) {
        load(resourcesPaths);
    }

    public void load(String... resourcesPaths) {
        this.resourcesPaths = resourcesPaths;
        properties = new Properties();
        for (String location : resourcesPaths) {
            logger.debug("Loading properties file from:" + location);
            InputStream is = null;
            try {
                is = PropertiesUtil.class.getClassLoader().getResourceAsStream(location);
                properties.load(is);
            } catch (Exception ex) {
                logger.info("Could not load properties from path:" + location + ", " + ex.getMessage());
            } finally {
                IOUtils.closeQuietly(is);
            }
        }
    }

    /**
     * 更新与删除时指定文件名称
     */
    public void setOptProperiesName(String properiesName) {
        this.properiesName = properiesName;
    }

    @Override
    public Object get(String key) {
        if (properties.containsKey(key)) {
            return properties.getProperty(key);
        }
        return "";
    }

    public String getAbsolutePath(String filename) {
        if (!FileUtils.isAbsolutePath(filename)) {
            filename = Objects.requireNonNull(PropertiesUtil.class.getClassLoader().getResource(filename)).getPath();
        }
        return filename;
    }

    @Override
    public void set(String key, Object value) {
        if (StringUtils.isEmpty(this.properiesName)) {
            this.properiesName = this.resourcesPaths[0];
        }
        try (InputStream is = Files.newInputStream(Paths.get(getAbsolutePath(this.properiesName)));
             OutputStream os = Files.newOutputStream(Paths.get(getAbsolutePath(this.properiesName)))) {
            Properties p = new Properties();
            p.load(is);
            p.setProperty(key, value + "");
            p.store(os, "====配置更新====");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.properiesName = "";
        }
    }

    public void set(Map<String, Object> dataMap) {
        if (StringUtils.isEmpty(this.properiesName)) {
            this.properiesName = this.resourcesPaths[0];
        }
        try (InputStream is = new FileInputStream(getAbsolutePath(this.properiesName));
             OutputStream os = new FileOutputStream(getAbsolutePath(this.properiesName));) {
            Properties p = new Properties();
            FileUtils.createFile(getAbsolutePath(this.properiesName));
            p.load(is);
            for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                p.setProperty(entry.getKey(), entry.getValue() + "");
            }
            p.store(os, "====配置更新====");
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.properiesName = "";
        }

    }

    @Override
    public void remove(String key) {
        if (StringUtils.isEmpty(this.properiesName)) {
            this.properiesName = this.resourcesPaths[0];
        }

        try (InputStream is = new FileInputStream(getAbsolutePath(this.properiesName));
             OutputStream os = new FileOutputStream(getAbsolutePath(this.properiesName));) {
            Properties p = new Properties();
            p.load(is);
            p.remove(key);
            p.store(os, "");
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.properiesName = "";
        }

    }

    public static void main(String[] args) {
        String properiesName = "generation/generation_config.properties";
        PropertiesUtil propertiesUtil = new PropertiesUtil(properiesName);
        String sdfsdf = propertiesUtil.getString("project_path");
        System.out.println(sdfsdf);
        // 指定更新与删除时指定文件名称
        // propertiesUtil.setOptProperiesName("generation/generation_config.properties");
        // propertiesUtil.set("project_path", "sdfsdfsd");

    }

}
