package br.com.fiap.reservarestaurante.infrastructure.persistence.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "avaliacoes")
public class AvaliacaoJPAEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true, length = 36)
    private String id;
}
