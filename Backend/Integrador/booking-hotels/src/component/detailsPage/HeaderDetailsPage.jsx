import React from "react";
import { Link } from "react-router-dom";
// Librería Fontawesome, uso de íconos:
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faChevronLeft } from "@fortawesome/free-solid-svg-icons";

// CSS:
import styles from "../../styles/detailsPage/headerDetails.module.css";

const HeaderDetailsPage = ({producto}) => {
  
  return (
    <div className={styles.header}>
      <div className={styles.headerText}>
        <h4>{producto.categoria?.titulo}</h4>
        <h3>{producto.nombre}</h3>
      </div>
      <Link to="../">
        <FontAwesomeIcon
          icon={faChevronLeft}
          //   onClick={}
          className={styles.headerIcon}
        />
      </Link>
    </div>
  );
};

export default HeaderDetailsPage;
