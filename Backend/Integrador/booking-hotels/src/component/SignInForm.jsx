import React from "react";
import { Link, Redirect } from "react-router-dom";
import { useState } from "react";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEye, faEyeSlash } from "@fortawesome/free-regular-svg-icons";

//Js con una base de datos temporar solo para simulación de logueo
import dataUsuarios from "../dataUsuarios";

//Hooks
import useInput from "../hooks/useInput";

//CSS
import formStyles from "../styles/form.module.css";
import globalStyles from "../styles/global.module.css";
// import "../styles/form.module.css";
// import "../styles/global.module.css";

const SignInForm = () => {
	//Uso directo del useState para setear los errores que contengan los input según la acción del usuario
	//sobre los form:
	const [emailErr, setEmailErr] = useState({});
	const [passwordErr, setPasswordErr] = useState({});
	const [credentiaslErr, setCredentiaslErr] = useState({});

	// Llamada del hook useInput para el control del los valores ingresados en cada uno de los inputs de los formularios:
	const email = useInput("", setEmailErr);
	const password = useInput("", setPasswordErr);

	//Para manejo de esconder/mostrar password
	const [showPassword, setShowPassword] = useState(false);

	//Para chequear login exitoso
	const [loginExitoso, setLoginExitoso] = useState(false);

	//Manejo del evento submit, al hacerle click al botón "Ingresar".
	//Aquí se debe redireccionar a la pantalla de logueo:
	const handleSubmit = (event) => {
		event.preventDefault();
		const isValid = formValidation();

		if (isValid) {
			//console.log("Inicio de sesión exitoso");
		} else {
			//console.log("Fallo en formulario");
		}
	};

	//Función que valida si el campo del email no está vacío,
	//en caso que sí lo esté, setea el estado "emailErr" con el mensaje correspondiente
	const validateEmail = (email) => {
		const emailErr = {};
		let isValid = true;

		if (email === "") {
			emailErr.emptyEmailField = "Este campo es obligatorio";
			isValid = false;
		}

		setEmailErr(emailErr);
		return isValid;
	};

	//Función que valida si el campo del password no está vacío,
	//en caso que sí lo esté, setea el estado "passwordErr" con el mensaje correspondiente
	const validatePassword = (password) => {
		const passwordErr = {};
		let isValid = true;

		if (password === "") {
			passwordErr.emptyPasswordField = "Este campo es obligatorio";
			isValid = false;
		}

		setPasswordErr(passwordErr);
		return isValid;
	};

	function formValidation() {
		const credentiaslErr = {};
		let isValid = true;

		//Llamada a las funciones de validación de los campos email y password:
		isValid = validateEmail(email.value) && isValid;
		isValid = validatePassword(password.value) && isValid;

		//Condicional que compara los datos ingresados en los input con los valores
		//del objeto del archivo dataUsuarios.js creado solo para fines de comprobación:
		if (isValid) {
			const usuario = buscarUsuarioPorEmail();
			if (usuario && usuario.password === password.value) {
				//console.log("Iniciando sesión");
				sessionStorage.setItem("usuario", JSON.stringify(usuario));
				setLoginExitoso(true);
			} else {
				credentiaslErr.credentialsAuthenticationFail =
					"Por favor, vuelva a intentarlo, sus credenciales son inválidas";
				isValid = false;
				//console.log("Credenciales inválidas");
			}
		}

		setCredentiaslErr(credentiaslErr);
		return isValid;
	}

	function buscarUsuarioPorEmail() {
		const usuario = dataUsuarios.find(
			(usuario) => usuario.email === email.value
		);
		return usuario;
	}

	function toggleShowPassword(event) {
		setShowPassword((showPassword) => !showPassword);
	}

	return loginExitoso ? (
		<Redirect to="/" />
	) : (
		<div className={`${formStyles.form} ${formStyles.signinForm}`}>
			<h1>Iniciar Sesión</h1>
			<form
				method="post"
				onSubmit={handleSubmit}
				autoComplete="off"
				noValidate
				role="form"
			>
				<div className={formStyles.formGroup} id="errorCredentialsMsg">
					{/* Recuerden que el "credentiaslErr" se creó como un objeto vació en la función "formValidation()" 
          Esta función lo que hace es recorrer ese objeto y mostrar el mensaje del error correspondiente,
          por el momento solo hay uno pero cuando le agreguemos más validaciones habrán más mensajes de error(?)
          Es lo mismo con los otros que encuentren en el correo electrónico y la contraseña.
           */}

					{Object.keys(credentiaslErr).map((key) => {
						return (
							<div key={key} className={formStyles.errorCredentialsMsg}>
								{credentiaslErr[key]}
							</div>
						);
					})}
					<label htmlFor="email">
						<p>Correo Electrónico</p>
					</label>
					<input
						{...email}
						className={`
              ${formStyles.formControl}
              ${Object.keys(emailErr).length !== 0 ? formStyles.inputError : ""}
            `}
						type="email"
						name="email"
						id="email"
					></input>
					{Object.keys(emailErr).map((key) => {
						return (
							<div key={key} className={formStyles.errorMsg}>
								{emailErr[key]}
							</div>
						);
					})}
				</div>

				<div className={formStyles.formGroup}>
					<label htmlFor="password">
						<p>Contraseña</p>
					</label>
					<input
						{...password}
						className={`
              ${formStyles.formControl}
              ${
								Object.keys(passwordErr).length !== 0
									? formStyles.inputError
									: ""
							}
              ${!showPassword ? formStyles.achicarTamanioPuntitos : ""}
            `}
						type={showPassword ? "text" : "password"}
						name="password"
						id="password"
					/>
					{showPassword ? (
						<FontAwesomeIcon
							icon={faEye}
							onClick={toggleShowPassword}
							className={formStyles.iconoOjoNormal}
							data-testid="icono-ojo-normal"
						/>
					) : (
						// <i
						//   class="icono-ojo-normal far fa-eye"
						//   onClick={toggleShowPassword}
						// ></i>
						<FontAwesomeIcon
							icon={faEyeSlash}
							onClick={toggleShowPassword}
							className={formStyles.iconoOjoTachado}
							data-testid="icono-ojo-tachado"
						/>

						// <i
						//   class="icono-ojo-tachado far fa-eye-slash"
						//   onClick={toggleShowPassword}
						// ></i>
					)}
					{Object.keys(passwordErr).map((key) => {
						return (
							<div key={key} className={formStyles.errorMsg}>
								{passwordErr[key]}
							</div>
						);
					})}
				</div>

				<div className={formStyles.btnContainer}>
					<button
						id="btnIngresar"
						className={`${globalStyles.button} ${formStyles.buttonForm}`}
					>
						Ingresar
					</button>
					<div className={formStyles.signInLink}>
						<p>¿Aún no tienes cuenta?</p>
						<Link to="/register">Regístrate</Link>
					</div>
				</div>
			</form>
		</div>
	);
};

export default SignInForm;
