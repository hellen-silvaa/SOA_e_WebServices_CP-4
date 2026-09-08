package br.com.fiap3esr.autoescola3esr.domain.agenda.validacao;

import br.com.fiap3esr.autoescola3esr.domain.agenda.DadosAgendamento;
import br.com.fiap3esr.autoescola3esr.domain.agenda.ValidacaoException;
import br.com.fiap3esr.autoescola3esr.domain.aluno.AlunoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidadorAlunoAtivoTest {

    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private ValidadorAlunoAtivo validador;

    private final DadosAgendamento dados =
            new DadosAgendamento(1L, 1L, null, LocalDateTime.of(2026, 9, 14, 10, 0));

    @Test
    void deveRejeitarAgendamentoParaAlunoInativo() {
        when(alunoRepository.existsByIdAndAtivoFalse(1L)).thenReturn(true);
        assertThrows(ValidacaoException.class, () -> validador.validar(dados));
    }

    @Test
    void devePermitirAgendamentoParaAlunoAtivo() {
        when(alunoRepository.existsByIdAndAtivoFalse(1L)).thenReturn(false);
        assertDoesNotThrow(() -> validador.validar(dados));
    }
}
