package br.com.fiap3esr.autoescola3esr.domain.usuario;

public record DadosDetalhamentoUsuario(
        Long id,
        String login,
        Role perfil) {
    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getLogin(),
                usuario.getPerfil()
        );
    }
}
