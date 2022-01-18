import axios from 'axios';

import dataCategorias from "../dataCategorias";
import { Link, useHistory } from "react-router-dom";
import { useState, useEffect } from "react";
//CSS
import styles from "../styles/index/categories.module.css";

function Category(params) {
  /* =========== Axios a la Api de Categorías ========= */
  const history = useHistory()
  const[listaC, setLista]= useState([])
      useEffect(()=> {
  const listCategories = async () => {
     try{
        const response = await axios.get(
           "http://localhost:8080/categorias"
           );
           setLista(response.data);
        } catch (err){
           console.log(err);
        }
     }
     listCategories();
  },[]);

  /* =========== Lista de categorias hardcodeada ========= */

  return (
    <div className={styles.category}>
      <h2>Buscar por tipo de alojamiento</h2>
      <div className={styles.typeContainer}>
        {listaC.map((item) => (
          <div key={item.titulo} className={styles.typeCard}>
            <Link to={`./?categoria=${item.titulo}`}>
              <img src={item.urlImagen} alt={item.titulo} />
            </Link>

            <h3>{item.titulo}</h3>
            <p>{item.descripcion}</p>
          </div>
        ))}
      </div>
    </div>
  );
}

export default Category;
