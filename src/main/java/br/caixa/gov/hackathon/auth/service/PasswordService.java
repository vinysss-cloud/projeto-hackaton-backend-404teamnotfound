package br.caixa.gov.hackathon.auth.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Serviço central de senhas.
 *
 * Etapa 20:
 * - novo padrão: BCrypt com fator de custo 12;
 * - compatibilidade temporária com hashes legados SHA-256 + salt;
 * - o campo senha_salt continua preenchido para compatibilidade com a tabela atual.
 */
@ApplicationScoped
public class PasswordService {

    private static final int BCRYPT_COST = 12;
    private static final String BCRYPT_MARKER = "BCRYPT";

    public String gerarSalt() {
        return BCRYPT_MARKER;
    }

    public String gerarHash(String senha, String salt) {
        validarSenhaObrigatoria(senha);
        return BCrypt.hashpw(senha, BCrypt.gensalt(BCRYPT_COST));
    }

    public boolean senhaConfere(String senhaInformada, String salt, String hashSalvo) {
        if (senhaInformada == null || senhaInformada.isBlank() || hashSalvo == null || hashSalvo.isBlank()) {
            return false;
        }

        if (ehHashBCrypt(hashSalvo)) {
            return BCrypt.checkpw(senhaInformada, hashSalvo);
        }

        return senhaConfereLegadoSha256(senhaInformada, salt, hashSalvo);
    }

    public boolean deveAtualizarParaBCrypt(String hashSalvo) {
        return hashSalvo != null && !ehHashBCrypt(hashSalvo);
    }

    private boolean ehHashBCrypt(String hashSalvo) {
        return hashSalvo.startsWith("$2a$") || hashSalvo.startsWith("$2b$") || hashSalvo.startsWith("$2y$");
    }

    private boolean senhaConfereLegadoSha256(String senhaInformada, String salt, String hashSalvo) {
        if (salt == null || salt.isBlank()) {
            return false;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((salt + ":" + senhaInformada).getBytes(StandardCharsets.UTF_8));
            String hashCalculado = HexFormat.of().formatHex(hash);
            return hashCalculado.equals(hashSalvo);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algoritmo SHA-256 indisponível para validação legada.", e);
        }
    }

    private void validarSenhaObrigatoria(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Senha é obrigatória.");
        }
    }
}
