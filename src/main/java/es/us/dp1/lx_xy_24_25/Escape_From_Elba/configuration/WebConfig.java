package es.us.dp1.lx_xy_24_25.Escape_From_Elba.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {
	
	@Autowired
	GenericIdToEntityConverter idToEntityConverter;
	
    @Override
    public void addFormatters(FormatterRegistry registry) {
    	
        registry.addConverter(idToEntityConverter);
    }


@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // aplica a todos los endpoints
                .allowedOrigins("http://localhost:3000") // frontend React
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // métodos permitidos
                .allowedHeaders("*") // cabeceras permitidas
                .allowCredentials(true); // permite cookies o autorización
    }
    
}