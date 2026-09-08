package br.com.fiap3esr.autoescola3esr.service;

import br.com.fiap3esr.autoescola3esr.domain.agenda.ValidacaoException;
import br.com.fiap3esr.autoescola3esr.domain.usuario.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario cadastrar(DadosCadastroUsuario dados) {
        if (repository.existsByLogin(dados.login())) {
            throw new ValidacaoException("Já existe um usuário cadastrado com esse login!");
        }
        String senhaCriptografada = passwordEncoder.encode(dados.senha());
        Usuario usuario = new Usuario(dados.login(), senhaCriptografada, dados.perfil());
        return repository.save(usuario);
    }

    public Page<Usuario> listar(Pageable paginacao) {
        return repository.findAll(paginacao);
    }

    @Transactional
    public Usuario atualizarPerfil(DadosAtualizacaoUsuario dados) {
        Usuario usuario = repository.findById(dados.id())
                .orElseThrow(() -> new UsuarioNotFoundException("ID do usuário informado não existe!"));
        usuario.atualizarPerfil(dados.perfil());
        return usuario;
    }

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new UsuarioNotFoundException("ID do usuário informado não existe!");
        }
        repository.deleteById(id);
    }

    /**
     * Permite que o próprio usuário autenticado troque a sua senha,
     * mediante confirmação da senha atual.
     */
    @Transactional
    public void alterarPropriaSenha(String login, DadosAlteracaoSenha dados) {
        Usuario usuario = repository.findByLogin(login);
        if (usuario == null) {
            throw new UsuarioNotFoundException("Usuário autenticado não encontrado!");
        }
        if (!passwordEncoder.matches(dados.senhaAtual(), usuario.getSenha())) {
            throw new ValidacaoException("A senha atual informada está incorreta!");
        }
        usuario.alterarSenha(passwordEncoder.encode(dados.novaSenha()));
    }
}
