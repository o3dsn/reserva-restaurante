package br.com.fiap.reservarestaurante.infrastructure.api;

import br.com.fiap.avaliacao.api.AvaliacoesApi;
import br.com.fiap.avaliacao.model.*;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.create.AvaliacaoCreateUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.delete.AvaliacaoDeleteUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.get.AvaliacaoGetByIdUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list.AvaliacaoListUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list.byidrestaurant.AvaliacaoListByIdRestauranteUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.nota.NotaRestauranteGetByIdUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.update.AvaliacaoUpdateUseCase;
import br.com.fiap.reservarestaurante.infrastructure.mappers.AvaliacaoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class AvaliacaoController implements AvaliacoesApi {

    private static final AvaliacaoMapper avaliacaoMapper = AvaliacaoMapper.INSTANCE;
    private final AvaliacaoCreateUseCase avaliacaoCreateUseCase;
    private final AvaliacaoListUseCase avaliacaoListUseCase;
    private final AvaliacaoGetByIdUseCase avaliacaoGetByIdUseCase;
    private final AvaliacaoListByIdRestauranteUseCase avaliacaoListByIdRestauranteUseCase;
    private final AvaliacaoUpdateUseCase avaliacaoUpdateUseCase;
    private final AvaliacaoDeleteUseCase avaliacaoDeleteUseCase;
    private final NotaRestauranteGetByIdUseCase notaRestauranteGetByIdUseCase;

    @Override
    public ResponseEntity<AvaliacaoDTO> atualizarAvaliacao(final String id, final AtualizaAvaliacaoDTO body) {
        final var input = avaliacaoMapper.fromDTO(id, body);
        final var output = avaliacaoMapper.toDTO(avaliacaoUpdateUseCase.execute(input));
        return ResponseEntity.ok(output);
    }

    @Override
    public ResponseEntity<AvaliacaoDTO> buscarAvaliacaoPorId(final String id) {
        final var output = avaliacaoMapper.toDTO(avaliacaoGetByIdUseCase.execute(id));
        return ResponseEntity.ok(output);
    }

    @Override
    public ResponseEntity<PaginadaAvaliacaoDTO> buscarAvaliacaoPorIdRestaurante(final String id, final Integer pagina, final Integer porPagina) {
        final var input = avaliacaoMapper.fromDTO(new Page(pagina, porPagina), id);
        final var avaliacoes = avaliacaoListByIdRestauranteUseCase.execute(input).mapItems(avaliacaoMapper::toDTO);
        final var paginatedAvaliacoes = new PaginadaAvaliacaoDTO()
                .items(avaliacoes.items())
                .pagina(avaliacoes.currentPage())
                .porPagina(avaliacoes.perPage())
                .total(avaliacoes.total());
        return ResponseEntity.ok(paginatedAvaliacoes);
    }

    @Override
    public ResponseEntity<PaginadaAvaliacaoDTO> buscarAvaliacoes(Integer pagina, Integer porPagina, Boolean ativo) {
        final var input = avaliacaoMapper.fromDTO(new Page(pagina, porPagina), ativo);
        final var avaliacoes = avaliacaoListUseCase.execute(input).mapItems(avaliacaoMapper::toDTO);
        final var paginatedAvaliacoes = new PaginadaAvaliacaoDTO()
                .items(avaliacoes.items())
                .pagina(avaliacoes.currentPage())
                .porPagina(avaliacoes.perPage())
                .total(avaliacoes.total());
        return ResponseEntity.ok(paginatedAvaliacoes);
    }

    @Override
    public ResponseEntity<NotaRestauranteDTO> buscarNotaPorIdRestaurante(String id) {
        final var nota = avaliacaoMapper.toDTO(notaRestauranteGetByIdUseCase.execute(id));
        return ResponseEntity.ok(nota);
    }

    @Override
    public ResponseEntity<AvaliacaoDTO> cadastrarAvaliacao(
            final String reservaId,
            final String usuarioId,
            final CriaAvaliacaoDTO body) {
        final var useCaseInput = avaliacaoMapper.fromDTO(reservaId, usuarioId, body);
        final var useCaseOutput = avaliacaoCreateUseCase.execute(useCaseInput);
        var uri = URI.create("/avaliacoes/" + useCaseOutput.id());
        return ResponseEntity.created(uri).body(avaliacaoMapper.toDTO(useCaseOutput));
    }

    @Override
    public ResponseEntity<Void> deletarAvaliacao(String id) {
        avaliacaoDeleteUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
