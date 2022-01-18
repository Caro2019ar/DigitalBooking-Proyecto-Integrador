import React, { useState, useEffect } from "react";
import { Redirect } from "react-router-dom";

import Header from "../detailsPage/HeaderDetailsPage";
import CardProduct from "../componentGlobal/CardProduct";
import NoReviewsModal from "../modals/Modal";
import { ProductoService } from "../../Service/ProductoService.jsx";

// CSS
import "../../styles/index/cardproducttransitions.css";
import myBookingsPage from "../../styles/myBookingsPage/myBookingsPage.module.css";


const MyReviewsPage = (props) => {

  const [reviewsByUser, setReviewsByUser] = useState([]);

  useEffect(() => {

    const productoService = new ProductoService();
    const fetchReviewsByUser = async () => {
      try {
        const products =
          await productoService.getProductsByPuntuacionFromUsuario(
            props.user.id
          );
        setReviewsByUser(products);
      } catch (e) {
        console.log("Error llamado api");
      }
    };
    fetchReviewsByUser();

  }, []);
  

  if (!props.user) return <Redirect to="/login" />;
  else if (props.user.rol === "ROLE_ADMIN") return <Redirect to="/" />;

  //console.log(reviewsByUser);

  return (
    <>
      <Header producto="" title="Mis valoraciones" />
      <div className={`${myBookingsPage.productsCardContainer}`}>
        <div
          className={`
            ${myBookingsPage.cardContainer}
            ${
              reviewsByUser.length === 0
                ? myBookingsPage.cardContainerNoProducts
                : ""
            }
        `}
        >
          {reviewsByUser.length ? (
            reviewsByUser.map((product) => {
              return (
                <CardProduct
                  key={product.id}
                  item={product}
                  usuarioId={props.user.id}
                />
              );
            })
          ) : (
            <NoReviewsModal
              iconName="fas fa-ban"
              text="Aún no has hecho ninguna review"
              buttonText="Ok"
            />
          )}
        </div>
      </div>
    </>
  );
};


export default MyReviewsPage;