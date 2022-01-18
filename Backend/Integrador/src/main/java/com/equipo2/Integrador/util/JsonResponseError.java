package com.equipo2.Integrador.util;

import java.sql.Timestamp;


public class JsonResponseError {

    Timestamp timestamp;
    Integer status;
    String error;

    public JsonResponseError() {
    }

    public JsonResponseError(Integer status, String error)
    {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.status = status;
        this.error = error;
    }

    public Timestamp getTimestamp()
    {
        return timestamp;
    }

    public Integer getStatus()
    {
        return status;
    }

    public String getError()
    {
        return error;
    }

    @Override
    public String toString() {
        return "JsonResponseError{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", error='" + error + '\'' +
                '}';
    }
}
