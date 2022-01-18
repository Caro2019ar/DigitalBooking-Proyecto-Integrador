import React from "react";

//Componente
import HorizontalLine from "../componentGlobal/HorizontalLine";

// CSS
import DescriptionStyles from "../../styles/detailsPage/description.module.css";
import styles from "../../styles/detailsPage/productPolicy.module.css";

const ProductPolicy = (props) => {
  let arrayProductRules = props.producto.politica.normas.split(",");
  let arrayProductSecurity = props.producto.politica.saludYSeguridad.split(",");
  let arrayProductCancellation = props.producto.politica.cancelacion.split(",");

  return (
    <div className={DescriptionStyles.descriptionTextContainer}>
      <h2>{props.title}</h2>
      <HorizontalLine />
      <div className={styles.container}>
        <div className={styles.columnContainer}>
          <h3>Normas de la casa</h3>
          <ul>
            {arrayProductRules.map((productRule, index) => {
              return <li key={index}>{productRule} </li>;
            })}
          </ul>
        </div>

        <div className={styles.columnContainer}>
          <h3>Salud y seguridad</h3>
          <ul>
            {arrayProductSecurity.map((ProductSecurity, index) => {
              return <li key={index}>{ProductSecurity} </li>;
            })}
          </ul>
        </div>

        <div className={styles.columnContainer}>
          <h3>Política de cancelación</h3>
          <ul>
            {arrayProductCancellation.map((ProductCancellation, index) => {
              return <li key={index}>{ProductCancellation} </li>;
            })}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default ProductPolicy;
