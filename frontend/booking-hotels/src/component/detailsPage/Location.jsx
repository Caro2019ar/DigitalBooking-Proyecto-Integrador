import React from "react";
import { useState, useEffect } from "react";

// Librería Fontawesome, uso de íconos:
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMapMarkerAlt } from "@fortawesome/free-solid-svg-icons";
import { faStar } from "@fortawesome/free-solid-svg-icons";
import { ProductoService } from "../../Service/ProductoService";
import { PuntuacionService } from "../../Service/PuntuacionService";

// CSS:
import styles from "../../styles/detailsPage/location.module.css";


const Location = ({ producto, usuario }) => {

  const [puntajeTotalActual, setPuntajeTotalActual] = useState(producto.valoracion.puntajeTotal);
  const [cantidadVotosActual, setCantidadVotosActual] = useState(producto.valoracion.cantidadVotos);
  const [puntajeActualUsuario, setPuntajeActualUsuario] = useState(null);


  useEffect(() => {
    
    const puntuacionService = new PuntuacionService();

    if (usuario && usuario.rol === "ROLE_CUSTOMER")
    {
      puntuacionService.getPuntuacionByProductoAndUsuario(producto.id, usuario.id)
      .then( (data) => {
        if (data.length !== 0)
          setPuntajeActualUsuario(data[0].puntos)
      })
      .catch((e) => {
        if (e.response) console.log(e.response.data.error);
        else
          console.log("Ha ocurrido un error. Por favor intente más tarde.");
      });
    }
    else
      setPuntajeActualUsuario(null);

  }, [usuario]);

  function handlePuntuacion(puntos)
  {
    const productoService = new ProductoService();

    if (puntajeActualUsuario)
    {
      productoService.actualizarPuntuacion(producto.id, usuario.id, puntos)
      .then( () => {
        setPuntajeTotalActual( (puntajeTotalActual) => puntajeTotalActual - puntajeActualUsuario + puntos);
        setPuntajeActualUsuario(puntos);
      })
      .catch((e) => {
        if (e.response) console.log(e.response.data.error);
        else
          console.log("Ha ocurrido un error. Por favor intente más tarde.");
      });
    }
    else
    {
      productoService.agregarPuntuacion(producto.id, usuario.id, puntos)
      .then( () => {
        setPuntajeTotalActual( (puntajeTotalActual) => puntajeTotalActual + puntos);
        setCantidadVotosActual( (cantidadVotosActual) => cantidadVotosActual + 1);
        setPuntajeActualUsuario(puntos);
      })
      .catch((e) => {
        if (e.response) console.log(e.response.data.error);
        else
          console.log("Ha ocurrido un error. Por favor intente más tarde.");
      });
    }
  }

  function handleBorrarVoto()
  {
    const productoService = new ProductoService();

    if (puntajeActualUsuario)
    {
      productoService.eliminarPuntuacion(producto.id, usuario.id)
      .then( () => {
        setPuntajeTotalActual( (puntajeTotalActual) => puntajeTotalActual - puntajeActualUsuario);
        setCantidadVotosActual( (cantidadVotosActual) => cantidadVotosActual - 1);
        setPuntajeActualUsuario(null);
      })
      .catch((e) => {
        if (e.response) console.log(e.response.data.error);
        else
          console.log("Ha ocurrido un error. Por favor intente más tarde.");
      });
    }
  }

  function getStars()
  {
    const arrayStars = [];
    for(let i = 1; i <= 5; i++)
    {
      arrayStars.push(
      <li
        key={i}
        onClick={ () => handlePuntuacion(i) }
        className={`
          ${styles.starIconContainer}
          ${(puntajeActualUsuario >= i) ? styles.starIconContainerVoted : ''}
        `}>
        <FontAwesomeIcon icon={faStar} className={styles.starIcon} />
      </li>
      );
    }

    return arrayStars;
  }

  function getRatingText()
  {
    if (cantidadVotosActual === 0)
      return "";
    
    const promedioActual = (puntajeTotalActual / cantidadVotosActual).toFixed(1);

    if (promedioActual >= 4.5)
      return "Divino";
    else if (promedioActual >= 4.0)
      return "Excelente";
    else if (promedioActual >= 3.5)
      return "Muy bueno";
    else if (promedioActual >= 3.0)
      return "Bueno";
    else if (promedioActual >= 2.5)
      return "Aceptable";
    else if (promedioActual >= 2.0)
      return "Pobre";
    else if (promedioActual >= 1.5)
      return "Horrible";
    else
      return "Desastroso";
  }


  return (
    <>
      <div className={styles.container}>
        <div className={styles.textLocationContainer}>
          <FontAwesomeIcon
            icon={faMapMarkerAlt}
            className={styles.headerIcon}
          />
          <div className={styles.textBox}>
            <h4>{producto.ciudad?.nombre}, {producto.ciudad?.pais}</h4>
            <h4></h4>
          </div>
        </div>
        <div className={styles.reviewContainer}>
          <div
            className={`
              ${styles.borrarVotoLeft}
              ${!puntajeActualUsuario ? styles.hide : ''}
            `}
            onClick={handleBorrarVoto}
          >
            Borrar
          </div>
          <div className={styles.scoreContainer}>
            <h4>
              {getRatingText()}
            </h4>
            <ul className={`
              ${styles.starsContainer}
              ${(!usuario || usuario.rol === "ROLE_ADMIN") ? styles.sinUsuario : ''}
            `}>
              {getStars()}
            </ul>
            <div
              className={`
                ${styles.borrarVotoBottom}
                ${!puntajeActualUsuario ? styles.hide : ''}
              `}
              onClick={handleBorrarVoto}
            >
              Borrar
            </div>
          </div>
          <span>
            <p>
              {(cantidadVotosActual === 0) ?
              '-' :
              (puntajeTotalActual / cantidadVotosActual).toFixed(1)}
            </p>
          </span>
          <div className={styles.cantidadVotos}>
            <p>
              {`(${cantidadVotosActual})`}
            </p>
          </div>
        </div>
      </div>
    </>
  );

}


export default Location;