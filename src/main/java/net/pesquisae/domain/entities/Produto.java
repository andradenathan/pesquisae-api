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
    BigDecimal preco;
    String image;

    public Produto() {}

    public Produto(
            String nome,
            BigDecimal preco,
            String image
    ) {
        this.nome = nome;
        this.preco = preco;
        this.image = image;
    }
}