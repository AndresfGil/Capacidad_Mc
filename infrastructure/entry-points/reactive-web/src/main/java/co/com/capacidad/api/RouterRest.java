package co.com.capacidad.api;

import co.com.capacidad.api.docs.CapacidadControllerDocs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest implements CapacidadControllerDocs {

    @Bean
    @Override
    public RouterFunction<ServerResponse> routerFunction(CapacidadHandler handler) {
        return route(POST("/api/capacidad"), handler::listenGuardarCapacidad)
                .andRoute(GET("/api/capacidad"), handler::listenListarCapacidades)
                .andRoute(POST("/api/capacidad/batch"), handler::listenObtenerCapacidadesPorIds);
    }
}
