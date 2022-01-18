import React from "react";

// CSS
import styles from "../../styles/detailsPage/description.module.css";


const Description = (props) => {

  return (
    <div className={styles.descriptionTextContainer}>
      <h2>{props.text} {props.producto.ciudad.nombre}</h2>
      <p>{props.producto.descripcion}</p>
    </div>
  );

};


export default Description;