package br.com.fiap3esr.autoescola3esr.domain.agenda;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface InstrucaoRepository extends JpaRepository<Instrucao, Long> {
    boolean existsByInstrutorIdAndDataHoraAndMotivoCancelamentoIsNull(Long idInstrutor, LocalDateTime dataHora);

    long countByAlunoIdAndDataHoraBetweenAndMotivoCancelamentoIsNull(
            Long idAluno, LocalDateTime inicioDoDia, LocalDateTime fimDoDia);
}
