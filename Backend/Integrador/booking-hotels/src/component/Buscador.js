import React, { useState } from "react";
import { SelectMenu, Calendar } from "./BuscadorComponentes.js";

//CSS
import "react-datepicker/dist/react-datepicker.css";
import stylesbuscador from "../styles/index/buscador.module.css";
import "../styles/index/datepicker.css";
import "../styles/global.module.css";
import { useHistory, useLocation } from "react-router";
/* ================================================= */
/* =========== Buscador con componentes ============ */
/* ================================================= */
const Buscador = () => {
const [dateRange, setDateRange] = useState([null, null]);
const [selectedCity, setSelectedCity] = useState("");
const [message, setMessage] = useState("");
const [startDate, endDate] = dateRange;
const {search} = useLocation()
const searchParams = new URLSearchParams(search)
const city = searchParams.get("city")
const history = useHistory()

	const pull_date = (date_Range) => {
		setDateRange(date_Range);
	};
	const parentCallback = (selected_City) => {
		setSelectedCity(selected_City);
	};

	const handleSubmit = (event) => {
		if (!startDate) {
			setMessage("Por favor, informe Check-in");
			event.preventDefault();
		} else if (startDate && !endDate) {
			setMessage("Por favor, informe Check-out");
			event.preventDefault();
		} else if (startDate && endDate && selectedCity) {
			setMessage("");
		} else {
			setMessage("Por favor, informe un Destino");
			event.preventDefault();
		}
		history.replace(`/?ciudad=${selectedCity}`)
		console.log(selectedCity);
  	};

	return (
		<div className={stylesbuscador.buscador}>
		<form action="">
			<h2 className={stylesbuscador.text_center}>
			Busca ofertas en hoteles, casas y mucho más
			</h2>
			<div className={stylesbuscador.buscador_container}>
			<SelectMenu
				parentCallback={parentCallback}
				selectedCity={selectedCity}
			
			/>
				<Calendar pull_date={pull_date} dateRange={dateRange} />
			<button
				type="button"
				data-test="buscarButton"
				className={stylesbuscador.button}
				onClick={(event) => handleSubmit(event)}
			>
				Buscar
			</button>
			</div>
			<span className={stylesbuscador.span_error_message}>{message}</span>
		</form>
		</div>
	);
};

export default Buscador;
