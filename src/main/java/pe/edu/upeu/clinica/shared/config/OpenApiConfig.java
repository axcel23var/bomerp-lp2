package pe.edu.upeu.clinica.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI clinicaDentOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("ClinicaDent API")
                .version("v1")
                .description("Backend monolitico modular de ClinicaDent. "
                        + "Documentacion agrupada por los 4 modulos de la Unidad 1.")
                .contact(new Contact()
                        .name("ClinicaDent UPeU")
                        .email("clinicadent@upeu.edu.pe")));
    }

    @Bean
    public GroupedOpenApi citasApi() {
        return GroupedOpenApi.builder()
                .group("modulo-1-citas")
                .displayName("Modulo 1 - Citas y Admision")
                .pathsToMatch("/api/v1/pacientes/**", "/api/v1/citas/**")
                .build();
    }

    @Bean
    public GroupedOpenApi tratamientosApi() {
        return GroupedOpenApi.builder()
                .group("modulo-2-tratamientos")
                .displayName("Modulo 2 - Catalogo y Tratamientos")
                .pathsToMatch(
                        "/api/v1/especialidades/**",
                        "/api/v1/procedimientos/**",
                        "/api/v1/tratamientos/**")
                .build();
    }

    @Bean
    public GroupedOpenApi atencionesApi() {
        return GroupedOpenApi.builder()
                .group("modulo-3-atenciones")
                .displayName("Modulo 3 - Atenciones Clinicas")
                .pathsToMatch("/api/v1/odontologos/**", "/api/v1/atenciones/**")
                .build();
    }

    @Bean
    public GroupedOpenApi pagosApi() {
        return GroupedOpenApi.builder()
                .group("modulo-4-pagos")
                .displayName("Modulo 4 - Pagos y Caja")
                .pathsToMatch("/api/v1/pagos/**", "/api/v1/metodos-pago/**")
                .build();
    }
}
