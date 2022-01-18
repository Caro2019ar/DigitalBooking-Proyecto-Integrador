package com.equipo2.Integrador.service;

import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;

import java.util.List;

public interface IService<T, ID> {

    public T agregar(T t) throws BadRequestException, ResourceConflictException;
    public T buscar(ID id) throws ResourceNotFoundException;
    public List<T> listar();
    public T actualizar(T t) throws BadRequestException, ResourceNotFoundException, ResourceConflictException;
    public void eliminar(ID id) throws ResourceNotFoundException, BadRequestException;
}