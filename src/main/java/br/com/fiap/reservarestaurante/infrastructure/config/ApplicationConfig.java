package br.com.fiap.reservarestaurante.infrastructure.config;

import br.com.fiap.reservarestaurante.application.repositories.AvaliacaoRepository;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.create.AvaliacaoCreateUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.create.DefaultAvaliacaoCreateUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.delete.AvaliacaoDeleteUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.delete.DefaultAvaliacaoDeleteUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.get.AvaliacaoGetByIdUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.get.DefaultAvaliacaoGetByIdUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list.AvaliacaoListUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list.DefaultAvaliacaoListUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list.byidrestaurant.AvaliacaoListByIdRestauranteUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list.byidrestaurant.DefaultAvaliacaoListByIdRestauranteUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.nota.DefaultNotaRestauranteGetByUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.nota.NotaRestauranteGetByIdUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.update.AvaliacaoUpdateUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.update.DefaultAvaliacaoUpdateUseCase;
import br.com.fiap.reservarestaurante.infrastructure.persistence.repositories.AvaliacaoJPARepository;
import br.com.fiap.reservarestaurante.infrastructure.repositories.AvaliacaoRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public AvaliacaoRepository avaliacaoRepository(final AvaliacaoJPARepository avaliacaoJPARepository){
        return new AvaliacaoRepositoryImpl(avaliacaoJPARepository);
    }

    @Bean
    public AvaliacaoCreateUseCase avaliacaoCreateUseCase(final AvaliacaoRepository avaliacaoRepository) {
        return new DefaultAvaliacaoCreateUseCase(avaliacaoRepository);
    }

    @Bean
    public AvaliacaoListUseCase avaliacaoListUseCase(final AvaliacaoRepository avaliacaoRepository) {
        return new DefaultAvaliacaoListUseCase(avaliacaoRepository);
    }

    @Bean
    public AvaliacaoGetByIdUseCase avaliacaoGetByIdUseCase(final AvaliacaoRepository avaliacaoRepository) {
        return new DefaultAvaliacaoGetByIdUseCase(avaliacaoRepository);
    }

    @Bean
    public AvaliacaoListByIdRestauranteUseCase avaliacaoListByIdRestauranteUseCase(final AvaliacaoRepository avaliacaoRepository) {
        return new DefaultAvaliacaoListByIdRestauranteUseCase(avaliacaoRepository);
    }

    @Bean
    public AvaliacaoUpdateUseCase avaliacaoUpdateUseCase(final AvaliacaoRepository avaliacaoRepository) {
        return new DefaultAvaliacaoUpdateUseCase(avaliacaoRepository);
    }

    @Bean
    public AvaliacaoDeleteUseCase avaliacaoDeleteUseCase(final AvaliacaoRepository avaliacaoRepository) {
        return new DefaultAvaliacaoDeleteUseCase(avaliacaoRepository);
    }

    @Bean
    public NotaRestauranteGetByIdUseCase notaRestauranteGetByIdUseCase(final AvaliacaoRepository avaliacaoRepository) {
        return new DefaultNotaRestauranteGetByUseCase(avaliacaoRepository);
    }

}
