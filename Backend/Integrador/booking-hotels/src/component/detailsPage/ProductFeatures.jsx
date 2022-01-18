import React from "react";

//componente
import HorizontalLine from "../HorizontalLine";

// CSS
import productFeatureStyles from "../../styles/detailsPage/productFeatures.module.css";

const ProductFeatures = ({producto}) => {
  return (
    <div className={productFeatureStyles.featuresContainer}>
      <h2>¿Qué ofrece este lugar?</h2>
      <HorizontalLine />
      <div className={productFeatureStyles.iconsContainer}>
        <ul className={`fa-ul ${productFeatureStyles.listContainer}`}>
          {producto.caracteristicas?.map((item) => (
            <li>
            <span className="fa-li">
              <i className={`fas ${item.icono}`}></i>
            </span>
            <p>{item.nombre}</p>
          </li>))}
         
    
        </ul>
      </div>
    </div>
  );
};

export default ProductFeatures;
