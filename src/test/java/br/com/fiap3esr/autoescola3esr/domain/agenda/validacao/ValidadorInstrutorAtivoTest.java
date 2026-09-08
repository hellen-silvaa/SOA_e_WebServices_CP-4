package br.com.fiap3esr.autoescola3esr.domain.agenda.validacao;

import br.com.fiap3esr.autoescola3esr.domain.agenda.DadosAgendamento;
import br.com.fiap3esr.autoescola3esr.domain.agenda.ValidacaoException;
import br.com.fiap3esr.autoescola3esr.domain.instrutor.InstrutorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidadorInstrutorAtivoTest {

    @Mock
    private InstrutorRepository instrutorRepository;

    @InjectMocks
    private ValidadorInstrutorAtivo validador;

    private final DadosAgendamento dados =
            new DadosAgendamento(1L, 7L, null, LocalDateTime.of(2026, 9, 14, 10, 0));

    @Test
    void deveRejeitarAgendamentoComInstrutorInativo() {
        when(instrutorRepository.existsByIdAndAtivoFalse(7L)).thenReturn(true);
        assertThrows(ValidacaoException.class, () -> validador.validar(dados));
    }

    @Test
    void devePermitirAgendamentoComInstrutorAtivo() {
        when(instrutorRepository.existsByIdAndAtivoFalse(7L)).thenReturn(false);
        assertDoesNotThrow(() -> validador.validar(dados));
    }

    @Test
    void deveIgnorarQuandoInstrutorNaoFoiInformado() {
        DadosAgendamento semInstrutor =
                new DadosAgendamento(1L, null, null, LocalDateTime.of(2026, 9, 14, 10, 0));
        assertDoesNotThrow(() -> validador.validar(semInstrutor));
        verifyNoInteractions(instrutorRepository);
    }
}
