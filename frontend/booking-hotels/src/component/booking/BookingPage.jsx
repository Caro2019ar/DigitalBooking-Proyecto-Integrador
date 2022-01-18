import React from "react";
import { useState, useEffect } from "react";
import { useParams } from "react-router";
import { Redirect } from "react-router-dom";
// useContext:
// import { DateProvider } from "../BuscadorComponentes";
// import { createContext } from "react";

// components
import { ProductoService } from "../../Service/ProductoService";
import HeaderDetailsPage from "../detailsPage/HeaderDetailsPage";
import ProductPolicy from "../detailsPage/ProductPolicy";
import FormBooking from "./FormBooking";
import BookingDateCalendar from "./BookingDateCalendar";
import Entrytime from "./EntryTime";
import Product from "./Product";

//CSS
import global from "../../styles/global.module.css";
import globalBooking from "../../styles/bookingPage/globalBooking.module.css";
import Loader from "../componentGlobal/Loader";

const BookingPage = ({ usuario }) => {
  let { id } = useParams();

  const [loading, setLoading] = useState(null);
  const [producto, setProducto] = useState({});
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);
  const [city, setCity] = useState(null);
  const [entryTime, setEntryTime] = useState(null);
  const [infoCovid, setInfoCovid] = useState(null);
  const [vacunadoCovid, setVacunadoCovid] = useState(null);

  useEffect(() => {
    setLoading(true);
    const productService = new ProductoService();
    productService
      .getProductById(id)
      .then((data) => {
        setProducto(data);
      })
      .catch((e) => {
        if (e.response) console.log(e.response.data.error);
        else console.log("Ha ocurrido un error. Por favor intente más tarde.");
      })
      .finally(() => setLoading(false));
  }, []);

  const handleDateChange = (startDate, endDate) => {
    setStartDate(startDate);
    setEndDate(endDate);
  };

  const handleCityChange = (city) => setCity(city);
  const handleInfoCovidChange = (infoCovid) => setInfoCovid(infoCovid);
  const handleVacunadoCovidChange = (vacunadoCovid) =>
    setVacunadoCovid(vacunadoCovid);
  const handleEntryTimeChange = (entryTime) => setEntryTime(entryTime);

  if (loading) return <Loader absolute={true} />;

  console.log(producto, "producto");

  return usuario ? (
    <>
      <HeaderDetailsPage producto={producto} />
      <div
        className={`${global.container} ${global.bookingBG} ${globalBooking.grid}`}
      >
        <FormBooking
          producto={producto}
          usuario={usuario}
          className={globalBooking.gridForm}
          onCityChange={handleCityChange}
          onInfoCovidChange={handleInfoCovidChange}
          onVacunadoCovidChange={handleVacunadoCovidChange}
        />
        {Object.keys(producto).length === 0 ? null : (
          <BookingDateCalendar
            productoId={producto.id}
            onDateChange={handleDateChange}
            className={globalBooking.gridCalendar}
          />
        )}
        <Entrytime
          className={globalBooking.gridEntry}
          onSelect={handleEntryTimeChange}
        />
        {Object.keys(producto).length === 0 ? null : (
          <Product
            producto={producto}
            usuario={usuario}
            startDate={startDate}
            endDate={endDate}
            entryTime={entryTime}
            city={city}
            infoCovid={infoCovid}
            vacunadoCovid={vacunadoCovid}
            className={globalBooking.gridProduct}
          />
        )}
      </div>
      {Object.keys(producto).length === 0 ? null : (
        <div className={`${global.container}`}>
          <ProductPolicy title="Qué tienes que saber" producto={producto} />
        </div>
      )}
    </>
  ) : (
    <Redirect to="/login" />
  );
};

export default BookingPage;
