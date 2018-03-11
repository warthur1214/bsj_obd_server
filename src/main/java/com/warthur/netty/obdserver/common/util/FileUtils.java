package com.warthur.netty.obdserver.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;

public final class FileUtils {
    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {}

    public static Properties readPropertiesFile(String filePath) {
        Properties prop = new Properties();
        
        String absolutePath = getFileAbsolutePath(filePath);
        
        try (FileInputStream fis = new FileInputStream(absolutePath)) {
            prop.load(fis);
        } catch (Exception e) {
            log.error("failed to read the properties file from: {}", absolutePath, e);
        }

        return prop;
    }

    public static String getFileAbsolutePath(String filePath) {
        String path = FileUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        
		try {
			path = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("Decode file path failed when get file absolute path!", e);
		}
        
        String configPath = path + filePath;
        log.debug("the file {} absolute path is: {}", filePath, configPath);
        return configPath;
    }

    
}
