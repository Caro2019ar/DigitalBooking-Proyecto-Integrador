import { useState } from "react";
// import { Link, Redirect } from "react-router-dom";

// Hooks
import useInput from "../../hooks/useInput";

//css
import formStyles from "../../styles/forms/form.module.css";
import formBooking from "../../styles/bookingPage/formBooking.module.css";

const FormBooking = (props) => {

	const [city, setCity] = useState("");
	const [infoCovid, setInfoCovid] = useState("");

	const handleCityChange = (event) => {
		setCity(event.target.value);
		props.onCityChange(event.target.value);
	};

	const handleInfoCovidChange = (event) => {
		setInfoCovid(event.target.value);
		props.onInfoCovidChange(event.target.value);
	};

	const handleVacunadoCovidChange = (event) => props.onVacunadoCovidChange(event.target.value);


	return (
		<div
			className={`${formStyles.form} ${formBooking.container} ${props.className}`}
		>
			<h2>Completá tus datos</h2>
			<form
				method="post"
				/*onSubmit={handleSubmit}*/
				autoComplete="on"
				className={formBooking.form}
			>
				<div
					className={`${formStyles.formGroup} ${formStyles.inputDesktopGroup} ${formBooking.formGroup}`}
				>
					<div
						className={`${formStyles.formSubGroup} ${formBooking.formSubGroup}`}
					>
						<label htmlFor="firstName" className={formBooking.label}>
							<p>Nombre</p>
						</label>
						<input
							className={`${formStyles.formControl} ${formBooking.formDisabled}`}
							type="text"
							name="firstName"
							id="firstName"
							placeholder={props.usuario.nombre}
							disabled
						></input>
					</div>
					<div
						className={`${formStyles.formSubGroup} ${formBooking.formSubGroup}`}
					>
						<label htmlFor="lastName" className={formBooking.label}>
							<p>Apellido</p>
						</label>
						<input
							className={`${formStyles.formControl} ${formBooking.formDisabled}`}
							type="text"
							name="lastName"
							id="lastName"
							placeholder={props.usuario.apellido}
							disabled
						></input>
					</div>
					<div
						className={`${formStyles.formSubGroup} ${formBooking.formSubGroup}`}
					>
						<label htmlFor="email" className={formBooking.label}>
							<p>Correo electrónico</p>
						</label>
						<input
							className={`${formStyles.formControl} ${formBooking.formDisabled}`}
							type="email"
							name="email"
							id="email"
							placeholder={props.usuario.email}
							disabled
						/>
					</div>
					<div
						className={`${formStyles.formSubGroup} ${formBooking.formSubGroup}`}
					>
						<label htmlFor="city" className={formBooking.label}>
							<p>Ciudad</p>
						</label>
						<input
							value={city}
							className={`${formStyles.formControl} ${formBooking.formActive}`}
							type="text"
							name="city"
							id="city"
							placeholder="Ingrese su ciudad..."
							onChange={handleCityChange}
						></input>
					</div>
					<div
						className={`${formStyles.formSubGroup} ${formBooking.formSubGroup}`}
					>
						<label htmlFor="firstName" className={formBooking.label}>
							<p>Información COVID</p>
						</label>
						<p className={formBooking.text}>
							Datos de interés para el vendedor
						</p>
						<textarea
							value={infoCovid}
							name="infoCovid"
							id="infoCovid"
							placeholder="Información importante relacionada a COVID-19..."
							className={`${formStyles.formControl} ${formBooking.formActive}`}
							onChange={handleInfoCovidChange}
						></textarea>
					</div>
					<div
						className={`${formStyles.formSubGroup} ${formBooking.formSubGroup}`}
					>
						<p className={`${formBooking.text} ${formBooking.vaccine}`}>
							¿Está vacunado contra el COVID-19?
						</p>
						<input
							className={formBooking.radioButton}
							value={true}
							type="radio"
							name="vacuna"
							id="vacunaYes"
							onChange={handleVacunadoCovidChange}
						/>
						<label htmlFor="vaccineYes" className={formBooking.labelRadio}>
							Sí
						</label>
						<input
							className={formBooking.radioButton}
							value={false}
							type="radio"
							name="vacuna"
							id="vacunaNo"
							onChange={handleVacunadoCovidChange}
						/>
						<label htmlFor="vaccineNo" className={formBooking.labelRadio}>
							No
						</label>
					</div>
				</div>
			</form>
		</div>
	);
};

export default FormBooking;
