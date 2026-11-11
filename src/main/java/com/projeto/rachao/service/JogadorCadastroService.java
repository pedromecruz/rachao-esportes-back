package com.projeto.rachao.service;

import com.projeto.rachao.dto.JogadorCadastroDTO;
import com.projeto.rachao.entidades.Jogador;
import com.projeto.rachao.repository.JogadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JogadorCadastroService {

    private final JogadorRepository jogadorRepository;

    public JogadorCadastroService(
            JogadorRepository jogadorRepository) {

        this.jogadorRepository = jogadorRepository;
    }

    // validar se os campos obrigatórios estão preenchidos
    private void validarCamposObrigatorios(JogadorCadastroDTO dados) {

        // null vai verificar se está sem nenhum valor
        //o trim com o isEmpty vai validar se o usuário cadastrou apenas espaços em brancos
    if (dados.getNome() == null || dados.getNome().trim().isEmpty()) 
        throw new IllegalArgumentException("O Nome é obrigatório.");

    if (dados.getEmail() == null || dados.getEmail().trim().isEmpty()) 
        throw new IllegalArgumentException("O E-mail é obrigatório.");

    if (dados.getSenha() == null || dados.getSenha().trim().isEmpty()) 
        throw new IllegalArgumentException("A Senha é obrigatória.");

    if (dados.getCpf() == null || dados.getCpf().trim().isEmpty())
        throw new IllegalArgumentException("O CPF é obrigatório.");
    }   

    // valida se o e-mail é valido
    private boolean isEmailValido(String email) {
            return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    // valida se o cpf é valido
    private boolean isCpfValido(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) 
            return false;

        try {
            int soma = 0, peso = 10;
            
            // multiplica os numeros do cpf em ordem decrescente
            for (int i = 0; i < 9; i++) 
                soma += (cpf.charAt(i) - '0') * peso--;
            
            // divide e pega o resto
            int resto = soma % 11;
            // resto é igual a 0 ou 1? se sim, o número deverá ser 0
            // caso contrário, o digito deverá ser o 11 - resto, o +'0' é um macete do java para converter numeros em char
            char digito1 = (resto == 0 || resto == 1) ? '0' : (char) ((11 - resto) + '0');

            soma = 0; 
            peso = 11;
            
            for (int i = 0; i < 10; i++) 
                soma += (cpf.charAt(i) - '0') * peso--;
            
            resto = soma % 11;
            char digito2 = (resto == 0 || resto == 1) ? '0' : (char) ((11 - resto) + '0');

            // se as duas condições forem true (verdadeiras), pode criar o jogador
            return (digito1 == cpf.charAt(9)) && (digito2 == cpf.charAt(10));

        } catch (Exception e) {
            return false;
        }
    }


    @Transactional
    public Jogador cadastrarJogador(JogadorCadastroDTO dados) {

        validarCamposObrigatorios(dados);

        if (!isEmailValido(dados.getEmail())) 
            throw new IllegalArgumentException("O formato do e-mail é inválido.");

        if (dados.getSenha().length() < 8) 
            throw new IllegalArgumentException("A senha deve possuir no mínimo 8 caracteres.");

        if (!isCpfValido(dados.getCpf())) 
            throw new IllegalArgumentException("O CPF informado é inválido.");

        if (jogadorRepository.existsByEmail(dados.getEmail())) 
            throw new IllegalArgumentException("Este e-mail já está cadastrado no sistema.");

        if (jogadorRepository.existsByCpf(dados.getCpf()))
            throw new IllegalArgumentException("Este CPF já está cadastrado no sistema.");

        // se passou de tudo, cria o jogador caro
        Jogador jogador = new Jogador();

        jogador.setNome(dados.getNome());
        jogador.setEmail(dados.getEmail());
        jogador.setSenha(dados.getSenha());
        jogador.setCpf(dados.getCpf());

        return jogadorRepository.save(jogador);
    }
}