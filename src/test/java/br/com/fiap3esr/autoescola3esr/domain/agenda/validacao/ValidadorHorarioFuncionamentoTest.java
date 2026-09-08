package br.com.fiap3esr.autoescola3esr.domain.agenda.validacao;

import br.com.fiap3esr.autoescola3esr.domain.agenda.DadosAgendamento;
import br.com.fiap3esr.autoescola3esr.domain.agenda.ValidacaoException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidadorHorarioFuncionamentoTest {

    private final ValidadorHorarioFuncionamento validador = new ValidadorHorarioFuncionamento();

    private DadosAgendamento agendamentoEm(LocalDateTime dataHora) {
        return new DadosAgendamento(1L, 1L, null, dataHora);
    }

    @Test
    void deveRejeitarAgendamentoNoDomingo() {
        // 13/09/2026 é um domingo
        var dados = agendamentoEm(LocalDateTime.of(2026, 9, 13, 10, 0));
        assertThrows(ValidacaoException.class, () -> validador.validar(dados));
    }

    @Test
    void deveRejeitarAgendamentoAntesDaAbertura() {
        var dados = agendamentoEm(LocalDateTime.of(2026, 9, 14, 5, 0));
        assertThrows(ValidacaoException.class, () -> validador.validar(dados));
    }

    @Test
    void deveRejeitarAgendamentoDepoisDoFechamento() {
        // 21:00 não deixa a instrução de 1h terminar dentro do expediente
        var dados = agendamentoEm(LocalDateTime.of(2026, 9, 14, 21, 0));
        assertThrows(ValidacaoException.class, () -> validador.validar(dados));
    }

    @Test
    void devePermitirAgendamentoEmDiaUtilDentroDoExpediente() {
        var dados = agendamentoEm(LocalDateTime.of(2026, 9, 14, 10, 0)); // segunda
        assertDoesNotThrow(() -> validador.validar(dados));
    }

    @Test
    void devePermitirAgendamentoNoSabado() {
        var dados = agendamentoEm(LocalDateTime.of(2026, 9, 12, 8, 0)); // sábado
        assertDoesNotThrow(() -> validador.validar(dados));
    }
}
