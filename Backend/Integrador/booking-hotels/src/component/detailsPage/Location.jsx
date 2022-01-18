import React from "react";

// Librería Fontawesome, uso de íconos:
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMapMarkerAlt } from "@fortawesome/free-solid-svg-icons";
import { faStar } from "@fortawesome/free-solid-svg-icons";

// CSS:
import styles from "../../styles/detailsPage/location.module.css";

const Location = ({producto}) => {
 
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
          <div className={styles.scoreContainer}>
            <h4>Muy bueno</h4>
            <ul className={styles.starContainer}>
              <li>
                <FontAwesomeIcon icon={faStar} className={styles.starIcon} />
              </li>
              <li>
                <FontAwesomeIcon icon={faStar} className={styles.starIcon} />
              </li>
              <li>
                <FontAwesomeIcon icon={faStar} className={styles.starIcon} />
              </li>
              <li>
                <FontAwesomeIcon icon={faStar} className={styles.starIcon} />
              </li>
              <li>
                <FontAwesomeIcon icon={faStar} className={styles.starIcon} />
              </li>
              <li></li>
            </ul>
          </div>
          <span>
            <p>8</p>
          </span>
        </div>
      </div>
    </>
  );
};

export default Location;
