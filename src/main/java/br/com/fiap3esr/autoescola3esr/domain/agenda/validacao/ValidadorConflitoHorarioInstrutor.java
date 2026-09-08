package br.com.fiap3esr.autoescola3esr.domain.agenda.validacao;

import br.com.fiap3esr.autoescola3esr.domain.agenda.DadosAgendamento;
import br.com.fiap3esr.autoescola3esr.domain.agenda.InstrucaoRepository;
import br.com.fiap3esr.autoescola3esr.domain.agenda.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorConflitoHorarioInstrutor implements ValidadorAgendamento {
    @Autowired
    private InstrucaoRepository repository;

    @Override
    public void validar(DadosAgendamento dados) {
        if (dados.idInstrutor() == null) {
            // Instrutor não informado: será sorteado pelo serviço, e a query de
            // sorteio já exclui instrutores ocupados na data/hora.
            return;
        }
        boolean instrutorOcupado = repository.existsByInstrutorIdAndDataHoraAndMotivoCancelamentoIsNull(
                dados.idInstrutor(),
                dados.dataHora()
        );

        if (instrutorOcupado) {
            throw new ValidacaoException("Instrutor indisponível na data/hora escolhida!");
        }
    }
}