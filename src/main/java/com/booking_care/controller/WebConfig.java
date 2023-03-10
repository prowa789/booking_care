package com.booking_care.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/dang-ki").setViewName("dang-ki");
        registry.addViewController("/").setViewName("gioi-thieu");
        registry.addViewController("/dich-vu").setViewName("dich-vu");
        registry.addViewController("/ve-chung-toi").setViewName("about");
        registry.addViewController("/trang-chu").setViewName("gioi-thieu");
        registry.addViewController("/danh-gia").setViewName("review");
        registry.addViewController("/403").setViewName("403");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory("baidang-photos", registry);
        exposeDirectory("bacsy-photos", registry);
        exposeDirectory("benhnhan-photos", registry);
    }

    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get(dirName);
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        if (dirName.startsWith("../")) dirName = dirName.replace("../", "");

        registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/" + uploadPath + "/");
    }
}
