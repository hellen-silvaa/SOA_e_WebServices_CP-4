package br.com.fiap3esr.autoescola3esr.domain.instrutor;

import br.com.fiap3esr.autoescola3esr.domain.endereco.DadosEndereco;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * O telefone é ocultado enquanto não tiver sido informado (regra de negócio:
 * "ocultar essa informação, inicialmente, para posterior atualização").
 * Depois de preenchido via PUT /instrutores ele passa a aparecer normalmente.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DadosDetalhamentoInstrutor(
        Long id,
        String nome,
        String email,
        String telefone,
        String cnh,
        Especialidade especialidade,
        DadosEndereco endereco,
        boolean ativo) {
    public DadosDetalhamentoInstrutor(Instrutor instrutor) {
        this(
                instrutor.getId(),
                instrutor.getNome(),
                instrutor.getEmail(),
                instrutor.getTelefone(),
                instrutor.getCnh(),
                instrutor.getEspecialidade(),
                new DadosEndereco(instrutor.getEndereco()),
                instrutor.isAtivo()
        );
    }
}