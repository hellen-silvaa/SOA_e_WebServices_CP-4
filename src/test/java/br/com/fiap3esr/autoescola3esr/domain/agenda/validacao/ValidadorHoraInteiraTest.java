package br.com.fiap3esr.autoescola3esr.domain.agenda.validacao;

import br.com.fiap3esr.autoescola3esr.domain.agenda.DadosAgendamento;
import br.com.fiap3esr.autoescola3esr.domain.agenda.ValidacaoException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidadorHoraInteiraTest {

    private final ValidadorHoraInteira validador = new ValidadorHoraInteira();

    @Test
    void deveRejeitarHorarioQuebrado() {
        var dados = new DadosAgendamento(1L, 1L, null, LocalDateTime.of(2026, 9, 14, 9, 30));
        assertThrows(ValidacaoException.class, () -> validador.validar(dados));
    }

    @Test
    void devePermitirHoraInteira() {
        var dados = new DadosAgendamento(1L, 1L, null, LocalDateTime.of(2026, 9, 14, 9, 0));
        assertDoesNotThrow(() -> validador.validar(dados));
    }
}
