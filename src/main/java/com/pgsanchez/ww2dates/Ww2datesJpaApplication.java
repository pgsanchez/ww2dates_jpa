package com.pgsanchez.ww2dates;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@ComponentScan(basePackages = {"com.pgsanchez.ww2dates.controller", 
		"com.pgsanchez.ww2dates.dao", 
		"com.pgsanchez.ww2dates.securingweb",
		"com.pgsanchez.ww2dates.service",
		"com.pgsanchez.ww2dates"})
@EntityScan(basePackages= {"com.pgsanchez.ww2dates.model"}) //Packages donde tiene que buscar clases del modelo
@EnableJpaRepositories(basePackages= {"com.pgsanchez.ww2dates.dao"})
@SpringBootApplication
public class Ww2datesJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ww2datesJpaApplication.class, args);
	}

}
