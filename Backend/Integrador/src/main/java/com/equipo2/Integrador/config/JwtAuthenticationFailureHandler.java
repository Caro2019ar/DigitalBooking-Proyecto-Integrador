package com.equipo2.Integrador.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


@Profile("!test")
@Component
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler, Serializable {

    private static final long serialVersionUID = -7858869558953243875L;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse httpServletResponse,
                                        AuthenticationException ex) throws IOException, ServletException
    {
        Map<String,Object> response = new HashMap<>();
        response.put("status","34");
        response.put("message","unauthorized access");

        httpServletResponse.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        OutputStream out = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(out, response);
        out.flush();
    }
}