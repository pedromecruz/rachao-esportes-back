package com.projeto.rachao.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.projeto.rachao.dto.JogadorCadastroDTO;
import com.projeto.rachao.dto.JogadorDTO;
import com.projeto.rachao.dto.JogadorPerfilDTO;

import com.projeto.rachao.entidades.Jogador;

import com.projeto.rachao.repository.JogadorRepository;
import com.projeto.rachao.repository.PartidaRepository;
import com.projeto.rachao.repository.InscricaoRepository;
import com.projeto.rachao.repository.DesempenhoRepository;

@Service
public class JogadorService {

    private final JogadorRepository jogadorRepository;
    private final PasswordEncoder passwordEncoder;
    private final PartidaRepository partidaRepository;
    private final InscricaoRepository inscricaoRepository;
    private final DesempenhoRepository desempenhoRepository;

    // INJEÇÃO DE DEPENDÊNCIAS
    public JogadorService(
            JogadorRepository jogadorRepository,
            PasswordEncoder passwordEncoder,
            PartidaRepository partidaRepository,
            InscricaoRepository inscricaoRepository,
            DesempenhoRepository desempenhoRepository) {

        this.jogadorRepository = jogadorRepository;
        this.passwordEncoder = passwordEncoder;
        this.partidaRepository = partidaRepository;
        this.inscricaoRepository = inscricaoRepository;
        this.desempenhoRepository = desempenhoRepository;
    }

