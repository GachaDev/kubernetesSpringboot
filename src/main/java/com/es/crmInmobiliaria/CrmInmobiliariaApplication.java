package com.es.crmInmobiliaria;

import com.es.crmInmobiliaria.security.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class CrmInmobiliariaApplication {
	public static void main(String[] args) {
		SpringApplication.run(CrmInmobiliariaApplication.class, args);
	}
}
