package br.org.generation.farmacia.service;

import java.nio.charset.Charset;
import java.util.Optional;

import javax.mail.MessagingException;

import br.org.generation.farmacia.email.SmtpEmailSender;
import br.org.generation.farmacia.model.Usuario;
import br.org.generation.farmacia.model.UsuarioLogin;
import br.org.generation.farmacia.repository.UsuarioRepository;
import br.org.generation.farmacia.security.JwtService;
import org.apache.commons.codec.binary.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private SmtpEmailSender smtpEmailSender;

    public Optional<Usuario> cadastrarUsuario(Usuario usuario){
        if(usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
        	throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário já existe!", null);
        }
        usuario.setSenha(criptografarSenha(usuario.getSenha()));
        String name = usuario.getNome();
        try {
			smtpEmailSender.send(usuario.getEmail(), "Olá " + name + " Seja bem vindo a FarmaGen!", "<h2>Aproveite o nosso catalogo</h2>"
					+ "<h4>Aproveite as suas compras "
					+ "e as nossas promoções, que sempre estarão disponiveis </h4>");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return Optional.of(usuarioRepository.save(usuario));
    }


    private boolean compararSenhas(String senhaDigitada, String senhaDB) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(senhaDigitada, senhaDB);
    }


    private String criptografarSenha(String senha) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(senha);
    }


    private String gerarToken(String usuario) {
        return "Bearer " + jwtService.generateToken(usuario);
    }


    public Optional<Usuario> atualizarUsuario(Usuario usuario) {
        if (usuarioRepository.findById(usuario.getId()).isPresent()) {
            Optional<Usuario> buscaUsuario = usuarioRepository.findByEmail(usuario.getEmail());
            if (buscaUsuario.isPresent()) {
                if (buscaUsuario.get().getId() != usuario.getId())
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);
            }
            usuario.setSenha(criptografarSenha(usuario.getSenha()));
            return Optional.of(usuarioRepository.save(usuario));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!", null);
    }

    public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin){
        var credenciais = new UsernamePasswordAuthenticationToken(usuarioLogin.get().getEmail(), usuarioLogin.get().getSenha());

        Authentication authentication = authenticationManager.authenticate(credenciais);
        Optional<Usuario> usuario = usuarioRepository.findByEmail(usuarioLogin.get().getEmail());
        if(usuario.isPresent()) {
            if(compararSenhas(usuarioLogin.get().getSenha(), usuario.get().getSenha())) {
                usuarioLogin.get().setId(usuario.get().getId());
                usuarioLogin.get().setNome(usuario.get().getNome());
                usuarioLogin.get().setToken(gerarToken
                        (usuarioLogin.get().getEmail()));
                usuarioLogin.get().setSenha("");
                usuarioLogin.get().setTipo(usuario.get().getTipo());
                usuarioLogin.get().setEmail(usuario.get().getEmail());


                return usuarioLogin;
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        }
        return Optional.empty();
    }
    
}
