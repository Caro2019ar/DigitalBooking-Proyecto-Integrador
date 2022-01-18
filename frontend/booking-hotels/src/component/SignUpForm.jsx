import { useState } from "react";
import { Link, Redirect } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEye, faEyeSlash } from "@fortawesome/free-regular-svg-icons";
import { ClienteService } from "../Service/ClienteService";
import dataUsuarios from "../dataUsuarios";

// Hooks
import useInput from "../hooks/useInput";

// CSS
import globalStyles from "../styles/global.module.css";
import formStyles from "../styles/forms/form.module.css";


const SignUpForm = ({ usuario }) => {

	//Uso directo del useState para setear los errores que contengan los input según la acción del usuario
	//sobre los form:
	const [firstNameErr, setFirstNameErr] = useState({});
	const [lastNameErr, setLastNameErr] = useState({});
	const [emailErr, setEmailErr] = useState({});
	const [passwordErr, setPasswordErr] = useState({});
	const [passwordRepeatErr, setPasswordRepeatErr] = useState({});

	// Llamada del hook useInput para el control del los valores ingresados en cada uno de los inputs de los formularios
	const firstName = useInput("", setFirstNameErr);
	const lastName = useInput("", setLastNameErr);
	const email = useInput("", setEmailErr);
	const password = useInput("", setPasswordErr);
	const passwordRepeat = useInput("", setPasswordRepeatErr);

	//Para manejo de esconder/mostrar password
	const [showPassword, setShowPassword] = useState(false);

	//Para redirección a login con registro exitoso
	const [registroExitoso, setRegistroExitoso] = useState(false);

	//Para errores en el llamado a la API de registro de clientes
	const [registroErr, setRegistroErr] = useState("");


	const handleSubmit = (event) => {

		event.preventDefault();
		setRegistroErr("");
		const isValid = formValidation();

		if (isValid)
			crearCuenta(event);
	};

	function crearCuenta(event)
	{
		const dataUsuario = {
			nombre: firstName.value,
			apellido: lastName.value,
			email: email.value,
			contrasena: password.value,
		};

		const clienteService = new ClienteService();

		clienteService.registrarCliente(dataUsuario)
		.then( data => {
			setRegistroExitoso(true)
		})
		.catch( e => {
			if (e.response) setRegistroErr(e.response.data.error)
			else setRegistroErr("Lamentablemente no ha podido registrarse.\nPor favor intente más tarde.")
		})
	}

	const validateNameRegex = (name) => {
		const nameRegex = /^[a-záàâãéèêíïóôõöúçñ' ]+$/i;
		const nameResult = nameRegex.test(name);
		return nameResult;
	};

	// const validateEmail = (email) => {
	//   const emailRGEX = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	//   const emailResult = emailRGEX.test(email);
	//   return emailResult;
	// };

	const validateEmailRegex = (email) => {
		const emailRegex =
			/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
		const emailResult = emailRegex.test(email);
		return emailResult;
	};

	const formValidation = () => {
		const firstNameErr = {};
		const lastNameErr = {};
		const emailErr = {};
		const passwordErr = {};
		const passwordRepeatErr = {};
		let isValid = true;

		//Validate first name

		if (firstName.value === "") {
			firstNameErr.emptyNameField = "Este campo es obligatorio";
			isValid = false;
		} else if (firstName.value.trim().length < 3) {
			firstNameErr.firstNameIsTooShort =
				"El nombre es muy corto, debe tener al menos 3 caracteres";
			isValid = false;
		} else if (validateNameRegex(firstName.value) === false) {
			firstNameErr.firstNameNotAllowedCharacters =
				"El nombre no debe contener números ni caracteres especiales";
			isValid = false;
		}

		//Validate last name

		if (lastName.value === "") {
			lastNameErr.emptyNameField = "Este campo es obligatorio";
			isValid = false;
		} else if (lastName.value.trim().length < 3) {
			lastNameErr.lastNameIsTooShort =
				"El apellido es muy corto, debe tener al menos 3 caracteres";
			isValid = false;
		} else if (validateNameRegex(lastName.value) === false) {
			lastNameErr.lastNameNotAllowedCharacters =
				"El apellido no debe contener números ni caracteres especiales";
			isValid = false;
		}

		//Validate email

		if (email.value === "") {
			emailErr.emptyEmailField = "Este campo es obligatorio";
			isValid = false;
		} else if (validateEmailRegex(email.value) === false) {
			emailErr.emailCharactersVerification = "El email ingresado no es válido";
			isValid = false;
		}

		//Validate password

		if (password.value === "") {
			passwordErr.emptyPasswordField = "Este campo es obligatorio";
			isValid = false;
		} else if (password.value.length < 6) {
			passwordErr.shortPasswordField =
				"La contraseña debe contener al menos 6 caracteres";
			isValid = false;
		}

		//Validate repeat password

		if (passwordRepeat.value === "") {
			passwordRepeatErr.emptyPasswordRepeatField = "Este campo es obligatorio";
			isValid = false;
		} else if (passwordRepeat.value.length < 6) {
			passwordRepeatErr.shortPasswordRepeatField =
				"La contraseña debe contener al menos 6 caracteres";
			isValid = false;
		} else if (passwordRepeat.value !== password.value) {
			passwordRepeatErr.passwordsDontMatch =
				"Las contraseñas ingresadas no coinciden";
			isValid = false;
		}

		setFirstNameErr(firstNameErr);
		setLastNameErr(lastNameErr);
		setEmailErr(emailErr);
		setPasswordErr(passwordErr);
		setPasswordRepeatErr(passwordRepeatErr);

		return isValid;
	};

	function toggleShowPassword() {
		setShowPassword( showPassword => !showPassword )
	}

	
	if (usuario)
		return <Redirect to="/" />;

	return registroExitoso ? (
		<Redirect to="/registro-exito" />
	) : (
		<div className={formStyles.form}>
			<h1>Crear cuenta</h1>
			<form
				method="post"
				onSubmit={handleSubmit}
				autoComplete="off"
				noValidate
				role="form"
			>
				<div
					className={`${formStyles.formGroup} ${formStyles.inputDesktopGroup}`}
				>
					<div className={formStyles.formSubGroup}>
						<label htmlFor="firstName">
							<p>Nombre</p>
						</label>
						<input
							{...firstName}
							className={`
                ${formStyles.formControl}
                ${
                  Object.keys(firstNameErr).length !== 0
                    ? formStyles.inputError
                    : ""
                }
              `}
              type="text"
              name="firstName"
              id="firstName"
            ></input>
            {Object.keys(firstNameErr).map((key) => {
              return (
                <div key={key} className={formStyles.errorMsg}>
                  {firstNameErr[key]}
                </div>
              );
            })}
          </div>

          <div className={formStyles.formSubGroup}>
            <label htmlFor="lastName">
              <p>Apellido</p>
            </label>
            <input
              {...lastName}
              className={`
                ${formStyles.formControl}
                ${
                  Object.keys(lastNameErr).length !== 0
                    ? formStyles.inputError
                    : ""
                }
              `}
              type="text"
              name="lastName"
              id="lastName"
            />
            {Object.keys(lastNameErr).map((key) => {
              return (
                <div key={key} className={formStyles.errorMsg}>
                  {lastNameErr[key]}
                </div>
              );
            })}
          </div>
        </div>

        <div className={formStyles.formGroup}>
          <label htmlFor="email">
            <p>Correo electrónico</p>
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
          />
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
            //   className={`${formStyles.iconoOjoNormal} far fa-eye`}
            //   onClick={toggleShowPassword}
            // ></i>
            <FontAwesomeIcon
              icon={faEyeSlash}
              onClick={toggleShowPassword}
              className={formStyles.iconoOjoTachado}
              data-testid="icono-ojo-tachado"
            />
            // <i
            //  className={`${formStyles.iconoOjoTachado} far fa-eye`}
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

        <div className={formStyles.formGroup}>
          <label htmlFor="passwordRepeat">
            <p>Confirmar contraseña</p>
          </label>
          <input
            {...passwordRepeat}
            className={`
              ${formStyles.formControl}
              ${
                Object.keys(passwordRepeatErr).length !== 0
                  ? formStyles.inputError
                  : ""
              }
              ${!showPassword ? formStyles.achicarTamanioPuntitos : ""}
            `}
            type={showPassword ? "text" : "password"}
            name="password-repeat"
            // required
            id="passwordRepeat"
          />
          {/* {showPassword ? (
            <i
              className="icono-ojo-normal far fa-eye"
              onClick={toggleShowPassword}
            ></i>
          ) : (
            <i
              className="icono-ojo-tachado far fa-eye-slash"
              onClick={toggleShowPassword}
            ></i>
          )} */}
					{Object.keys(passwordRepeatErr).map((key) => {
						return (
							<div key={key} className={formStyles.errorMsg}>
								{passwordRepeatErr[key]}
							</div>
						);
					})}
				</div>

				<div className={`${formStyles.errorMsg} ${formStyles.errorRegistroMsg}`}>
					{registroErr}
				</div>

				<div className={formStyles.btnContainer}>
					<button className={`${globalStyles.button} ${formStyles.buttonForm}`}>
						<p id="btnCrearCuentaSign">Crear Cuenta</p>
					</button>
					<div className={formStyles.signInLink}>
						<p>¿Ya tienes una cuenta?</p>
						<Link to="/login">Iniciar sesión</Link>
					</div>
				</div>
			</form>
		</div>
	);
};

export default SignUpForm;
