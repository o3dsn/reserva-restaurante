package br.com.fiap.reservarestaurante;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ReservaRestauranteApplication {

  public static void main(String[] args) {
    SpringApplication.run(ReservaRestauranteApplication.class, args);
  }
}
