INSERT INTO restaurantes (
    id, nome, descricao, endereco, cidade,
    estado, bairro, tipo_cozinha_id, faixa_preco, telefone,
    email, avaliacao_media, avaliacao_total, horario_abertura, horario_fechamento)
VALUES
    ('177d17ed-9b8b-480f-becf-bb57c896f0f6','Restaurante 1', 'Restaurante 1 desc', 'Rua do restaurante', 'São Paulo',
    'SP', 'bairro', 'RUSTICA', '$$', '+55 11 1111-1111',
    'restaurante1@gmail.com', 0, 0, '06:00:00', '22:00:00');

INSERT INTO restaurantes (
    id, nome, descricao, endereco, cidade,
    estado, bairro, tipo_cozinha_id, faixa_preco, telefone,
    email, avaliacao_media, avaliacao_total, horario_abertura, horario_fechamento)
VALUES
    ('177d17ed-9b8b-480f-becf-bb57c896f0f7','Restaurante 1', 'Restaurante 1 desc', 'Rua do restaurante', 'São Paulo',
     'SP', 'bairro', 'RUSTICA', '$$', '+55 11 1111-1111',
     'restaurante1@gmail.com', 0, 0, '06:00:00', '22:00:00');

INSERT INTO usuarios (
    id
)
VALUES
    ('afaa347c-b698-4e51-b71a-d861c5f480ba');

INSERT INTO reservas (
    id, restaurante_id, usuario_id, status, comentario,
    data_horario_reserva, criacao, alteracao
)
VALUES
    ('093bff48-6e42-4939-99d9-959f61b41fdd','177d17ed-9b8b-480f-becf-bb57c896f0f6', 'afaa347c-b698-4e51-b71a-d861c5f480ba', 'FINALIZADA', '',
    '2024-01-02T14:00:00.000Z', '2023-12-26T13:00:00.000Z', '2024-01-02T15:00:00.000Z'),
    ('bb0bd99f-9ae0-4901-b484-8438fb72308d','177d17ed-9b8b-480f-becf-bb57c896f0f6', 'afaa347c-b698-4e51-b71a-d861c5f480ba', 'FINALIZADA', '',
    '2024-01-15T14:00:00.000Z', '2024-01-10T13:00:00.000Z', null),
    ('d750f2ba-568b-4d39-98fa-c525736be003','177d17ed-9b8b-480f-becf-bb57c896f0f6', 'afaa347c-b698-4e51-b71a-d861c5f480ba', 'FINALIZADA', '',
    '2024-01-20T14:00:00.000Z', '2024-01-20T13:00:00.000Z', null),
    ('828a24a8-4e99-4c68-a476-3c9397bb4e97','177d17ed-9b8b-480f-becf-bb57c896f0f6', 'afaa347c-b698-4e51-b71a-d861c5f480ba', 'CONFIRMADA', 'reserva para ser avaliada pelo AvaliacaoControllerIT',
    '2024-01-20T14:00:00.000Z', '2024-01-20T13:00:00.000Z', '2024-01-20T15:00:00.000Z'),
    ('81d46912-62bf-4c38-b638-3f569478e369','177d17ed-9b8b-480f-becf-bb57c896f0f6', 'afaa347c-b698-4e51-b71a-d861c5f480ba', 'CONFIRMADA', 'reserva para lançar excecao AvaliacaoControllerIT',
        '2024-01-20T14:00:00.000Z', '2024-01-20T13:00:00.000Z', null),
    ('be147699-ff04-4503-a48e-5d5740739272','177d17ed-9b8b-480f-becf-bb57c896f0f6', 'afaa347c-b698-4e51-b71a-d861c5f480ba', 'FINALIZADA', 'reserva para lançar excecao AvaliacaoControllerIT',
        '2024-01-20T14:00:00.000Z', '2024-01-20T13:00:00.000Z', '2024-01-20T15:00:00.000Z');;

INSERT INTO avaliacoes(
    id, reserva_id, usuario_id,
    criacao, alteracao, exclusao, ativo,
    comentario, nota
)
VALUES
    ('73168510-2714-4cd9-a2e6-149b3fc862d6','093bff48-6e42-4939-99d9-959f61b41fdd','afaa347c-b698-4e51-b71a-d861c5f480ba',
    '2024-11-01 00:00:00.10000', null, null, true,
    'O empenho em analisar a execução dos pontos do programa nos obriga à análise das condições inegavelmente apropriadas.', 4.3),
    ('d4322250-3fc0-49f2-99df-0ca87c40dc5a','bb0bd99f-9ae0-4901-b484-8438fb72308d','afaa347c-b698-4e51-b71a-d861c5f480ba',
    '2024-10-01 00:00:00.20000', null, '2024-10-07 00:00:00.20000', false,
    'O empenho em analisar a execução dos pontos do programa nos obriga à análise das condições inegavelmente apropriadas.', 2.7),
    ('3d4abf43-22ce-4d83-ba4c-d8bafe71569f','d750f2ba-568b-4d39-98fa-c525736be003','afaa347c-b698-4e51-b71a-d861c5f480ba',
    '2024-10-05 00:00:00.30000', '2024-10-06 00:00:00.30000', null, true,
    'O empenho em analisar a execução dos pontos do programa nos obriga à análise das condições inegavelmente apropriadas.', 1.4);




