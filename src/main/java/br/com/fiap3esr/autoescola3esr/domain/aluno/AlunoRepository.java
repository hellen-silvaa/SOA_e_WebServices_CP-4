package br.com.fiap3esr.autoescola3esr.domain.aluno;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    boolean existsByIdAndAtivoFalse(Long id);

    Page<Aluno> findAllByAtivoTrue(Pageable paginacao);
}
