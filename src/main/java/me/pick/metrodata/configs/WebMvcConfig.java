package me.pick.metrodata.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/css/**")
				.addResourceLocations("classpath:/static/css/");

		registry.addResourceHandler("/img/**")
				.addResourceLocations("classpath:/static/img/");

		registry.addResourceHandler("/dist/**")
				.addResourceLocations("classpath:/static/dist/");
	}
}