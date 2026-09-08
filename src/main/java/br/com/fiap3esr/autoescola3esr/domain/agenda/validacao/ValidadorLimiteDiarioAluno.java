package br.com.fiap3esr.autoescola3esr.domain.agenda.validacao;

import br.com.fiap3esr.autoescola3esr.domain.agenda.DadosAgendamento;
import br.com.fiap3esr.autoescola3esr.domain.agenda.InstrucaoRepository;
import br.com.fiap3esr.autoescola3esr.domain.agenda.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Não permitir o agendamento de mais de duas instruções no mesmo dia para um mesmo aluno.
 */
@Component
public class ValidadorLimiteDiarioAluno implements ValidadorAgendamento {
    private static final long LIMITE_DIARIO = 2;

    @Autowired
    private InstrucaoRepository repository;

    @Override
    public void validar(DadosAgendamento dados) {
        LocalDate dia = dados.dataHora().toLocalDate();

        long instrucoesNoDia = repository.countByAlunoIdAndDataHoraBetweenAndMotivoCancelamentoIsNull(
                dados.idAluno(),
                dia.atStartOfDay(),
                dia.atTime(LocalTime.MAX)
        );

        if (instrucoesNoDia >= LIMITE_DIARIO) {
            throw new ValidacaoException(
                    "Cada aluno pode ter no máximo duas instruções agendadas no mesmo dia!");
        }
    }
}
