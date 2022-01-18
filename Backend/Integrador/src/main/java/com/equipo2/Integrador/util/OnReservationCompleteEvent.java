package com.equipo2.Integrador.util;

import com.equipo2.Integrador.DTO.ClienteDTO;
import com.equipo2.Integrador.DTO.ReservaDTO;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;


public class OnReservationCompleteEvent extends ApplicationEvent {

    private ReservaDTO reservaDTO;
    private String appUrl;
    private Locale locale;

    public OnReservationCompleteEvent(ReservaDTO reservaDTO, Locale locale, String appUrl)
    {
        super(reservaDTO);
        this.reservaDTO = reservaDTO;
        this.locale = locale;
        this.appUrl = appUrl;
    }

    public ReservaDTO getReservaDTO()
    {
        return reservaDTO;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setReservaDTO(ClienteDTO clienteDTO) {
        this.reservaDTO = reservaDTO;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}