package com.equipo2.Integrador.config;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


@Profile("!test")
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -7858869558953243875L;


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException
    {
        System.out.println(authException.getMessage());
        //System.out.println("sakjgsalgjhsalkhjsalhsjalksajhslkhjsalhjsalkhjsalksajlkhsa");
        //response.setStatus(406);
        //response.getWriter().print(authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        /*response.resetBuffer();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("Content-Type", "application/json");
        response.getOutputStream().print("{\"errorMessage\":\"You can't use this!\"}");
        response.flushBuffer(); // marks response as committed -- if we don't do this the request will go through normally!*/
    }
}