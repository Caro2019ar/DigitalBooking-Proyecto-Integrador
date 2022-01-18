package com.equipo2.Integrador.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Profile("!test")
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {
        // Configure AuthenticationManager so that it knows from where to load user for matching credentials
        // Use BCryptPasswordEncoder
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
        // We don't need CSRF for this example
        httpSecurity.csrf().disable()
                // Don't authenticate this particular request
                //.authorizeRequests().antMatchers("/authenticate").permitAll()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/reservas").hasAnyRole("CUSTOMER")
                .antMatchers(HttpMethod.GET, "/productos/*/es-favorito**").hasAnyRole("CUSTOMER")
                .antMatchers(HttpMethod.GET, "/productos/favoritos**").hasAnyRole("CUSTOMER")
                .antMatchers(HttpMethod.GET, "/productos/paginado/favoritos**").hasAnyRole("CUSTOMER")
                .antMatchers(HttpMethod.GET, "/productos/favoritos-id**").hasAnyRole("CUSTOMER")
                .antMatchers(HttpMethod.PUT, "/clientes/*/agregar-favorito").hasAnyRole("CUSTOMER")
                .antMatchers(HttpMethod.PUT, "/clientes/*/eliminar-favorito").hasAnyRole("CUSTOMER")
                .antMatchers(HttpMethod.GET, "/productos/puntuaciones-cliente**").hasAnyRole("CUSTOMER")
                .antMatchers(HttpMethod.PUT, "/productos/*/agregar-puntuacion").hasAnyRole("CUSTOMER")
                .antMatchers(HttpMethod.PUT, "/productos/*/actualizar-puntuacion").hasAnyRole("CUSTOMER")
                .antMatchers(HttpMethod.PUT, "/productos/*/eliminar-puntuacion").hasAnyRole("CUSTOMER")
                .antMatchers(HttpMethod.POST, "/productos").hasAnyRole("ADMIN")
                //.antMatchers("/*", "/**").permitAll()
                //.antMatchers("/*", "/**").hasRole("ADMIN")
                // All other requests need to be authenticated
                //.anyRequest().authenticated().and()
                .anyRequest().permitAll().and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                // Make sure we use stateless session; session won't be used to store user's state.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // Para deshabilitar Spring Boot Security en las peticiones preflight
                .and().cors();

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /*@Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new JwtAuthenticationFailureHandler();
    }*/

    // Para poder ingresar a la base de datos H2
    @Override
    public void configure(WebSecurity web) throws Exception
    {
        web.ignoring().antMatchers("/h2-console/**");
    }
}