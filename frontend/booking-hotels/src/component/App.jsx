import React, { useState, useEffect } from "react";
import { BrowserRouter } from "react-router-dom";
import { actualizarEstadoSesion } from "../tokenUtils";
import Swal from "sweetalert2";

//component
import ScrollToTop from "./componentGlobal/ScrollToTop";
import Header from "./header/Header";
import Main from "./index/Main";
import Footer from "./componentGlobal/Footer";

//CSS
import globalStyles from "../styles/global.module.css";
import "../styles/index/main.module.css";

import "../styles/componentGlobal/sweetalert.css";


function App() {

	const [usuario, setUsuario] = useState(null);
	const [timeoutIds, setTimeoutIds] = useState([]);

	function checkIfLoggedIn()
	{
		const usuarioEnStorage = JSON.parse(localStorage.getItem("usuario"));
		if ((usuario && !usuarioEnStorage) || (!usuario && usuarioEnStorage))
			setUsuario(usuarioEnStorage);
	}

	function handleIniciarSesion(usuario, token)
	{
		localStorage.setItem("usuario", JSON.stringify(usuario));
		localStorage.setItem("token", JSON.stringify(token));
		setUsuario(usuario);
	}

	function handleCerrarSesion()
	{
		localStorage.removeItem("usuario");
		localStorage.removeItem("token");

		if (timeoutIds.length !== 0)
		{
			for (const id of timeoutIds) {
				clearTimeout(id);
			}
			setTimeoutIds([]);
		}

		if (usuario !== null)
			setUsuario(null);
	}

	function handleAlert(tiempoRestante)
	{
		let timerInterval;

		Swal.fire({
			title: "Cerrando sesión...",
			html: "Su sesión expirará en <b></b> segundos",
			timer: (tiempoRestante > 60000) ? 60000 : tiempoRestante,
			showCloseButton: true,
			timerProgressBar: true,
			didOpen: () => {
				Swal.showLoading();
				const b = Swal.getHtmlContainer().querySelector("b");
				timerInterval = setInterval(() => {
					b.textContent = (Swal.getTimerLeft() / 1000).toFixed();
				}, 100);
			},
			willClose: () => {
				clearInterval(timerInterval);
			},
		}).then((result) => {
			if (result.dismiss === Swal.DismissReason.timer) {
				//console.log('I was closed by the timer')
			}
		});
	}

	useEffect(() => {

		let timeoutIdsArray = [];
		if (usuario)
		{
			timeoutIdsArray = actualizarEstadoSesion(handleAlert, handleCerrarSesion);
			setTimeoutIds(timeoutIdsArray);
		}
		return () => {
			for (const id of timeoutIdsArray) {
				clearTimeout(id);
			}
		};

	}, [usuario]);


	checkIfLoggedIn();


	return (
		<div className={globalStyles.App}>
			<BrowserRouter>
				<ScrollToTop />
				<Header usuario={usuario} onCerrarSesion={handleCerrarSesion} />
				<Main usuario={usuario} onIniciarSesion={handleIniciarSesion} />
				<Footer />
			</BrowserRouter>
		</div>
	);
}


export default App;