package me.pick.metrodata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@SpringBootApplication
public class MetrodataApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetrodataApplication.class, args);
        System.out.println("\n <================ Application started ================>\n");
    }

    @Configuration
    public class GraphQLConfig extends WebMvcConfigurationSupport {
        @Override
        protected void addResourceHandlers(ResourceHandlerRegistry registry) {
            // Handler untuk CSS
            registry.addResourceHandler("/css/**")
                  .addResourceLocations("classpath:/static/css/");
            
            // Handler untuk gambar
            registry.addResourceHandler("/img/**")
                  .addResourceLocations("classpath:/static/img/");

            // Handler untuk dist
            registry.addResourceHandler("/dist/**")
                  .addResourceLocations("classpath:/static/dist/");
        }
    }
}
