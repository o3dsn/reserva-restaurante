package br.com.fiap.reservarestaurante.bdd;

import br.com.fiap.reservarestaurante.ReservaRestauranteApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = ReservaRestauranteApplication.class)
public class StepDefsDefault {}
