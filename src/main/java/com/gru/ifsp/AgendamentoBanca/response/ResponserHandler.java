package com.gru.ifsp.AgendamentoBanca.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponserHandler{

    public static ResponseEntity<Object> generateResponse(String messagem, HttpStatus status, Object responseObj){
        Map<String, Object> map = generateMapTemplateResponse(messagem, status, responseObj);

        return new ResponseEntity<Object>(map, status);
    }

    public static Map<String, Object> generateMapTemplateResponse(String mensagem, HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mensagem", mensagem);
        map.put("status", status.value());
        map.put("data", responseObj);

        return map;
    }

}
