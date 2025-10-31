package com.criptounip.cripto_aps.service;

import org.springframework.stereotype.Service;

@Service
public class CriptoService {
    private static final String VIGENERE = "vigenere";
    private static final String AES = "aes";

    public String criptografar(String texto, String chave, String algoritmo) {
        if(VIGENERE.equalsIgnoreCase(algoritmo)){
            return "Criptografado com Vigenère";
        } else if(AES.equalsIgnoreCase(algoritmo)) {
            return "Criptografado com AES";
        } else {
            throw new IllegalArgumentException("Algoritmo desconhecido.");
        } 
    }

    public String descriptogrfar(String textoCifrado, String chave, String algoritmo) {
        if(VIGENERE.equalsIgnoreCase(algoritmo)){
            return "Descriptografado com Vigenère";
        } else if(AES.equalsIgnoreCase(algoritmo)) {
            return "Descriptografado com AES";
        } else {
            throw new IllegalArgumentException("Algoritmo desconhecido.");
        } 
    }
}
