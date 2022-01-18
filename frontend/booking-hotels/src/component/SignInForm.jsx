import React from "react";
import { Link, Redirect, useLocation } from "react-router-dom";
import { useState, useEffect } from "react";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEye, faEyeSlash } from "@fortawesome/free-regular-svg-icons";
import {
  faCheckCircle,
  faExclamationCircle,
} from "@fortawesome/free-solid-svg-icons";
import { UsuarioService } from "../Service/UsuarioService";
import { ClienteService } from "../Service/ClienteService";


//Hooks
import useInput from "../hooks/useInput";

//CSS
import formStyles from "../styles/forms/form.module.css";
import globalStyles from "../styles/global.module.css";
// import "../styles/form.module.css";
// import "../styles/global.module.css";

const SignInForm = ({ usuario, onIniciarSesion }) => {
	 
	//Uso directo del useState para setear los errores que contengan los input según la acción del usuario
	//sobre los form:
	const [emailErr, setEmailErr] = useState({});
	const [passwordErr, setPasswordErr] = useState({});
	const [loginErr, setLoginErr] = useState("");

	// Llamada del hook useInput para el control del los valores ingresados en cada uno de los inputs de los formularios:
	const email = useInput("", setEmailErr);
	const password = useInput("", setPasswordErr);

	//Para manejo de esconder/mostrar password
	const [showPassword, setShowPassword] = useState(false);

	//Para chequear login exitoso
	const [loginExitoso, setLoginExitoso] = useState(false);

	//Para la confirmación de la cuenta del usuario
	const [confirmacionSuccessMsg, setConfirmacionSuccessMsg] = useState("");
	const [confirmacionFailMsg, setConfirmacionFailMsg] = useState("");

	const location = useLocation();
	const searchParams = new URLSearchParams(location.search);
	//Manejo de la confirmación de cuenta
	const confirmacion = searchParams.has("confirmacionToken")
	//Manejo del error de intento de reserva sin login
	const warningFromBooking = searchParams.has("from-booking");


	useEffect( () => {

		if (confirmacion)
		{
			const clienteService = new ClienteService();
			clienteService.confirmacionCuenta(searchParams.get("confirmacionToken"))
			.then( data => {
				if (data.message === "OK")
					setConfirmacionFailMsg("");
					setConfirmacionSuccessMsg("Su cuenta ha sido verificada.\nYa puede ingresar al sitio.");
			})
			.catch( e => {
				setConfirmacionSuccessMsg("");
				if (e.response)
					setConfirmacionFailMsg(e.response.data.error);
				else
					setConfirmacionFailMsg("Su cuenta no pudo ser verificada.\nPor favor, intente más tarde.");
			})
		}

	  }, []);



	//Manejo del evento submit, al hacerle click al botón "Ingresar"
	const handleSubmit = async (event) => {

		event.preventDefault();
		setLoginErr("");
		const isValid = formValidation();

		let usuario = null;
		if (isValid)
			usuario = await chequeosBackend();

		if (usuario)
		{
			const dataUsuario = {
				username: email.value,
				password: password.value,
			};

			const usuarioService = new UsuarioService();
	
			usuarioService.login(dataUsuario)
			.then( data => {
				const usuarioStorage = {
					id: usuario.id,
					email: usuario.email,
					nombre: usuario.nombre,
					apellido: usuario.apellido,
					rol: usuario.rol.nombre
				}
				onIniciarSesion(usuarioStorage, data.token);
				setLoginExitoso(true);
			})
			.catch( e => {
				if (e.response)
					setLoginErr(e.response.data.error);
				else
					setLoginErr("Lamentablemente no ha podido iniciar sesión.\nPor favor intente más tarde.");
			})
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

	function formValidation()
	{
		let isValid = true;

		//Llamada a las funciones de validación de los campos email y password:
		isValid = validateEmail(email.value) && isValid;
		isValid = validatePassword(password.value) && isValid;

		return isValid;
	}

	async function chequeosBackend() {

		let usuario = null;

		// Valido los datos ingresados con el backend
		const usuarioService = new UsuarioService();

		await usuarioService.validateUserLogin({ 
			email: email.value,
			contrasena: password.value
		})
		.then( data => {
			usuario = data;
		})
		.catch( e => {
			if (e.response)
				setLoginErr(e.response.data.error);
			else
				setLoginErr("Lamentablemente no ha podido iniciar sesión.\nPor favor intente más tarde.");
		})
	
		return usuario;
	}
      
	const toggleShowPassword = () => setShowPassword( (showPassword) => !showPassword);

	
	if (usuario)
		return <Redirect to="/" />;

	return loginExitoso ? (
		<Redirect to="/" />
	) : (
		
		<div className={`${formStyles.form} ${formStyles.signinForm}`}>

			<div className={`
				${formStyles['fail-message']}
				${warningFromBooking ? "" : formStyles.hide}`}>
				<FontAwesomeIcon
					icon={faExclamationCircle}
					className={formStyles["fail-icono"]}
					data-testid="fail-icono"
				/>
				<h3 className={formStyles["fail-texto"]}>
					Para realizar una reserva es obligatorio estar logueado.
				</h3>
			</div>

			<div className={`
				${formStyles['success-message']}
				${confirmacionSuccessMsg ? "" : formStyles.hide}`}>
				<FontAwesomeIcon
					icon={faCheckCircle}
					className={formStyles["success-icono"]}
					data-testid="success-icono"
				/>
				<h3 className={formStyles["success-texto"]}>
					{confirmacionSuccessMsg}
				</h3>
			</div>
			
			<div className={`
				${formStyles['fail-message']}
				${confirmacionFailMsg ? "" : formStyles.hide}`}>
				<FontAwesomeIcon
					icon={faExclamationCircle}
					className={formStyles["fail-icono"]}
					data-testid="fail-icono"
				/>
				<h3 className={formStyles["fail-texto"]}>
					{confirmacionFailMsg}
				</h3>
			</div>

			<h1>Iniciar Sesión</h1>
			<form
				method="post"
				onSubmit={handleSubmit}
				autoComplete="off"
				noValidate
				role="form"
			>
				<div className={formStyles.formGroup}>
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
			${Object.keys(passwordErr).length !== 0 ? formStyles.inputError : ""}
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

        <div
          className={`${formStyles.errorMsg} ${formStyles.errorRegistroMsg}`}
        >
          {loginErr}
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