    // VALIDAÇÃO DE E-MAIL
    private boolean isEmailValido(String email) {

        return email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        );
    }

    // VALIDAÇÃO DOS CAMPOS OBRIGATÓRIOS
    private void validarCamposObrigatorios(JogadorCadastroDTO dados) {

        if (dados == null) {
            throw new IllegalArgumentException(
                    "Os dados do cadastro são obrigatórios.");
        }

        if (dados.getNome() == null ||
                dados.getNome().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "O nome é obrigatório.");
        }

        if (dados.getEmail() == null ||
                dados.getEmail().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "O e-mail é obrigatório.");
        }

        if (dados.getSenha() == null ||
                dados.getSenha().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "A senha é obrigatória.");
        }

        if (dados.getDataNascimento() == null) {

            throw new IllegalArgumentException(
                    "A data de nascimento é obrigatória.");
        }
    }

    // VALIDAÇÃO DE MAIORIDADE
    private void validarMaioridade(LocalDate dataNascimento) {

        if (dataNascimento == null) {
            throw new IllegalArgumentException(
                    "A data de nascimento é obrigatória.");
        }

        LocalDate hoje = LocalDate.now();

        // Impede datas futuras
        if (dataNascimento.isAfter(hoje)) {

            throw new IllegalArgumentException(
                    "A data de nascimento não pode ser futura.");
        }

        // Calcula a idade
        int idade = Period.between(
                dataNascimento, hoje).getYears();

        if (idade < 18) {

            throw new IllegalArgumentException(
                    "O jogador deve ter pelo menos 18 anos para se cadastrar.");
        }
    }

    // VALIDAÇÃO DE CPF
    private boolean isCpfValido(String cpf) {

        if (cpf == null) {
            return false;
        }

        // Remove caracteres especiais
        cpf = cpf.replaceAll("[^0-9]", "");

        // Verifica quantidade de dígitos e números repetidos
        if (cpf.length() != 11 ||
                cpf.matches("(\\d)\\1{10}")) {

            return false;
        }

        int soma = 0;
        int peso = 10;

        // Primeiro dígito verificador
        for (int i = 0; i < 9; i++) {

            soma += (cpf.charAt(i) - '0') * peso--;
        }

        int resto = soma % 11;

        int digito1 = (resto < 2) ? 0 : 11 - resto;

        soma = 0;
        peso = 11;

        // Segundo dígito verificador
        for (int i = 0; i < 10; i++) {

            soma += (cpf.charAt(i) - '0') * peso--;
        }

        resto = soma % 11;

        int digito2 = (resto < 2) ? 0 : 11 - resto;

        // Compara os dígitos calculados
        return digito1 == (cpf.charAt(9) - '0')
                && digito2 == (cpf.charAt(10) - '0');
    }

    // CADASTRAR JOGADOR
    public JogadorDTO cadastrar(JogadorCadastroDTO dto) {

        // Verifica os campos obrigatórios
        validarCamposObrigatorios(dto);

        // Padroniza o e-mail
        String email = dto.getEmail().trim().toLowerCase();

        // Valida o formato do e-mail
        if (!isEmailValido(email)) {

            throw new IllegalArgumentException(
                    "E-mail inválido.");
        }

        // Valida o tamanho da senha
        if (dto.getSenha().length() < 8) {

            throw new IllegalArgumentException(
                    "A senha deve ter no mínimo 8 caracteres.");
        }

        // Verifica se o jogador é maior de idade
        validarMaioridade(dto.getDataNascimento());

        // Impede e-mails duplicados
        if (jogadorRepository.existsByEmail(email)) {

            throw new IllegalArgumentException(
                    "E-mail já cadastrado.");
        }

        // Cria o jogador
        Jogador jogador = new Jogador();

        jogador.setNome(dto.getNome().trim());
        jogador.setEmail(email);
        jogador.setDataNasc(dto.getDataNascimento());

        // Criptografa a senha
        jogador.setSenha(
                passwordEncoder.encode(dto.getSenha())
        );

        // Salva no banco de dados
        Jogador salvo = jogadorRepository.save(jogador);

        return converterParaDTO(salvo);
    }

    // ATUALIZAR PERFIL
    public JogadorDTO atualizarPerfil(
            Long id,
            JogadorPerfilDTO dto) {

        if (dto == null) {

            throw new IllegalArgumentException(
                    "Os dados do perfil são obrigatórios.");
        }

        // Busca o jogador
        Jogador jogador = jogadorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Jogador não encontrado."));

        // VALIDAÇÃO DE CPF
        if (dto.getCpf() != null &&
                !dto.getCpf().isBlank()) {

            String cpf = dto.getCpf()
                    .replaceAll("[^0-9]", "");

            if (!isCpfValido(cpf)) {

                throw new IllegalArgumentException(
                        "CPF inválido.");
            }

            // Impede CPF duplicado
            if (!cpf.equals(jogador.getCpf())
                    && jogadorRepository.existsByCpf(cpf)) {

                throw new IllegalArgumentException(
                        "CPF já cadastrado.");
            }

            jogador.setCpf(cpf);
        }

        // VALIDAÇÃO DE CEP
        if (dto.getCep() != null) {

            String cep = dto.getCep()
                    .replaceAll("[^0-9]", "");

            if (cep.length() != 8) {

                throw new IllegalArgumentException(
                        "O CEP deve conter 8 dígitos.");
            }

            jogador.setCep(cep);
        }

        // VALIDAÇÃO DE DESCRIÇÃO
        if (dto.getDescricao() != null) {

            if (dto.getDescricao().length() > 500) {

                throw new IllegalArgumentException(
                        "A descrição deve ter no máximo 500 caracteres.");
            }

            jogador.setDescricao(dto.getDescricao());
        }

        // VALIDAÇÃO DE ALTURA
        if (dto.getAltura() != null) {

            if (!Double.isFinite(dto.getAltura())
                    || dto.getAltura() <= 0
                    || dto.getAltura() > 3) {

                throw new IllegalArgumentException(
                        "Altura inválida.");
            }

            jogador.setAltura(dto.getAltura());
        }

        // Salva as alterações
        Jogador salvo = jogadorRepository.save(jogador);

        return converterParaDTO(salvo);
    }

    // BUSCAR JOGADOR POR ID
    public JogadorDTO buscarPorId(Long id) {

        Jogador jogador = jogadorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Jogador não encontrado."));

        return converterParaDTO(jogador);
    }

    // LISTAR TODOS OS JOGADORES
    public List<JogadorDTO> listar() {

        return jogadorRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    // EXCLUIR JOGADOR
    public void excluir(Long id) {

        // Busca o jogador
        Jogador jogador = jogadorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Jogador não encontrado."));

        // Verifica se existem registros associados
        if (partidaRepository.existsByJogadorHostId(id)
                || inscricaoRepository.existsByJogadorId(id)
                || desempenhoRepository.existsByJogadorId(id)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "O jogador possui partidas ou registros associados e não pode ser excluído.");
        }

        // Exclui o jogador
        jogadorRepository.delete(jogador);
    }

    // CONVERTER ENTIDADE PARA DTO
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