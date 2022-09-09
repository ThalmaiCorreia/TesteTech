package com.techsolutio.teste.model;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity // Notação necessaria para criação de tabela
@Table(name = "tb_fornecedor")// Notação para nomear a tabela
public class Fornecedor {
    @Id //chave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)//é o auto incremento
    private Long id;

    @NotBlank(message = "O atributo seção é obrigatório, e não pode conter espaços em branco!")
    @Size(max = 50, message = "O atributo seção deve conter no máximo 50 caracteres")
    private String secao;

    @NotBlank(message = "O atributo descrição é obrigatório, e não pode conter espaços em branco!")
    @Size(min = 5, max = 1000, message = "O atributo descrição deve conter no mínimo 05 e no máximo 100 caracteres")
    private String descricao;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties("categoria")
    private List<Produto> produto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSecao() {
        return secao;
    }

    public void setSecao(String secao) {
        this.secao = secao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Produto> getProduto() {
        return produto;
    }

    public void setProduto(List<Produto> produto) {
        this.produto = produto;
    }
}
