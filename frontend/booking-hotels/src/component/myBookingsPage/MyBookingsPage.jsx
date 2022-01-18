import React, { useState, useEffect } from "react";
import { Redirect } from "react-router-dom";

import Header from "../detailsPage/HeaderDetailsPage";
import CardProductMyBookings from "../myBookingsPage/CardProductMyBookings";
import NoBookingsModal from "../modals/Modal";

// CSS
import "../../styles/index/cardproducttransitions.css";
import myBookingsPage from "../../styles/myBookingsPage/myBookingsPage.module.css";

import { ReservaService } from "../../Service/ReservaService.jsx";


const MyBookingsPage = (props) => {

  const [productsByBooking, setProductsByBooking] = useState([]);

  useEffect(() => {

    const reservaService = new ReservaService();
    const fetchProductsByBooking = async () => {
      try {
        const products = await reservaService.reservaPorIdUsuario(
          props.user.id
        );
        setProductsByBooking(products);
        // console.log(products, "products");
      } catch (e) {
        console.log("Error llamado api");
      }
    };
    fetchProductsByBooking();

  }, []);


  if (!props.user)
    return <Redirect to="/login" />;
  else if (props.user.rol === "ROLE_ADMIN")
    return <Redirect to="/" />;

  return (
    <>
      <Header producto="" title="Mis reservas" />
      <div className={`${myBookingsPage.productsCardContainer}`}>
        <div className={`
          ${myBookingsPage.cardContainer}
          ${(productsByBooking.length === 0) ? myBookingsPage.cardContainerNoProducts : ''}
        `}>
          {productsByBooking.length ? (
            productsByBooking.map((product) => {
              return (
                <CardProductMyBookings
                  product={product}
                  buttonText="Ver producto"
                  id={product.id}
                  key={product.id}
                />
              );
            })
          ) : (
            <NoBookingsModal
              iconName="fas fa-ban"
              text="Aún no tienes ninguna reserva"
              buttonText="Ok"
            />
          )}
        </div>
      </div>
    </>
  );
};

export default MyBookingsPage;
