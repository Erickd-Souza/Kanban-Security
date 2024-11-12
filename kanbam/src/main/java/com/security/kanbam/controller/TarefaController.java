package com.security.kanbam.controller;

import com.security.kanbam.controller.dto.CreateTarefaDto;
import com.security.kanbam.model.Role;
import com.security.kanbam.model.Tarefa;
import com.security.kanbam.repository.TarefaRepository;
import com.security.kanbam.repository.UserRepository;
import com.security.kanbam.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/tarefas")
public class TarefaController {
    private final TarefaRepository tarefaRepository;
    private final UserRepository userRepository;

    public TarefaController(TarefaRepository tarefaRepository, UserRepository userRepository) {
        this.tarefaRepository = tarefaRepository;
        this.userRepository = userRepository;
    }

    @Autowired
    private TarefaService tarefaService;

    @GetMapping
    public List<Tarefa> raiz(){
        return tarefaService.listaTodos();
    }


    @PostMapping
    public ResponseEntity<Void> criarTarefa(@RequestBody CreateTarefaDto dto,
                                            JwtAuthenticationToken token){
        UUID userId = UUID.fromString(token.getName());
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        var tarefa = new Tarefa();
        tarefa.setTitulo(dto.titulo());
        tarefa.setDescricao(dto.descricao());
        tarefa.setDataLimite(dto.dataLimite());
        tarefa.setUser(user);
        tarefaService.salvarTarefa(tarefa);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public Tarefa atualizarTarefa(@PathVariable Integer id, @RequestBody Tarefa tarefa) {
        return tarefaService.atualizarTarefa(id, tarefa);
    }
    @PutMapping("/{id}/move")
    public Tarefa moverTarefa(@PathVariable Integer id) {
        return tarefaService.moverTarefa(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarefa(@PathVariable("id") Integer tarefaId,
                                            JwtAuthenticationToken token){
        var user = userRepository.findById(UUID.fromString(token.getName()))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuário não encontrado"));

        var tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Tarefa não encontrada"));
        var isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));

        if(isAdmin || tarefa.getUser().getUserId().equals(user.getUserId())) {
            tarefaService.excluirTarefa(tarefaId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

  /*  @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarefa(@PathVariable Integer id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));
        tarefaService.excluirTarefa(id);
        return ResponseEntity.noContent().build();  // Retorna 204 No Content, indicando sucesso
    }*/


    @GetMapping("/atrasados")
    public List<Tarefa> listarAtrasadas() {
        return tarefaService.listarAtrasados();
    }
}
