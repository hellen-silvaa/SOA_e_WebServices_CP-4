package br.com.fiap3esr.autoescola3esr.domain.agenda.validacao;

import br.com.fiap3esr.autoescola3esr.domain.agenda.DadosAgendamento;

public interface ValidadorAgendamento {
    void validar(DadosAgendamento dados);
}