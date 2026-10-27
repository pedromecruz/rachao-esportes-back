package com.projeto.rachao.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projeto.rachao.dto.JogadorCadastroDTO;
import com.projeto.rachao.dto.JogadorDTO;
import com.projeto.rachao.entidades.Jogador;
import com.projeto.rachao.repository.JogadorRepository;

@Service
public class JogadorService {

    private final JogadorRepository jogadorRepository;
    private final PasswordEncoder passwordEncoder;

    public JogadorService(JogadorRepository jogadorRepository, PasswordEncoder passwordEncoder) {
        this.jogadorRepository = jogadorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public JogadorDTO cadastrar(JogadorCadastroDTO dto) {

        // verificar se tem e-mail duplicado
        if (jogadorRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        Jogador jogador = new Jogador();
        jogador.setNome(dto.getNome());
        jogador.setEmail(dto.getEmail());
        jogador.setCep(dto.getCep());
        jogador.setDataNascimento(dto.getDataNascimento());
        jogador.setDescricao(dto.getDescricao());
        jogador.setSenha(passwordEncoder.encode(dto.getSenha()));
        Jogador salvo = jogadorRepository.save(jogador);

        JogadorDTO resposta = new JogadorDTO();
        resposta.setId(salvo.getId());
        resposta.setNome(salvo.getNome());
        resposta.setEmail(salvo.getEmail());
        resposta.setCep(salvo.getCep());
        resposta.setDataNascimento(salvo.getDataNascimento());
        resposta.setDescricao(salvo.getDescricao());

        return resposta;

    }

}
