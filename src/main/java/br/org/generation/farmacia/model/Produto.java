package br.org.generation.farmacia.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_produtos")
@Getter
@Setter
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message="Nome é obrigatório!")
	private String nome;
	
	@NotBlank(message="Descrição é obrigatório!")
	@Size(max = 600)
	private String descricaoBasica;
	
	@NotBlank(message="Descrição aprofundada do produto")
	@Size(max = 5000)
	private String sobre;

	@PositiveOrZero
	private int quantidade;

	private String principioAtivo;
	
	private boolean generico;
	
	private String registroMedico;
	
	private String codigoBarras;
	
	private String dosagem;
	
	private String peso;
	
	private String avisoMedico;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@NotNull(message="Preço é obrigatório!")
	@Positive(message = "Digite um valor maior do que zero")
	private BigDecimal preco;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@NotNull(message="Preço é obrigatório!")
	@Positive(message = "Digite um valor maior do que zero")
	private BigDecimal valorCompra;
	
	private String foto;

	@ManyToOne
	@JsonIgnoreProperties("produto")
	private Categoria categoria;
	
	@ManyToOne
	@JsonIgnoreProperties("produto")
	private Marca marca;

}