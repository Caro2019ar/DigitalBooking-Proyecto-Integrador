import React from "react";
import { Route, Switch, useLocation } from "react-router-dom";
import { useState } from "react";

import DateContext from "../../context/DateContext";

import Buscador from "./Buscador";
import Category from "./Category";
import CardProductList from "../componentGlobal/CardProductList";
import SignInForm from "../SignInForm";
import SignUpForm from "../SignUpForm";
import RegistroOk from "../RegistroOk";
import NotFound from "../NotFound";
import DetailsPage from "../detailsPage/DetailsPage";
import BookingPage from "../booking/BookingPage";
import BookingOkPage from "../booking/BookingOkPage";
import FavoritePage from "../FavoritePage";
import NewProductPage from "../newProduct/NewProductPage";
import NewProductOkPage from "../newProduct/NewProductOkPage";
import MyBookingsPage from "../myBookingsPage/MyBookingsPage";
import MyReviewsPage from "../myReviewsPage/MyReviewsPage";
import { ProductoService } from "../../Service/ProductoService";


const Main = ({ usuario, onIniciarSesion }) => {

	const [dates, setDates] = useState({});

	const productService = new ProductoService();

	let location = useLocation();
	if (
		((location.pathname === "/" && !location.search.includes("inicio")) ||
			location.pathname === "/favorite") &&
		Object.keys(dates).length !== 0
	)
		setDates({});

	const handleDates = (startDate, endDate) => {
		setDates({
			startDate,
			endDate,
		});
	};

	return (
		<DateContext.Provider value={dates}>
			<main>
				<Switch>
					<Route exact path="/">
						<Buscador onBuscar={handleDates} />
						<Category />
						<CardProductList
							title="Recomendaciones"
							favorite={false}
							usuario={usuario}
						/>
					</Route>
					<Route exact path="/login">
						<SignInForm usuario={usuario} onIniciarSesion={onIniciarSesion} />
          </Route>
					<Route exact path="/register">
				    <SignUpForm usuario={usuario} />
          </Route>
					<Route exact path="/registro-exito" component={RegistroOk} />
					<Route exact path="/product/:id">
						<DetailsPage usuario={usuario} />
					</Route>
					<Route exact path="/product/:id/booking">
						<BookingPage usuario={usuario} />
					</Route>
					<Route exact path="/booking-success" component={BookingOkPage} />
					<Route exact path="/favorite">
						<FavoritePage usuario={usuario} />
					</Route>
					<Route exact path="/admin/newProduct">
            <NewProductPage usuario={usuario} />
          </Route>
					<Route
						exact path="/new-product-success"
						component={NewProductOkPage}
					/>
					<Route exact path="/my-reservations">
						<MyBookingsPage user={usuario} />
					</Route>
					<Route exact path="/my-reviews">
						<MyReviewsPage user={usuario} />
					</Route>
					<Route path="*" component={NotFound} />
				</Switch>
			</main>
		</DateContext.Provider>
	);
};

export default Main;
