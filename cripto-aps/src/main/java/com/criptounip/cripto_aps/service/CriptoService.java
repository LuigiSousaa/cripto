package com.criptounip.cripto_aps.service;

import org.springframework.stereotype.Service;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Service
public class CriptoService {
    private static final String VIGENERE = "vigenere";
    private static final String AES = "aes";
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    public String criptografar(String texto, String chave, String algoritmo) {
        if (VIGENERE.equalsIgnoreCase(algoritmo)) {
            return criptografarVigenere(texto, chave);
        } else if (AES.equalsIgnoreCase(algoritmo)) {
            try {
                return criptografarAES(texto, chave);
            } catch (Exception e) {
                return "Erro ao criptografar com AES: " + e.getMessage();
            }
        } else {
            throw new IllegalArgumentException("Algoritmo desconhecido.");
        }
    }

    public String descriptografar(String textoCifrado, String chave, String algoritmo) {
        if (VIGENERE.equalsIgnoreCase(algoritmo)) {
            return descriptografarVigenere(textoCifrado, chave);
        } else if (AES.equalsIgnoreCase(algoritmo)) {
            try {
                return descriptografarAES(textoCifrado, chave);
            } catch (Exception e) {
                return "Erro ao descriptografar com AES: " + e.getMessage();
            }
        } else {
            throw new IllegalArgumentException("Algoritmo desconhecido.");
        }
    }

    private String criptografarAES(String texto, String chave) throws Exception {
        SecretKeySpec secretKey = createSecretKey(chave);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        byte[] ivBytes = new byte[16];
        new SecureRandom().nextBytes(ivBytes);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

        byte[] textoCifradoBytes = cipher.doFinal(texto.getBytes(StandardCharsets.UTF_8));

        byte[] resultadoFinal = new byte[ivBytes.length + textoCifradoBytes.length];
        System.arraycopy(ivBytes, 0, resultadoFinal, 0, ivBytes.length);
        System.arraycopy(textoCifradoBytes, 0, resultadoFinal, ivBytes.length, textoCifradoBytes.length);

        return Base64.getEncoder().encodeToString(resultadoFinal);
    }

    private String descriptografarAES(String textoCifradoBase64, String chave) throws Exception {
        byte[] textoCifradoComIv = Base64.getDecoder().decode(textoCifradoBase64);

        byte[] ivBytes = Arrays.copyOfRange(textoCifradoComIv, 0, 16);
        byte[] textoCifradoBytes = Arrays.copyOfRange(textoCifradoComIv, 16, textoCifradoComIv.length);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        SecretKeySpec secretKey = createSecretKey(chave);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

        byte[] textoOriginalBytes = cipher.doFinal(textoCifradoBytes);

        return new String(textoOriginalBytes, StandardCharsets.UTF_8);
    }

    private SecretKeySpec createSecretKey(String chave) throws Exception {
        byte[] keyBytes = chave.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes16 = Arrays.copyOf(keyBytes, 16);
        return new SecretKeySpec(keyBytes16, ALGORITHM);
    }

    private String criptografarVigenere(String texto, String chave) {
        StringBuilder resultado = new StringBuilder();
        chave = chave.toUpperCase();
        int j = 0;

        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);

            if (Character.isLetter(c)) {
                int deslocamento = chave.charAt(j % chave.length()) - 'A';
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                int letraPos = c - base;
                int novaPos = (letraPos + deslocamento) % 26;
                char novaLetra = (char) (base + novaPos);
                resultado.append(novaLetra);
                j++;
            } else {
                resultado.append(c);
            }
        }
        return resultado.toString();
    }

    private String descriptografarVigenere(String textoCifrado, String chave) {
        StringBuilder resultado = new StringBuilder();
        chave = chave.toUpperCase();
        int j = 0;

        for (int i = 0; i < textoCifrado.length(); i++) {
            char c = textoCifrado.charAt(i);

            if (Character.isLetter(c)) {
                int deslocamento = chave.charAt(j % chave.length()) - 'A';
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                int letraPos = c - base;
                int novaPos = (letraPos - deslocamento + 26) % 26;
                char novaLetra = (char) (base + novaPos);
                resultado.append(novaLetra);
                j++;
            } else {
                resultado.append(c);
            }
        }
        return resultado.toString();
    }
}