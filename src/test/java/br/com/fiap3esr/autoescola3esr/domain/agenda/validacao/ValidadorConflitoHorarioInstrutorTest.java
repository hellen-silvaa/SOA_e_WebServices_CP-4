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
class ValidadorConflitoHorarioInstrutorTest {

    @Mock
    private InstrucaoRepository repository;

    @InjectMocks
    private ValidadorConflitoHorarioInstrutor validador;

    private final DadosAgendamento dados =
            new DadosAgendamento(1L, 7L, null, LocalDateTime.of(2026, 9, 14, 10, 0));

    @Test
    void deveRejeitarQuandoInstrutorJaTemInstrucaoNaMesmaDataHora() {
        when(repository.existsByInstrutorIdAndDataHoraAndMotivoCancelamentoIsNull(eq(7L), any(LocalDateTime.class)))
                .thenReturn(true);
        assertThrows(ValidacaoException.class, () -> validador.validar(dados));
    }

    @Test
    void devePermitirQuandoInstrutorEstaLivreNaDataHora() {
        when(repository.existsByInstrutorIdAndDataHoraAndMotivoCancelamentoIsNull(eq(7L), any(LocalDateTime.class)))
                .thenReturn(false);
        assertDoesNotThrow(() -> validador.validar(dados));
    }
}
