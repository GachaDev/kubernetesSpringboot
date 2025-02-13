package com.es.crmInmobiliaria.util;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {
    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("API para un CRM de una Inmobiliaria");

        Contact myContact = new Contact();
        myContact.setName("Jesús Chaves");

        Info information = new Info()
                .title("API CRM INMOBILIARIA")
                .version("1.0")
                .description("API para un CRM de una Inmobiliaria")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}