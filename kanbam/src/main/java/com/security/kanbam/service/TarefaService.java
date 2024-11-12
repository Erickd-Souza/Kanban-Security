package com.security.kanbam.service;

import com.security.kanbam.model.Status;
import com.security.kanbam.model.Tarefa;
import com.security.kanbam.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TarefaService {
    @Autowired
    private TarefaRepository tarefaRepository;
    public List<Tarefa> listaTodos(){
        return tarefaRepository.findAll();
    }
    public Tarefa salvarTarefa(Tarefa tarefa){
        return tarefaRepository.save(tarefa);
    }
    public Tarefa atualizarTarefa(Integer id, Tarefa novaTarefa){
        Tarefa tarefaAtual = tarefaRepository.findById(id).orElseThrow(() -> new RuntimeException("A Tarefa não foi encontrada!!!"));
        tarefaAtual.setTitulo(novaTarefa.getTitulo());
        tarefaAtual.setDescricao(novaTarefa.getDescricao());
        tarefaAtual.setPrioridade(novaTarefa.getPrioridade());
        tarefaAtual.setDataLimite(novaTarefa.getDataLimite());
        return tarefaRepository.save(tarefaAtual);
    }
    public void excluirTarefa(Integer id){
        tarefaRepository.deleteById(id);
    }
    public Tarefa moverTarefa(Integer id){
        Tarefa tarefa = tarefaRepository.findById(id).orElseThrow(() -> new RuntimeException("A Tarefa não foi encontrada!!!"));
        if(tarefa.getStatus() == Status.A_FAZER) {
            tarefa.setStatus(Status.EM_PROGRESSO);
        }else if(tarefa.getStatus() == Status.EM_PROGRESSO){
            tarefa.setStatus(Status.CONCLUIDO);
        }
        return tarefaRepository.save(tarefa);
    }
    public List<Tarefa> listarPrioridade(){
        return tarefaRepository.findByOrderByPrioridadeAsc();
    }
    public List<Tarefa> listarAtrasados(){
        return tarefaRepository.findAll().stream().filter(tarefa -> tarefa.getDataLimite() != null && tarefa.getDataLimite().isBefore(LocalDate.now()) && tarefa.getStatus() != Status.CONCLUIDO).collect(Collectors.toList());
    }
}
