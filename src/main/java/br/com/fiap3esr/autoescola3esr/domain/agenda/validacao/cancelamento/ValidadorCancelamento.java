package br.com.fiap3esr.autoescola3esr.domain.agenda.validacao.cancelamento;

import br.com.fiap3esr.autoescola3esr.domain.agenda.Instrucao;

public interface ValidadorCancelamento {
    void validar(Instrucao instrucao);
}
