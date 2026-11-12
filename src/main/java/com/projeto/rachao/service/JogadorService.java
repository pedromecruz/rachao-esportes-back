
package com.projeto.rachao.service;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projeto.rachao.dto.JogadorCadastroDTO;
import com.projeto.rachao.dto.JogadorDTO;
import com.projeto.rachao.dto.JogadorPerfilDTO;
import com.projeto.rachao.entidades.Jogador;
import com.projeto.rachao.repository.JogadorRepository;

@Service
public class JogadorService {

    private final JogadorRepository jogadorRepository;
    private final PasswordEncoder passwordEncoder;

    // injecao de dependencias construtor
    public JogadorService(JogadorRepository jogadorRepository,
            PasswordEncoder passwordEncoder) {
        this.jogadorRepository = jogadorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // verificacao do e-mail, se tem o formato padrão de email
    private boolean isEmailValido(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    // verifica se os campos estão preenchidos
    private void validarCamposObrigatorios(JogadorCadastroDTO dados) {

        if (dados == null) {
            throw new IllegalArgumentException(
                    "Os dados do cadastro são obrigatórios.");
        }

        // trim() remove espaços no começo e no final // isEmpty() verifica se a String
        // ficou vazia.
        if (dados.getNome() == null || dados.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome é obrigatório.");
        }

        if (dados.getEmail() == null || dados.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("O e-mail é obrigatório.");
        }

        if (dados.getSenha() == null || dados.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("A senha é obrigatória.");
        }

        if (dados.getDataNascimento() == null) {
            throw new IllegalArgumentException(
                    "A data de nascimento é obrigatória.");
        }
    }

    // Verifica se o jogador possui pelo menos 18 anos completos.
    private void validarMaioridade(LocalDate dataNascimento) {

        if (dataNascimento == null) {
            throw new IllegalArgumentException(
                    "A data de nascimento é obrigatória.");
        }

        LocalDate hoje = LocalDate.now();

        // Não permite datas de nascimento futuras.
        if (dataNascimento.isAfter(hoje)) {
            throw new IllegalArgumentException(
                    "A data de nascimento não pode ser futura.");
        }

        // Period calcula a diferença entre duas datas.
        // getYears() retorna os anos completos.
        int idade = Period.between(dataNascimento, hoje).getYears();

        if (idade < 18) {
            throw new IllegalArgumentException(
                    "O jogador deve ter pelo menos 18 anos para se cadastrar.");
        }
    }

   
    // Verifica os dois dígitos verificadores do CPF.
    private boolean isCpfValido(String cpf) {

        if (cpf == null) {
            return false;
        }

        cpf = cpf.replaceAll("[^0-9]", "");

        // O CPF precisa ter 11 dígitos e não pode conter todos os números iguais.
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int soma = 0;
        int peso = 10;

        // Calcula o primeiro dígito verificador.
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * peso--;
        }

        int resto = soma % 11;

        int digito1 = (resto < 2) ? 0 : 11 - resto;

        soma = 0;
        peso = 11;

        // Calcula o segundo dígito verificador.
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * peso--;
        }

        resto = soma % 11;

        int digito2 = (resto < 2) ? 0 : 11 - resto;

        // Compara os dígitos calculados com os informados.
        return digito1 == (cpf.charAt(9) - '0')
                && digito2 == (cpf.charAt(10) - '0');
    }

    // CADASTRAR JOGADOR
    public JogadorDTO cadastrar(JogadorCadastroDTO dto) {

        // verifica os campos obrigatorios
        validarCamposObrigatorios(dto);

        //padroniza o e-mail para nao ter maisculas
        String email = dto.getEmail().trim().toLowerCase();

        //valida formato do email
        if (!isEmailValido(email)) {
            throw new IllegalArgumentException("E-mail inválido.");
        }

        // verifica tamanho minimo da senha
        if (dto.getSenha().length() < 8) {
            throw new IllegalArgumentException(
                    "A senha deve ter no mínimo 8 caracteres.");
        }

       //verifica se é maior de idade
        validarMaioridade(dto.getDataNascimento());

        //impede cadastrar o mesmo email
        if (jogadorRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "E-mail já cadastrado.");
        }

        //criando nova entidade jogador
        Jogador jogador = new Jogador();

        jogador.setNome(dto.getNome().trim());
        jogador.setEmail(email);
        jogador.setDataNasc(dto.getDataNascimento());

    
        // O PasswordEncoder transforma a senha em um hash.
        jogador.setSenha(passwordEncoder.encode(dto.getSenha()));

        // salvar o jogador no banco
        Jogador salvo = jogadorRepository.save(jogador);

       // retorna somente oq é permitido no DTO
        return converterParaDTO(salvo);
    }

    public JogadorDTO atualizarPerfil(Long id, JogadorPerfilDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException(
                    "Os dados do perfil são obrigatórios.");
        }

        // buscando pelo id
        Jogador jogador = jogadorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Jogador não encontrado."));

        // verificacao pra ver se foi inserido cpf
        if (dto.getCpf() != null && !dto.getCpf().isBlank()) {

            // Remove pontos, traços e outros caracteres.
            String cpf = dto.getCpf().replaceAll("[^0-9]", "");

            // Valida os dígitos verificadores.
            if (!isCpfValido(cpf)) {
                throw new IllegalArgumentException("CPF inválido.");
            }

            // Verifica se o CPF já pertence a outro jogador.
            // Permite manter o CPF do próprio usuário.
            if (!cpf.equals(jogador.getCpf())
                    && jogadorRepository.existsByCpf(cpf)) {

                throw new IllegalArgumentException(
                        "CPF já cadastrado.");
            }

            jogador.setCpf(cpf);
        }

        // validacao de cep
        if (dto.getCep() != null) {

            String cep = dto.getCep().replaceAll("[^0-9]", "");

            if (cep.length() != 8) {
                throw new IllegalArgumentException(
                        "O CEP deve conter 8 dígitos.");
            }

            jogador.setCep(cep);
        }

        // atualiza a descricao caso seja informada
        if (dto.getDescricao() != null) {

            if (dto.getDescricao().length() > 500) {
                throw new IllegalArgumentException(
                        "A descrição deve ter no máximo 500 caracteres.");
            }

            jogador.setDescricao(dto.getDescricao());
        }

        // atualiza a altura caso seja informada
        if (dto.getAltura() != null) {

            // Considerando altura em metros.
            if (dto.getAltura() <= 0 || dto.getAltura() > 3) {
                throw new IllegalArgumentException(
                        "Altura inválida.");
            }

            jogador.setAltura(dto.getAltura());
        }

        // Salva as alterações no banco.
        Jogador salvo = jogadorRepository.save(jogador);

        return converterParaDTO(salvo);
    }

    // BUSCAR JOGADOR POR ID
    public JogadorDTO buscarPorId(Long id) {

        Jogador jogador = jogadorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Jogador não encontrado."));

        return converterParaDTO(jogador);
    }

    // metodo para converter em dto, para não precisar repetir no código varias
    // vezes
    private JogadorDTO converterParaDTO(Jogador jogador) {

        JogadorDTO dto = new JogadorDTO();

        dto.setId(jogador.getId());
        dto.setNome(jogador.getNome());
        dto.setEmail(jogador.getEmail());
        dto.setCep(jogador.getCep());
        dto.setDataNascimento(jogador.getDataNasc());
        dto.setDescricao(jogador.getDescricao());
        dto.setAltura(jogador.getAltura());

        return dto;
    }
}
