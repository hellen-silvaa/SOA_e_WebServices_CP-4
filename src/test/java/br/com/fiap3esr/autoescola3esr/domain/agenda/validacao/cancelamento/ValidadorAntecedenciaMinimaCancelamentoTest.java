package br.com.fiap3esr.autoescola3esr.domain.agenda.validacao.cancelamento;

import br.com.fiap3esr.autoescola3esr.domain.agenda.Instrucao;
import br.com.fiap3esr.autoescola3esr.domain.agenda.ValidacaoException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidadorAntecedenciaMinimaCancelamentoTest {

    private final ValidadorAntecedenciaMinimaCancelamento validador =
            new ValidadorAntecedenciaMinimaCancelamento();

    private Instrucao instrucaoEm(LocalDateTime dataHora) {
        return new Instrucao(null, null, dataHora);
    }

    @Test
    void deveRejeitarCancelamentoComMenosDe24HorasDeAntecedencia() {
        var instrucao = instrucaoEm(LocalDateTime.now().plusHours(10));
        assertThrows(ValidacaoException.class, () -> validador.validar(instrucao));
    }

    @Test
    void devePermitirCancelamentoComMaisDe24HorasDeAntecedencia() {
        var instrucao = instrucaoEm(LocalDateTime.now().plusHours(30));
        assertDoesNotThrow(() -> validador.validar(instrucao));
    }
}
