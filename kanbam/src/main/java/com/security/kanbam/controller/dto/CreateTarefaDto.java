package com.security.kanbam.controller.dto;

import com.security.kanbam.model.Prioridade;
import com.security.kanbam.model.Status;

import java.time.LocalDate;

public record CreateTarefaDto(
        String titulo,
        String descricao,
        LocalDate dataLimite,
        String userId
) {
}
