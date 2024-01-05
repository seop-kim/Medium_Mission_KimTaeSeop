package com.ll.medium.global.app;

import java.io.IOException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class AppConfig {
    private static String resourcesStaticDirPath;

    @Getter
    public static String tempDirPath;

    @Getter
    public static String genFileDirPath;

    @Getter
    public static String siteName;

    @Getter
    public static String siteBaseUrl;


    public static String getResourcesStaticDirPath() {
        if (resourcesStaticDirPath == null) {
            ClassPathResource resource = new ClassPathResource("static/");
            try {
                resourcesStaticDirPath = resource.getFile().getAbsolutePath();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return resourcesStaticDirPath;
    }
}