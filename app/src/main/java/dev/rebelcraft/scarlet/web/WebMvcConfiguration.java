package dev.rebelcraft.scarlet.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;

@Configuration
public class WebMvcConfiguration {

    @Bean
    public ViewResolver j2htmlViewResolver() {
        return new BeanNameViewResolver();
    }

}
