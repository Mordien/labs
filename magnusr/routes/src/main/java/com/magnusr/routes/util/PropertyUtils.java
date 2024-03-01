package com.magnusr.routes.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtils {

    public static Properties loadProperties(String propertiesFileName) throws IOException {
        Properties configuration = new Properties();
        InputStream inputStream = PropertyUtils.class
                .getClassLoader()
                .getResourceAsStream(propertiesFileName);
        configuration.load(inputStream);
        inputStream.close();

        return configuration;
    }

}
