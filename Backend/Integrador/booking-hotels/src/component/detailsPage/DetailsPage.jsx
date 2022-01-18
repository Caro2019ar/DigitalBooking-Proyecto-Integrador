import React from "react";
import axios from 'axios';
import { useState, useEffect } from "react";

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
import {ProductsService} from "../../Service/ProductsService"
import stylesDetailsPage from "../../styles/detailsPage/detailsPage.module.css";
import { useParams } from "react-router";

const DetailsPage = () => {
    let {id} = useParams()
 
    const[producto, setProducto]= useState([])

    useEffect(()=> {
      const productService = new ProductsService()
  
        productService.getProductById(id).then(data=>setProducto(data))
    
    },[id]);


  return (
    <>
      <HeaderDetailsPage producto={producto}/>
      <Location producto={producto}/>
      <ImageGallery producto={producto}/>
      <div className={stylesDetailsPage.detailsPageContainer}>
        <DesktopImageGallery producto={producto}/>
      </div>
        <Description producto={producto}/>
        <ProductFeatures producto={producto} />
        <AvailableDate />
        <Maps />
        <ProductPolicy />
    </>
  );
};

export default DetailsPage;
