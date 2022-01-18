import React, { useState, useLayoutEffect } from "react";
import { registerLocale } from "react-datepicker";
import es from "date-fns/locale/es";

// Importación de componentes:
import Calendar, { moment, getDate, Math } from "react-datepicker";
import Button from "../Button";

//CSS
import "../../styles/detailsPage/calendar.css";
import styles from "../../styles/detailsPage/availableDate.module.css";

registerLocale("es", es);

const AvailableDate = () => {
  const availableDay = ["04-11-2021", "13-11-2021"];

  const min_width_Tablet = 768;
  // Función para que re-renderice la página cuando cambia el ancho (viewport) y actualice el calendario para mostrar 1 o 2
  function useWindowSize() {
    const [size, setSize] = useState([0, 0]);
    useLayoutEffect(() => {
      function updateSize() {
        setSize([window.innerWidth, window.innerHeight]);
      }
      window.addEventListener("resize", updateSize);
      updateSize();
      return () => window.removeEventListener("resize", updateSize);
    }, []);
    return size;
  }
  const [screenWidth] = useWindowSize();
  return (
    <div className={styles.availableDate}>
      <div className={styles.boxTitleAndCalendar}>
        <h2 className={styles.title}>Fechas disponibles</h2>
        <Calendar
          inline
          locale="es"
          selected={false}
          calendarClassName="calendar"
          monthsShown={`${screenWidth < min_width_Tablet ? "1" : "2"}`}

          //***** ejemplo igual al de la doc ****** */
          // dayClassName={(date) =>
          //   getDate(date) < Math.random() * 31 ? "notAvailableDay" : undefined
          // }

          //***** ejemplo sacado de aca (https://webmonkez.com/questions/92465/como-marcar-fechas-particulares-en-react-calendar) ****** */
          // dayClassName={(date) => {
          //   if (
          //     availableDay.find((x) => x === moment(date).format("DD-MM-YYYY"))
          //   ) {
          //     return "notAvailableDay";
          //   }
          // }}
        />
        <div className="calendar vertical-line" />
      </div>
      <div className={styles.startBooking}>
        <p className={styles.text}>
          Agregá tus fechas de viaje para obtener precios exactos
        </p>
        <button className={styles.button}>
          <Button to="reservation" id="btn-reserva" text="Iniciar reserva" />
        </button>
      </div>
    </div>
  );
};

export default AvailableDate;
