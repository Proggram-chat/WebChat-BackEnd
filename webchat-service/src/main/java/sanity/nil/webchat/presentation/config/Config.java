package sanity.nil.webchat.presentation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerAdapter;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Webchat REST API", version = "3.0",
        description = "Webchat application",
        contact = @Contact(name = "sanity")),
        security = {@SecurityRequirement(name = "bearerToken")}
)
//@SecurityScheme(name = "bearerToken", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class Config {

//    @Bean
//    public HandlerAdapter wsHandlerAdapter() {
//        return new WebSocketHandlerAdapter();
//    }
//
//    @Bean
//    public HandlerMapping chatMapping(
//            @Qualifier("chatController") WebSocketHandler webSocketHandler
//    ) {
//        Map<String, WebSocketHandler> map = new HashMap<>();
//        map.put("/user/{userId}", webSocketHandler);
//        int order = Ordered.HIGHEST_PRECEDENCE;
//        return new SimpleUrlHandlerMapping(map, order);
//    }

}
