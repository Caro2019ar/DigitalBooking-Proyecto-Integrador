import React, { useState, useEffect } from "react";
import Select from "react-select";

import { CityService } from "../../Service/CityService";
import { CategoriaService } from "../../Service/CategoriaService";
import { ProductoService } from "../../Service/ProductoService";

import "../../styles/newProduct/selectBuscador.css";


const SelectBuscador = ({ recurso, hayError, onChange }) => {

  const [lista, setLista] = useState([]);


useEffect( () => {

    // Se usa esta variable para evitar error en consola:
    // "Can't perform a React state update on an unmounted component"
    let isMounted = true;

    if (recurso === 'ciudades')
    {
      const cityService = new CityService();
      cityService.getAllCity()
      .then( (data) => { if (isMounted) setLista(listadoEnFormatoSelect(data)) } )
      .catch( (e) => {
        if (e.response) console.log(e.response.data.error);
        else console.log("Ha ocurrido un error. Por favor intente más tarde.");
      });
    }

    if (recurso === 'categorias')
    {
      const categoriaService = new CategoriaService();
      categoriaService.getAllCategorias()
      .then( (data) => { if (isMounted) setLista(listadoEnFormatoSelect(data)) } )
      .catch( (e) => {
        if (e.response) console.log(e.response.data.error);
        else console.log("Ha ocurrido un error. Por favor intente más tarde.");
      });
    }

    if (recurso === 'productos')
    {
      const productoService = new ProductoService();
      productoService.getAllProducts()
      .then( (data) => { if (isMounted) setLista(listadoEnFormatoSelect(data)) } )
      .catch((e) => {
        if (e.response) console.log(e.response.data.error);
        else console.log("Ha ocurrido un error. Por favor intente más tarde.");
      });
    }

    return () => { isMounted = false };

}, []);

  function listadoEnFormatoSelect(data)
  {
    if (recurso === 'ciudades')
    {
      const listadoCiudades = [];
      for (let i = 0; i < data.length; i++) {
        listadoCiudades.push({
          value: data[i].id,
          label: `${data[i].nombre}, ${data[i].pais}`,
        });
      }
      return listadoCiudades;
    }

    if (recurso === 'categorias')
    {
      const listadoCategorias = [];
      for (let i = 0; i < data.length; i++) {
        listadoCategorias.push({
          value: data[i].id,
          label: `${data[i].titulo}`,
        });
      }
      return listadoCategorias;
    }

    if (recurso === 'productos')
    {
      const listadoProductos = [];
      for (let i = 0; i < data.length; i++) {
        listadoProductos.push({
          value: data[i].id,
          label: `${data[i].id} - ${data[i].nombre}`,
        });
      }
      return listadoProductos;
    }
  }

  function selectFormatOptionLabel({ value, label })
  {
      return (
        <div>
            {label}
        </div> 
      )
  };

  const handleChange = (e) => onChange(e.value);

  const selectStyles = {

    control: (base) => ({
        ...base,
        backgroundColor: hayError ? '#FFE1E1' : '#FFFFFF',
        boxShadow: '0px 1px 5px rgb(0 0 0 / 15%)',
        borderRadius: '5px',
        margin: '8px 0',
        padding: '12px 20px',
        fontSize: '0.75rem',
        border: hayError ? '1px solid #FF0000' : '1px solid transparent',
        '&:hover:not(:focus)': {
            border: hayError ? '1px solid #FF0000' : '1px solid transparent'
        },
        '&:focus': {
            outline: 'none !important',
            border: '1px solid rgb(179, 179, 179)'
        }
    }),

    valueContainer: (base) => ({
        ...base,
        padding: 0,
        cursor: "text"
    }),

    input: (base) => ({
        ...base,
        padding: 0,
        margin: 0
    }),

    placeholder: (base) => ({
        ...base,
        color: "#A9A9A9"
    }),

    option: (base, { isSelected }) => ({
        ...base,
        color: isSelected ? "#FFFFFF" : '#383B58',
        fontSize: '0.8rem',
        fontWeight: 'bold',
        width: 'auto',
        padding: '12px 20px 0px 20px',
        cursor: 'pointer',
        /*'&:hover': {
            color: '#FFFFFF',
            backgroundColor: '#2684FF'
        }*/
        '&:after': {
            content: '""',
            display: 'block',
            width: '100%',
            borderBottom: '#1DBEB4 1px solid',
            paddingTop: '12px'
        },
    }),

  };

  function getPlaceholder()
  {
    if (recurso === 'ciudades')
      return "Seleccione una ciudad de la lista";
    else if (recurso === 'categorias')
      return "Seleccione una categoría de la lista";
  }


  return (
    <>
      <Select
        classNamePrefix='selectBuscador'
        placeholder={getPlaceholder()}
        formatOptionLabel={selectFormatOptionLabel}
        options={lista}
        onChange={handleChange}
        styles={selectStyles}
      />
    </>
  );

};


export default SelectBuscador;