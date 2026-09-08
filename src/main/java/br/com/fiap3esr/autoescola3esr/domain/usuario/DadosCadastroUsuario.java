package br.com.fiap3esr.autoescola3esr.domain.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosCadastroUsuario(
        @NotBlank
        String login,

        @NotBlank
        @Size(min = 4)
        String senha,

        Role perfil) {
}
