package com.criptounip.cripto_aps.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.criptounip.cripto_aps.dto.CriptoRequest;
import com.criptounip.cripto_aps.dto.CriptoResponse;
import com.criptounip.cripto_aps.service.CriptoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/cripto")
public class CriptoController {

    @Autowired
    private CriptoService criptoService;

    @PostMapping("/criptografar")
    public CriptoResponse criptografar(@RequestBody CriptoRequest request) {
        String resultado = criptoService.criptografar(request.getTexto(), request.getChave(), request.getAlgoritmo());

        CriptoResponse response = new CriptoResponse();
        response.setResultado(resultado);
        return response;
    }

    @PostMapping("/descriptografar")
    public CriptoResponse descriptografar(@RequestBody CriptoRequest request) {
        String resultado = criptoService.descriptografar(request.getTexto(), request.getChave(), request.getAlgoritmo());
        
        CriptoResponse response = new CriptoResponse();
        response.setResultado(resultado);
        return response;
    }
}
