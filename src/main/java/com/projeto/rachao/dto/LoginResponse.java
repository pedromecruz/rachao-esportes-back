package com.projeto.rachao.dto;
public record LoginResponse(String token, Long jogadorId, String nome, long expiraEmSegundos) {}
