import React from "react";

//Componente
import HorizontalLine from "../HorizontalLine";

// CSS
import DescriptionStyles from "../../styles/detailsPage/description.module.css";
import styles from "../../styles/detailsPage/productPolicy.module.css";

const ProductPolicy = () => {
  return (
    <div className={DescriptionStyles.descriptionTextContainer}>
      <h2>Qué tienes que saber</h2>
      <HorizontalLine />
      <div className={styles.container}>
        <div className={styles.columnContainer}>
          <h3>Normas de la casa</h3>
          <ul>
            <li>Check out: 10:00 </li>
            <li>No se permiten fiestas (Qué sad)</li>
            <li>No fumar</li>
          </ul>
        </div>

        <div className={styles.columnContainer}>
          <h3>Salud y seguridad</h3>
          <ul>
            <li>Se aplican las pautas de distanciamiento social blah blah </li>
            <li>Detector de humo</li>
            <li>Depósito de seguridad</li>
          </ul>
        </div>

        <div className={styles.columnContainer}>
          <h3>Política de cancelación</h3>
          <ul>
            <li>Agrega las fechas de tu viaje para obtener blah blah </li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export default ProductPolicy;
