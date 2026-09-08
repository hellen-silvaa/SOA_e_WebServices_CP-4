package br.com.fiap3esr.autoescola3esr.domain.agenda.validacao;

import br.com.fiap3esr.autoescola3esr.domain.agenda.DadosAgendamento;
import br.com.fiap3esr.autoescola3esr.domain.agenda.ValidacaoException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidadorHorarioAntecedenciaTest {

    private final ValidadorHorarioAntecedencia validador = new ValidadorHorarioAntecedencia();

    @Test
    void deveRejeitarAgendamentoComMenosDe30MinutosDeAntecedencia() {
        var dados = new DadosAgendamento(1L, 1L, null, LocalDateTime.now().plusMinutes(10));
        assertThrows(ValidacaoException.class, () -> validador.validar(dados));
    }

    @Test
    void devePermitirAgendamentoComMaisDe30MinutosDeAntecedencia() {
        var dados = new DadosAgendamento(1L, 1L, null, LocalDateTime.now().plusHours(2));
        assertDoesNotThrow(() -> validador.validar(dados));
    }
}
