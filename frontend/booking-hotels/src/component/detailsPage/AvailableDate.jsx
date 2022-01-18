import React, { useState, useEffect, useLayoutEffect, useContext } from "react";
import { Link } from "react-router-dom";
import { registerLocale } from "react-datepicker";
import es from "date-fns/locale/es";

import DateContext from "../../context/DateContext";
import { ProductoService } from "../../Service/ProductoService";
import useWidthScreen from "../../hooks/useWidthScreen";

// Importación de componentes:
import Calendar from "react-datepicker";

//CSS
import globalStyles from "../../styles/global.module.css";
import "../../styles/detailsPage/calendar.css";
import styles from "../../styles/detailsPage/availableDate.module.css";

registerLocale("es", es);

const AvailableDate = ({ producto, usuario }) => {
  const dates = useContext(DateContext);
  const [notAvailableDays, setNotAvailableDays] = useState([]);

  useEffect(() => {
    if (notAvailableDays.length === 0) {
      const productoService = new ProductoService();
      productoService
        .getNotAvailableDays(producto.id)
        .then((data) => {
          setNotAvailableDays(constructDateArray(data));
        })
        .catch((e) => {
          if (e.response) console.log(e.response.data.error);
          else
            console.log("Se produjo un error llamando a la API de Productos.");
        });
    }
  }, []);

  function constructDateArray(data) {
    let dateArray = [];
    for (let i = 0; i < data.length; i++) {
      let dateString = data[i];
      // Le agrego la hora para obtener la fecha local (si no obtengo la fecha UTC y deshabilita el día anterior al indicado)
      let date = new Date(dateString + "T00:00");
      dateArray.push(date);
    }
    return dateArray;
  }

  // Función para que re-renderice la página cuando cambia el ancho (viewport) y actualice el calendario para mostrar 1 o 2
  const min_width_Tablet = 768;
  const screenWidth = useWidthScreen();

  const capitalizeFirstLetter = (string) =>
    string.charAt(0).toUpperCase() + string.slice(1);

  return (
    <div className={styles.availableDate}>
      <div className={styles.boxTitleAndCalendar}>
        <h2 className={styles.title}>Fechas disponibles</h2>
        <Calendar
          inline
          locale="es"
          selected={false}
          dateFormat="dd/MM/yyyy"
          calendarClassName="calendar"
          monthsShown={`${screenWidth < min_width_Tablet ? "1" : "2"}`}
          //=====Navegación deshabilitada desde la fecha actual=====
          minDate={new Date()}
          //====== Para que no se resalte el día actual en todos los meses (se pierde accesibilidad) ======
          disabledKeyboardNavigation
          //====== Para que la semana empiece el domingo ======
          calendarStartDay={0}
          excludeDates={notAvailableDays}
          formatWeekDay={(nameOfDay) =>
            capitalizeFirstLetter(nameOfDay.substr(0, 3))
          }
          startDate={dates.startDate}
          endDate={dates.endDate}
        />
        <div className="calendar vertical-line" />
      </div>
      <div className={styles.startBooking}>
        <p className={styles.text}>
          Agregá tus fechas de viaje para obtener precios exactos
        </p>
        <Link
          to={usuario ? `${producto.id}/booking` : `/login?from-booking`}
          id="btn-reserva"
        >
          <button className={globalStyles.button}>Iniciar reserva</button>
        </Link>
      </div>
    </div>
  );
};

export default AvailableDate;
