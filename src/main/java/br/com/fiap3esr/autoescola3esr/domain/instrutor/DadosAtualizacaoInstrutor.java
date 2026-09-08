package br.com.fiap3esr.autoescola3esr.domain.instrutor;

import br.com.fiap3esr.autoescola3esr.domain.endereco.DadosEndereco;
import jakarta.validation.constraints.NotNull;

/**
 * Somente nome, telefone e endereço podem ser atualizados.
 * O e-mail, a CNH e a especialidade do instrutor não podem ser alterados
 * (regra de negócio), por isso não fazem parte deste record.
 */
public record DadosAtualizacaoInstrutor(
        @NotNull
        Long id,
        String nome,
        String telefone,
        DadosEndereco endereco) {
}
