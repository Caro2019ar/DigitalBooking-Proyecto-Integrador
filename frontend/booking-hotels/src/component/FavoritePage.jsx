import React, { useState, useEffect } from "react";
import { Redirect } from "react-router-dom";

import CardProductList from "./componentGlobal/CardProductList.jsx";
import Header from "../component/detailsPage/HeaderDetailsPage";
import NoFavoritesModal from "./modals/Modal";
import { ProductoService } from "../Service/ProductoService";


const FavoritePage = ({ usuario }) => {

	const [favoritesProducts, setfavoritesProducts] = React.useState([]);

	useEffect(() => {

		const favoriteService = new ProductoService();
		const fetchfavoritesProducts = async () => {
			try {
				const products = await favoriteService.getFavoritesIdByUser(usuario.id);
				setfavoritesProducts(products);
			} catch (e) {
				console.log("Error llamado api");
			}
		};
		fetchfavoritesProducts();

	}, []);

	function handleDeleteLastFavorite() {
		if (favoritesProducts.length !== 0) setfavoritesProducts([]);
	}
	

	if (!usuario)
		return <Redirect to="/login" />;
	else if (usuario.rol === "ROLE_ADMIN")
		return <Redirect to="/" />;

	return (
		<>
			<Header producto="" title="Mis favoritos" />
			{favoritesProducts.length ? (
				<CardProductList
					title="Mis favoritos"
					favorite={true}
					usuario={usuario}
					onDeleteLastFavorite={handleDeleteLastFavorite}
				/>
			) : (
				<NoFavoritesModal
					iconName="fas fa-ban"
					text="Aún no tienes ningún favorito"
					buttonText="Ok"
				/>
			)}
		</>
	);
};


export default FavoritePage;