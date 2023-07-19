package br.org.generation.farmacia.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.generation.farmacia.model.Marca;
import br.org.generation.farmacia.repository.MarcaRepository;

@RestController
@RequestMapping("/marcas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MarcaController {
	
	@Autowired
	private MarcaRepository marcaRepository;
	
	@GetMapping
	public ResponseEntity<List<Marca>> getAll(){
		return ResponseEntity.ok(marcaRepository.findAll());
	}
	
	@GetMapping("/{id}") 
	public ResponseEntity<Marca> getById(@PathVariable Long id){
		return marcaRepository.findById(id)
			.map(resp -> ResponseEntity.ok(resp))
			.orElse(ResponseEntity.notFound().build());	
	}
	
	@GetMapping("/nome/{marca}")
	public ResponseEntity<List<Marca>> getByMarca(@PathVariable String marca){
		return ResponseEntity.ok(marcaRepository.findAllByMarcaContainingIgnoreCase(marca));
	}
	
	@PostMapping
	public ResponseEntity<Marca> postMarca(@Valid @RequestBody Marca marca){
		return ResponseEntity.status(HttpStatus.CREATED).body(marcaRepository.save(marca));	
	}
	
	@PutMapping
	public ResponseEntity<Marca> putMarca(@Valid @RequestBody Marca marca) {
					
		return marcaRepository.findById(marca.getId())
				.map(resposta -> ResponseEntity.ok().body(marcaRepository.save(marca)))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteMarca(@PathVariable Long id) {
		
		return marcaRepository.findById(id)
				.map(resposta -> {
					marcaRepository.deleteById(id);
					return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
				})
				.orElse(ResponseEntity.notFound().build());
	}

}
