package br.com.fiap3esr.autoescola3esr.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByLogin(String login);

    boolean existsByLogin(String login);
}
