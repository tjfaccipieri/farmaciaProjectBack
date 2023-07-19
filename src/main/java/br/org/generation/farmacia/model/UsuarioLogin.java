package br.org.generation.farmacia.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioLogin {

	private long id;
	private String nome;
	private String senha;
	private String email;
	private String tipo;
	private String token;
	
}
