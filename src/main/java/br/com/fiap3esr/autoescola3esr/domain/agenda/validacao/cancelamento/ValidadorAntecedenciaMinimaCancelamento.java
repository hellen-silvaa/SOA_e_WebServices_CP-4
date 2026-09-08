package br.com.fiap3esr.autoescola3esr.domain.agenda.validacao.cancelamento;

import br.com.fiap3esr.autoescola3esr.domain.agenda.Instrucao;
import br.com.fiap3esr.autoescola3esr.domain.agenda.ValidacaoException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Uma instrução somente poderá ser cancelada com antecedência mínima de 24 horas.
 */
@Component
public class ValidadorAntecedenciaMinimaCancelamento implements ValidadorCancelamento {
    @Override
    public void validar(Instrucao instrucao) {
        LocalDateTime agora = LocalDateTime.now();
        long horasDeAntecedencia = Duration.between(agora, instrucao.getDataHora()).toHours();

        if (horasDeAntecedencia < 24) {
            throw new ValidacaoException(
                    "A instrução só pode ser cancelada com antecedência mínima de 24 horas!");
        }
    }
}
