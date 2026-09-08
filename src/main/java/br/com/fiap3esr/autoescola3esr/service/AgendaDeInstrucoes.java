package br.com.fiap3esr.autoescola3esr.service;

import br.com.fiap3esr.autoescola3esr.domain.agenda.*;
import br.com.fiap3esr.autoescola3esr.domain.agenda.validacao.ValidadorAgendamento;
import br.com.fiap3esr.autoescola3esr.domain.agenda.validacao.cancelamento.ValidadorCancelamento;
import br.com.fiap3esr.autoescola3esr.domain.aluno.Aluno;
import br.com.fiap3esr.autoescola3esr.domain.aluno.AlunoNotFoundException;
import br.com.fiap3esr.autoescola3esr.domain.aluno.AlunoRepository;
import br.com.fiap3esr.autoescola3esr.domain.instrutor.Instrutor;
import br.com.fiap3esr.autoescola3esr.domain.instrutor.InstrutorNotFoundException;
import br.com.fiap3esr.autoescola3esr.domain.instrutor.InstrutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AgendaDeInstrucoes {
    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private InstrutorRepository instrutorRepository;

    @Autowired
    private InstrucaoRepository repository;

    @Autowired
    private List<ValidadorAgendamento> validadoresAgendamento;

    @Autowired
    private List<ValidadorCancelamento> validadoresCancelamento;

    @Transactional
    public DadosDetalhamentoAgendamento agendar(DadosAgendamento dados) {
        if (!alunoRepository.existsById(dados.idAluno())) {
            throw new AlunoNotFoundException("ID do aluno informado não existe!");
        }
        if (dados.idInstrutor() != null && !instrutorRepository.existsById(dados.idInstrutor())) {
            throw new InstrutorNotFoundException("ID do instrutor informado não existe!");
        }
        //Validações
        validadoresAgendamento.forEach(validador -> validador.validar(dados));

        Aluno aluno = alunoRepository.getReferenceById(dados.idAluno());
        Instrutor instrutor = escolherInstrutor(dados);
        if (instrutor == null) {
            throw new ValidacaoException("Não existe instrutor disponível para a data/hora informada!");
        }
        Instrucao instrucao = new Instrucao(aluno, instrutor, dados.dataHora());
        Instrucao salva = repository.save(instrucao);
        return new DadosDetalhamentoAgendamento(salva);
    }

    @Transactional
    public DadosDetalhamentoAgendamento cancelar(DadosCancelamento dados) {
        Instrucao instrucao = repository.findById(dados.idInstrucao())
                .orElseThrow(() -> new ValidacaoException("ID da instrução informado não existe!"));

        if (instrucao.isCancelada()) {
            throw new ValidacaoException("Esta instrução já foi cancelada!");
        }

        //Validações
        validadoresCancelamento.forEach(validador -> validador.validar(instrucao));

        instrucao.cancelar(dados.motivo());
        return new DadosDetalhamentoAgendamento(instrucao);
    }

    private Instrutor escolherInstrutor(DadosAgendamento dados) {
        if (dados.idInstrutor() != null) {
            return instrutorRepository.getReferenceById(dados.idInstrutor());
        }
        // Instrutor não informado: o sistema sorteia aleatoriamente um instrutor
        // disponível na data/hora. A especialidade é opcional — quando informada,
        // restringe o sorteio a instrutores daquela especialidade.
        return instrutorRepository.escolherInstrutorAleatorioDisponivel(dados.especialidade(), dados.dataHora());
    }
}
