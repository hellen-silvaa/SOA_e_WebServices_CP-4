package br.com.fiap3esr.autoescola3esr.domain.agenda.validacao;

import br.com.fiap3esr.autoescola3esr.domain.agenda.DadosAgendamento;
import br.com.fiap3esr.autoescola3esr.domain.agenda.InstrucaoRepository;
import br.com.fiap3esr.autoescola3esr.domain.agenda.ValidacaoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidadorLimiteDiarioAlunoTest {

    @Mock
    private InstrucaoRepository instrucaoRepository;

    @InjectMocks
    private ValidadorLimiteDiarioAluno validador;

    private DadosAgendamento agendamento() {
        return new DadosAgendamento(1L, 1L, null, LocalDateTime.of(2026, 9, 14, 10, 0));
    }

    private void mockInstrucoesNoDia(long quantidade) {
        when(instrucaoRepository.countByAlunoIdAndDataHoraBetweenAndMotivoCancelamentoIsNull(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(quantidade);
    }

    @Test
    void devePermitirQuandoAlunoNaoTemInstrucoesNoDia() {
        mockInstrucoesNoDia(0);
        assertDoesNotThrow(() -> validador.validar(agendamento()));
    }

    @Test
    void devePermitirASegundaInstrucaoDoDia() {
        mockInstrucoesNoDia(1);
        assertDoesNotThrow(() -> validador.validar(agendamento()));
    }

    @Test
    void deveRejeitarATerceiraInstrucaoNoMesmoDia() {
        mockInstrucoesNoDia(2);
        assertThrows(ValidacaoException.class, () -> validador.validar(agendamento()));
    }
}
