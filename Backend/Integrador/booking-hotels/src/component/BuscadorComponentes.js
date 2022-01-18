import React, { useState, useLayoutEffect } from "react";
import DatePicker, { registerLocale } from "react-datepicker";
import Select, { components } from "react-select";
import { CityService } from "../Service/CityService";
import es from "date-fns/locale/es";
import axios from 'axios';
import { useEffect } from "react";
// import options from "../dataCountries";
//CSS
import "react-datepicker/dist/react-datepicker.css";
import stylesbuscador from "../styles/index/buscador.module.css";
import "../styles/index/datepicker.css";
import "../styles/global.module.css";
import { styles } from "ansi-colors";
registerLocale("es", es);

/* ================================================= */
/* ========== Select de Paises y ciudades ========== */
/* ================================================= */

const selectFormatOptionLabel = ({ value, label }) => (
  <div className={stylesbuscador.select_option_list}>
    <i
      className={`far fa-map-marker-alt fa-lg ${stylesbuscador.select_icono_mapa_vacio}`}
      aria-hidden="true"
    />
    <div className={stylesbuscador.select_option_list_caja}>
      <div className={stylesbuscador.select_option_list_caja_pais}>
        {label.split(",")[0]}
      </div>
      <div className={stylesbuscador.select_option_list_caja_ciudad}>
        {label.split(",")[1]}
      </div>
    </div>
  </div>
);

export const SelectMenu = ({ parentCallback, selectedCity }) => {

  const [options, setOptions] = useState([])

  useEffect(() => {
    const cityService = new CityService();
    console.log(cityService);
    const prom = cityService.getAllCity();
    console.log(prom);
    prom.then(data => setOptions(listadoEnFormatoSelect(data))).catch( (e) => console.log(e))
  },[])

  function listadoEnFormatoSelect(dataCiudades)
  {
    const listadoCiudades = [];
    for (let i = 0; i < dataCiudades.length; i++)
    {
      listadoCiudades.push({
        value: i + 1,
        label: `${dataCiudades[i].nombre}, ${dataCiudades[i].pais}`,
      });
    }
    return listadoCiudades;
  }

  const handleChange = e => parentCallback(e.label.split(",")[0]);

  return (
  
    <div className={stylesbuscador.select_color}>
      <div
        className={
          selectedCity === ""
            ? `${stylesbuscador.oculta}`
            : `${stylesbuscador.selected_city}`
        }
        onClick={() => parentCallback("")}
      >
        <i
          className={`fas fa-map-marker-alt fa-lg ${stylesbuscador.selected_icono_mapa_relleno}`}
          aria-hidden="true"
        />
        {options.map((option) =>
          option.label.split(",")[0] === selectedCity ? option.label : ""
        )}
      </div>
      <Select
        
        value={selectedCity || ""}
        formatOptionLabel={selectFormatOptionLabel}
        styles={selectStyles}
        components={{ ValueContainer }}
        placeholder={"¿A dónde vamos?"}
        className={
          selectedCity
            ? `${stylesbuscador.oculta}`
            : `${stylesbuscador.select_container}`
        }
        
        options={options}
        onChange={handleChange}
        theme={(theme) => ({
          ...theme,
          colors: {
            ...theme.colors,
            neutral80: "gray",
          },
        })}
      />
    </div>
  );
};
// ====== Función para configurar el ícono de Mapa en el placeholder del Select ======
const ValueContainer = ({ children, ...props }) => {
	return (
		components.ValueContainer && (
			<components.ValueContainer {...props}>
				{!!children && (
					<i
						className={`fas fa-map-marker-alt ${stylesbuscador.icono_select}`}
						aria-hidden="true"
					/>
				)}
				{children}
			</components.ValueContainer>
		)
	);
};
// ====== Estilo para dar margin a la izquierda del ícono del mapa en el placeholder del Select ======
const selectStyles = {
	valueContainer: (base) => ({
		...base,
		paddingTop: 4,
		paddingBottom: 2,
		paddingLeft: 44,
		cursor: "text",
	}),
	placeholder: (base) => ({
		...base,
		color: "#7F7F7F",
	}),
};

/* ================================================= */
/* ========= Check-in Check-out y Calendar ========= */
/* ================================================= */

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

export const Calendar = ({ pull_date, dateRange }) => {
	const [startDate, endDate] = dateRange;
	const [myRef, setMyRef] = useState(null);
	const [screenWidth] = useWindowSize();
	const handlePullDate = (update) => {
		pull_date(update);
	};
	//====== Función del botón Aplicar para cerrar el Calendario ======
	const closeCalendar = () => {
		myRef.setOpen(false);
	};
	//console.log(startDate);
	// props.func(startDate, endDate);
	return (
		<div data-test="calendar" className={stylesbuscador.calendar_container}>
			<div>
				<i
					className={`far fa-calendar-day ${stylesbuscador.icono_calendar} ${
						!!startDate ? stylesbuscador.fecha_elegida : ""
					}`}
				/>
			</div>
			<DatePicker
				customInput={<CustomInput fechaElegida={!!startDate} />}
				popperPlacement="bottom-end"
				locale="es"
				dateFormat={"dd" + " 'de' " + "MMM."}
				formatWeekDay={(nameOfDay) =>
					capitalizeFirstLetter(nameOfDay.substr(0, 3))
				}
				monthsShown={`${screenWidth < min_width_Tablet ? "1" : "2"}`}
				minDate={new Date()}
				placeholderText="Check in - Check out"
				/*closeOnScroll={true}*/
				//====== Para que no se resalte el día actual en todos los meses (se pierde accesibilidad) ======
				disabledKeyboardNavigation
				//====== Para que la semana empiece el domingo ======
				calendarStartDay={0}
				//====== Para que la la selección del día no desaparezca al hacer hover en días anteriores al seleccionado ======
				selected={startDate}
				selectsRange={true}
				startDate={startDate}
				endDate={endDate}
				onChange={handlePullDate}
				//====== Para que no se cierre el calendario al elegir el check-out: ======
				shouldCloseOnSelect={false}
				//====== Referencia para función del botón Aplicar en el calendario ======
				ref={(r) => {
					setMyRef(r);
				}}
			>
				<div className="vertical-line" />
				<div className={stylesbuscador.calendar_footer}>
					<button
						className={stylesbuscador.calendar_button}
						onClick={closeCalendar}
					>
						Aplicar
					</button>
				</div>
			</DatePicker>
		</div>
	);
};

const capitalizeFirstLetter = (string) =>
	string.charAt(0).toUpperCase() + string.slice(1);

/* Para poder cambiarle el color al input al seleccionar una fecha */

const CustomInput = React.forwardRef(
	({ onChange, placeholder, value, id, onClick, fechaElegida }, ref) => (
		<input
			type="text"
			className={fechaElegida ? stylesbuscador.fecha_elegida : ""}
			onChange={onChange}
			placeholder={placeholder}
			value={value}
			id={id}
			onClick={onClick}
			ref={ref}
		/>
	)
);
