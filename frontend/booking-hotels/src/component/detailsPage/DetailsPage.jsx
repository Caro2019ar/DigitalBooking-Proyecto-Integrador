import React from "react";
import { useState, useEffect } from "react";
import { useParams } from "react-router";

// Importación de componentes:
import HeaderDetailsPage from "./HeaderDetailsPage";
import Location from "./Location";
import ImageGallery from "./ImageGallery";
import DesktopImageGallery from "./DesktopImageGallery";
import Description from "./Description";
import ProductFeatures from "./ProductFeatures";
import AvailableDate from "./AvailableDate";
import Maps from "./Maps";
import ProductPolicy from "./ProductPolicy";
import { ProductoService } from "../../Service/ProductoService";

//css
import stylesDetailsPage from "../../styles/global.module.css";
import Loader from "../componentGlobal/Loader";


const DetailsPage = ({ usuario }) => {

  let { id } = useParams();

  const [loading, setLoading] = useState(null);
  const [producto, setProducto] = useState(null);

  useEffect(() => {

    setLoading(true);
    const productoService = new ProductoService();
    productoService
      .getProductById(id)
      .then( (data) => setProducto(data) )
      .catch( (e) => {
        if (e.response) console.log(e.response.data.error);
        else console.log("Ha ocurrido un error. Por favor intente más tarde.");
      })
      .finally( () => setLoading(false) );

  }, [id]);

  if (loading)
    return <Loader absolute={true} />;

  return producto ? (
    <>
      <HeaderDetailsPage producto={producto} />
      <Location producto={producto} usuario={usuario} />
      <ImageGallery producto={producto} usuario={usuario} />
      <div className={stylesDetailsPage.container}>
        <DesktopImageGallery producto={producto} />
        <Description producto={producto} text="Alójate en el corazón de " />
        <ProductFeatures producto={producto} />
      </div>
      <AvailableDate producto={producto} usuario={usuario} />
      <div className={stylesDetailsPage.container}>
        <Maps producto={producto} />
        <ProductPolicy title="Qué tienes que saber" producto={producto} />
      </div>
    </>
  ) : null
};

export default DetailsPage;
