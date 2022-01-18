import React, { useState, useContext } from "react";
import { SelectMenu, Calendar } from "./BuscadorComponentes.jsx";

import DateContext from "../../context/DateContext";

//CSS
import "react-datepicker/dist/react-datepicker.css";
import stylesbuscador from "../../styles/index/buscador.module.css";
import stylesNewProduct from "../../styles/newProduct/buscadorCity.css";
import "../../styles/index/datepicker.css";
import "../../styles/global.module.css";
import { useHistory, useLocation } from "react-router";
/* ================================================= */
/* =========== Buscador con componentes ============ */
/* ================================================= */
const Buscador = (props) => {
  const [dateRange, setDateRange] = useState([null, null]);
  const [selectedCity, setSelectedCity] = useState("");
  const [message, setMessage] = useState("");
  const [startDate, endDate] = dateRange;
  const { search } = useLocation();
  const searchParams = new URLSearchParams(search);
  const city = searchParams.get("city");
  const history = useHistory();
  const dates = useContext(DateContext);
  let startDateString;
  let endDateString;

  const handleDates = props.onBuscar;

  const pull_date = (date_Range) => {
    setDateRange(date_Range);
  };
  const parentCallback = (selected_City) => {
    setSelectedCity(selected_City);
  };

  const stringify = (start, end) => {
    if (start) {
      startDateString = start.toString().substr(4, 11);
    }
    if (end) {
      endDateString = end.toString().substr(4, 11);
    }
  };

  stringify(startDate, endDate);

  const handleSubmit = (event) => {
    if (!startDate && selectedCity) {
      setMessage("");
      history.replace(`/?ciudad=${selectedCity}`);
      event.preventDefault();
    } else if (!startDate) {
      setMessage("Por favor, informe destino y/o fechas deseadas");
      event.preventDefault();
    } else if (startDate && !endDate && selectedCity) {
      setMessage("Por favor, informe fecha de salida");
      history.replace(`/?ciudad=${selectedCity}`);
      event.preventDefault();
    } else if (startDate && !endDate) {
      setMessage("Por favor, informe fecha de salida");
      event.preventDefault();
    } else if (startDate && endDate && selectedCity) {
      setMessage("");
      history.replace(
        `/?inicio=${startDateString}&fin=${endDateString}&ciudad=${selectedCity}`
      );
    } else {
      setMessage("");
      event.preventDefault();
      history.replace(`/?inicio=${startDateString}&fin=${endDateString}`);
    }
    handleDates(startDate, endDate);
    // history.replace(`/?ciudad=${selectedCity}`)
    console.log(selectedCity);
  };

  return (
    <div className={stylesbuscador.buscador}>
      <form action="">
        <h2 className={stylesbuscador.text_center}>
          Busca ofertas en hoteles, casas y mucho más
        </h2>
        <div
          className={`${stylesbuscador.buscador_container} ${stylesNewProduct.buscador_container}`}
        >
          <SelectMenu
            parentCallback={parentCallback}
            selectedCity={selectedCity}
            placeholder="¿A dónde vamos?"
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
