package br.org.generation.farmacia.repository;

import br.org.generation.farmacia.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    public Optional<Usuario> findByEmail(String email);
    
    public Optional<Usuario> findByEmailOrCpf(String email, String cpf);

}
