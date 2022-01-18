import React, { useState, useContext, useEffect } from "react";
import { registerLocale } from "react-datepicker";
import es from "date-fns/locale/es";
import useWidthScreen from "../../hooks/useWidthScreen";

//useContext:
import DateContext from "../../context/DateContext";
import { ProductoService } from "../../Service/ProductoService";

// Importación de componentes:
import Calendar from "react-datepicker";

//CSS
import "../../styles/index/datepicker.css";
import "../../styles/detailsPage/calendar.css";
import stylesbuscador from "../../styles/index/buscador.module.css";
import stylesBooking from "../../styles/bookingPage/bookingDateCalendar.module.css";
import "../../styles/bookingPage/bookingCalendar.css";

registerLocale("es", es);

const BookingDateCalendar = (props) => {
  const dates = useContext(DateContext);
  const [startDate, setStartDate] = useState(dates.startDate);
  const [endDate, setEndDate] = useState(dates.endDate);
  const [calendarRef, setCalendarRef] = useState(null);
  const [notAvailableDays, setNotAvailableDays] = useState([]);
  const [fechaDeCorte, setFechaDeCorte] = useState(null);

  const { productoId, onDateChange } = props;

  if (typeof startDate !== "undefined" && typeof endDate !== "undefined")
    onDateChange(startDate, endDate);

  useEffect(() => {
    if (notAvailableDays.length === 0) {
      const productoService = new ProductoService();
      productoService
        .getNotAvailableDays(productoId)
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

  const onChange = (dates) => {
    const [start, end] = dates;
    setStartDate(start);
    start ? setFechaDeCorte(getFechaDeCorte(start)) : setFechaDeCorte(null);
    setEndDate(end);
    onDateChange(start, end);
  };

  // Función para que re-renderice la página cuando cambia el ancho (viewport) y actualice el calendario para mostrar 1 o 2
  const min_width_Tablet = 768;
  const screenWidth = useWidthScreen();

  const capitalizeFirstLetter = (string) =>
    string.charAt(0).toUpperCase() + string.slice(1);

  const limpiar = () => {
    calendarRef.clear();
  };

  const disableSegunFechaInicialElegida = (date) => date <= fechaDeCorte;

  const getFechaDeCorte = (startDate) =>
    notAvailableDays.find((date) => date > startDate);

  return (
    <>
      <div className={`${stylesBooking.container} ${props.className}`}>
        <h2>Seleccioná tu fecha de reserva</h2>
        <div className={stylesBooking.calendar}>
          <Calendar
            calendarClassName={`calendar bookingCalendar`}
            inline
            locale="es"
            selected={false}
            dateFormat="dd/MM/yyyy"
            //   calendarClassName="calendar"
            monthsShown={`${screenWidth < min_width_Tablet ? "1" : "2"}`}
            excludeDates={notAvailableDays}
            formatWeekDay={(nameOfDay) =>
              capitalizeFirstLetter(nameOfDay.substr(0, 3))
            }
            //=====Navegación deshabilitada desde la fecha actual=====
            minDate={new Date()}
            //====== Para que no se resalte el día actual en todos los meses (se pierde accesibilidad) ======
            disabledKeyboardNavigation
            //====== Para que la semana empiece el domingo ======
            calendarStartDay={0}
            //====== Para que la selección del día no desaparezca al hacer hover en días anteriores al seleccionado ======
            selectsRange={true}
            startDate={startDate}
            endDate={endDate}
            onChange={onChange}
            filterDate={fechaDeCorte ? disableSegunFechaInicialElegida : null}
            ref={(datePicker) => setCalendarRef(datePicker)}
          >
            <div className="calendar vertical-line bookingCalendar" />
            <div
              className={`${stylesbuscador.calendar_footer} button_calendar_footer`}
            >
              <button
                className={`${stylesbuscador.calendar_button} booking_calendar_button`}
                onClick={limpiar}
              >
                Borrar
              </button>
            </div>
          </Calendar>
        </div>
      </div>
    </>
  );
};

export default BookingDateCalendar;
