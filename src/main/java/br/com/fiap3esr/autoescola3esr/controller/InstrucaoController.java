package br.com.fiap3esr.autoescola3esr.controller;

import br.com.fiap3esr.autoescola3esr.domain.agenda.DadosAgendamento;
import br.com.fiap3esr.autoescola3esr.domain.agenda.DadosCancelamento;
import br.com.fiap3esr.autoescola3esr.domain.agenda.DadosDetalhamentoAgendamento;
import br.com.fiap3esr.autoescola3esr.service.AgendaDeInstrucoes;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/instrucoes")
public class InstrucaoController {
    @Autowired
    private AgendaDeInstrucoes agenda;

    @PostMapping
    public ResponseEntity<DadosDetalhamentoAgendamento> agendarInstrucao(
            @RequestBody @Valid DadosAgendamento dados) {
        return ResponseEntity.ok(agenda.agendar(dados));
    }

    @DeleteMapping
    public ResponseEntity<DadosDetalhamentoAgendamento> cancelarInstrucao(
            @RequestBody @Valid DadosCancelamento dados) {
        return ResponseEntity.ok(agenda.cancelar(dados));
    }
}
