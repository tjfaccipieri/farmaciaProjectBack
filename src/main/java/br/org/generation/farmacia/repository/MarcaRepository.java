package br.org.generation.farmacia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.org.generation.farmacia.model.Marca;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long>{
	
	public List<Marca> findAllByMarcaContainingIgnoreCase(@Param("marca") String marca);

}
