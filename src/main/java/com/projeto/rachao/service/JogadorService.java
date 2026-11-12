
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

    // Injeção de dependências pelo construtor.
    // O Spring fornece automaticamente o Repository e o PasswordEncoder.
    public JogadorService(JogadorRepository jogadorRepository,
            PasswordEncoder passwordEncoder) {
        this.jogadorRepository = jogadorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ============================
    // VALIDAÇÕES DO CADASTRO
    // ============================

    // Verifica se o e-mail possui um formato básico válido.
    private boolean isEmailValido(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    // Verifica se os campos obrigatórios foram preenchidos.
    private void validarCamposObrigatorios(JogadorCadastroDTO dados) {

        if (dados == null) {
            throw new IllegalArgumentException(
                    "Os dados do cadastro são obrigatórios.");
        }

        // trim() remove espaços no começo e no final.
        // isEmpty() verifica se a String ficou vazia.
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

    // ============================
    // VALIDAÇÃO DO CPF
    // ============================

    // Verifica os dois dígitos verificadores do CPF.
    // Esse método será utilizado na atualização do perfil.
    private boolean isCpfValido(String cpf) {

        if (cpf == null) {
            return false;
        }

        cpf = cpf.replaceAll("[^0-9]", "");

        // O CPF precisa ter 11 dígitos e não pode conter
        // todos os números iguais.
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

    // ============================
    // CADASTRAR JOGADOR
    // ============================

    public JogadorDTO cadastrar(JogadorCadastroDTO dto) {

        // 1. Verifica os campos obrigatórios.
        validarCamposObrigatorios(dto);

        // 2. Normaliza o e-mail para evitar diferenças
        // entre letras maiúsculas e minúsculas.
        String email = dto.getEmail().trim().toLowerCase();

        // 3. Valida o formato do e-mail.
        if (!isEmailValido(email)) {
            throw new IllegalArgumentException("E-mail inválido.");
        }

        // 4. Valida o tamanho mínimo da senha.
        if (dto.getSenha().length() < 8) {
            throw new IllegalArgumentException(
                    "A senha deve ter no mínimo 8 caracteres.");
        }

        // 5. Verifica se o jogador é maior de idade.
        validarMaioridade(dto.getDataNascimento());

        // 6. Impede o cadastro de e-mails duplicados.
        if (jogadorRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "E-mail já cadastrado.");
        }

        // 7. Cria uma nova entidade Jogador.
        Jogador jogador = new Jogador();

        jogador.setNome(dto.getNome().trim());
        jogador.setEmail(email);
        jogador.setDataNasc(dto.getDataNascimento());

        // A senha nunca deve ser salva em texto puro.
        // O PasswordEncoder transforma a senha em um hash.
        jogador.setSenha(passwordEncoder.encode(dto.getSenha()));

        // O CPF não é preenchido no cadastro.
        // Será informado posteriormente na tela de perfil.

        // 8. Salva o jogador no banco de dados.
        Jogador salvo = jogadorRepository.save(jogador);

        // 9. Retorna os dados permitidos pelo JogadorDTO.
        return converterParaDTO(salvo);
    }

    // ============================
    // ATUALIZAR PERFIL
    // ============================

    public JogadorDTO atualizarPerfil(Long id, JogadorPerfilDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException(
                    "Os dados do perfil são obrigatórios.");
        }

        // Busca o jogador pelo ID.
        // Se não encontrar, lança uma exceção.
        Jogador jogador = jogadorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Jogador não encontrado."));

        // O CPF é opcional na atualização.
        // Se for informado, será validado.
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

        // Valida o CEP, quando informado.
        if (dto.getCep() != null) {

            String cep = dto.getCep().replaceAll("[^0-9]", "");

            if (cep.length() != 8) {
                throw new IllegalArgumentException(
                        "O CEP deve conter 8 dígitos.");
            }

            jogador.setCep(cep);
        }

        // Atualiza a descrição, quando informada.
        if (dto.getDescricao() != null) {

            if (dto.getDescricao().length() > 500) {
                throw new IllegalArgumentException(
                        "A descrição deve ter no máximo 500 caracteres.");
            }

            jogador.setDescricao(dto.getDescricao());
        }

        // Atualiza a altura, quando informada.
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

    // ============================
    // BUSCAR JOGADOR POR ID
    // ============================

    public JogadorDTO buscarPorId(Long id) {

        Jogador jogador = jogadorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Jogador não encontrado."));

        return converterParaDTO(jogador);
    }

    // ============================
    // CONVERTER ENTIDADE PARA DTO
    // ============================

    // Centraliza a conversão para evitar repetição de código.
    // Também impede que a senha seja retornada na resposta.
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
