package com.gru.ifsp.AgendamentoBanca.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gru.ifsp.AgendamentoBanca.response.ResponserHandler;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class ResponseUtils {

    public static void showErrorOnResponse(HttpServletResponse response, HttpStatus httpStatus, String errorMessage) throws IOException {
        response.setStatus(httpStatus.value());
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("error", errorMessage);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), responseMap);
    }

    public static void showTokensOnResponse(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("access_token", accessToken);
        tokenMap.put("refresh_token", refreshToken);

        response.setContentType(APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(response.getOutputStream(), ResponserHandler.generateMapTemplateResponse("",HttpStatus.OK, tokenMap));
    }

}
