import { Link } from "react-router-dom";
import axios from "axios";
import { useState, useEffect } from "react";
import { useParams, useLocation } from "react-router";

import { ProductoService } from "../../Service/ProductoService.jsx";
import { ClienteService } from "../../Service/ClienteService";

import globalStyles from "../../styles/global.module.css";
import styles from "../../styles/componentGlobal/paginacion.module.css";


const Paginacion = ({ page, cantidadResultados, resultadosPorPagina }) => {

    const pagina = page ? parseInt(page) : 1;

    const location = useLocation();
    //console.log(location)

    function getNextURL()
    {
        const ultimaAparicionSignoIgual = location.search.lastIndexOf('=');
        const urlSinPageNumber = location.search.substring(0, ultimaAparicionSignoIgual+1);
        return `${urlSinPageNumber}${parseInt(pagina)+1}`;
    }

    function getPrevURL()
    {   
        const ultimaAparicionSignoIgual = location.search.lastIndexOf('=');
        const urlSinPageNumber = location.search.substring(0, ultimaAparicionSignoIgual+1);
        return `${urlSinPageNumber}${parseInt(pagina)-1}`;
    }

    const estoyEnPrimeraPagina = () => pagina === 1;
    const estoyEnUltimaPagina = () => (cantidadResultados / pagina) <= resultadosPorPagina;

    
    return (
        <>
            <div className={styles.contenedor}>
                <Link to={!(pagina === 2) ? getPrevURL()
                                        : location.search.includes('&page=') ? location.search.replace('&page=2', '')
                                                                            : location.search.replace('?page=2', location.pathname)}
                    className={estoyEnPrimeraPagina() ? styles.linkDeshabilitado : ''}>
                    <div className={`
                        ${styles.iconoPrev}
                        ${estoyEnPrimeraPagina() ? styles.botonDeshabilitado : ''}
                    `}>
                        <i className="fas fa-chevron-left"></i>
                    </div>
                </Link>
                <div className={styles.paginaNumeroContenedor}>
                    <p className={styles.paginaNumero}>
                        {pagina || '1'}
                    </p>
                </div>
                <Link to={!estoyEnPrimeraPagina() ? getNextURL()
                                                : location.search ? `${location.search}&page=2`
                                                                    : `${location.search}?page=2`}
                    className={estoyEnUltimaPagina() ? styles.linkDeshabilitado : ''}>
                    <div className={`
                        ${styles.iconoNext}
                        ${estoyEnUltimaPagina() ? styles.botonDeshabilitado : ''}
                    `}>
                        <i className="fas fa-chevron-right"></i>
                    </div>
                </Link>
            </div>
            {/*<div className={styles.paginaTextoContenedor}>
                <p>
                    Página
                </p>
            </div>*/}
        </>
    )

}


export default Paginacion;