package com.equipo2.Integrador.controller;

import com.equipo2.Integrador.config.JwtTokenUtil;
import com.equipo2.Integrador.service.impl.JwtUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.equipo2.Integrador.model.JwtRequest;
import com.equipo2.Integrador.model.JwtResponse;


@Tag(name = "Usuarios / Login")
@Profile("!test")
@CrossOrigin
@RestController
@RequestMapping("/usuarios")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;


    @Operation(summary = "Login del usuario")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception
    {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws Exception
    {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    // Este endpoint no va a ser necesario cuando validemos el token desde el front
    /*@RequestMapping(value = "/validar", method = RequestMethod.POST)
    public ResponseEntity<Boolean> validarToken(@RequestBody JwtRequest authenticationRequest,
                                                @RequestHeader(name = "Authorization") String token) throws Exception
    {
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        return ResponseEntity.ok(jwtTokenUtil.validateToken(token.substring(7), userDetails));
    }*/
}