package com.gru.ifsp.AgendamentoBanca.advices;

import com.gru.ifsp.AgendamentoBanca.response.ResponserHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.util.HashMap;
import java.util.Map;

import static com.gru.ifsp.AgendamentoBanca.response.ResponserHandler.generateMapTemplateResponse;

@RestControllerAdvice
public class AgendamentoBancaAdvice {



    @ExceptionHandler()
    public ResponseEntity tratarRestricoesBean(MethodArgumentNotValidException e){
        Map<String, String> erros = new HashMap<>();

        for (int indice = 0; indice < e.getBindingResult().getAllErrors().size(); indice++){

            String fieldName =  ((FieldError) e.getBindingResult().getAllErrors().get(indice)).getField();
            String errorMessage = ((FieldError) e.getBindingResult().getAllErrors().get(indice)).getDefaultMessage();
            String erroFormatado = String.format("Erro no campo %s mensagem erro %s",fieldName,errorMessage);
            erros.put(String.format("Erro %s ",indice ), String.format("Erro no campo %s mensagem erro %s",fieldName,errorMessage));
        }


        return new ResponseEntity(erros, HttpStatus.BAD_REQUEST);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class, UnexpectedTypeException.class})
    public Map<String, String> handleValidationExceptions(ConstraintViolationException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = ((FieldError) error).getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({Exception.class})
    public static ResponseEntity<Object> treatErrorsException(Exception e){
        return ResponserHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

}
