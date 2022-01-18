import React, { useState, useEffect } from "react";

//componente
import HorizontalLine from "../HorizontalLine";

//image
import mapDesktop from "../../image/maps-desktop.png";
import mapMobileTablet from "../../image/maps-mobile-tablet.png";

// CSS:
import styles from "../../styles/detailsPage/maps.module.css";

const Maps = () => {
  //TODO deberia armarse un hook para no repetir este codigo
  //manejo del ancho de pantalla
  const [width, setWidth] = useState(window.innerWidth);

  const handleResize = () => setWidth(window.innerWidth);

  useEffect(() => {
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  return (
    <>
      <div className={styles.maps}>
        <h2 className={styles.title}>¿Dónde vas a estar?</h2>
        <HorizontalLine />
        <div className={styles.location}>Buenos Aires, Argentina</div>
        <img
          className={styles.imgMap}
          src={`${width < 768 ? mapMobileTablet : mapDesktop}`}
          alt="mapa ubicacion"
        />
      </div>
    </>
  );
};

export default Maps;
