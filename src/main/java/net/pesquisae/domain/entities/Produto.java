package net.pesquisae.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.UUID;

@Entity(name = "produtos")
public class Produto {
    @Id
    UUID id;
    String nome;
    String descricao;
    BigDecimal preco;
    String categoria;
    String image;

    public Produto() {}

    public Produto(
            String nome,
            String descricao,
            BigDecimal preco,
            String categoria,
            String image
    ) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.categoria = categoria;
        this.image = image;
    }
}