package com.equipo2.Integrador.util;

import com.equipo2.Integrador.DTO.ClienteDTO;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;


public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private ClienteDTO clienteDTO;
    private String appUrl;
    private Locale locale;

    public OnRegistrationCompleteEvent(ClienteDTO clienteDTO, Locale locale, String appUrl)
    {
        super(clienteDTO);
        this.clienteDTO = clienteDTO;
        this.locale = locale;
        this.appUrl = appUrl;
    }

    public ClienteDTO getClienteDTO() {
        return clienteDTO;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setClienteDTO(ClienteDTO clienteDTO) {
        this.clienteDTO = clienteDTO;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}