import React, { useState, useEffect } from "react";
import Button from "../Button";
import Redes from "../Redes";
import { useLocation } from "react-router-dom";

//CSS
import styles from "../../styles/index/nav.module.css";
import globalStyles from "../../styles/global.module.css";

function Nav(props) {
	const [usuario, setUsuario] = useState(null);
	const [mostrarMenu, setMostrarMenu] = useState(false);

	//para sacar props en location
	const location = useLocation();

	//manejo del ancho de pantalla
	const [width, setWidth] = useState(window.innerWidth);

	const handleResize = () => setWidth(window.innerWidth);

	useEffect(() => {
		window.addEventListener("resize", handleResize);
		return () => window.removeEventListener("resize", handleResize);
	}, []);

	const hideMenu = () => {
		setMostrarMenu(false);
		document.body.classList.remove(globalStyles["no-scroll"]);
	};

	const showMenu = () => {
		setMostrarMenu(true);
		document.body.classList.add(globalStyles["no-scroll"]);
	};

	function checkIfLoggedIn() {
		const usuarioLogueado = JSON.parse(sessionStorage.getItem("usuario"));
		if (usuario === null) {
			if (usuarioLogueado !== null) setUsuario(usuarioLogueado);
		} else {
			if (usuarioLogueado === null) setUsuario(usuarioLogueado);
		}
	}

	function cerrarSesion() {
		sessionStorage.removeItem("usuario");
		setUsuario(null);
	}

	checkIfLoggedIn();

	return (
		<nav className={styles.nav}>
			<div
				className={`
              ${styles["atenuar-fondo-hidden"]}
              ${mostrarMenu ? styles["atenuar-fondo-show"] : ""}
            `}
				onClick={hideMenu}
			/>
			{/* boton hamburguesa */}
			<div>
				<button type="button" className={styles.btnMenu} onClick={showMenu}>
					<i id="hamburguesaIcon" className="fas fa-bars"></i>
				</button>
			</div>

			{/* menu desplegado */}
			<div className={`${styles.menu} ${mostrarMenu ? styles.show : ""}`}>
				{/* encabezado menu*/}
				<div
					className={`${
						!usuario && width >= 768
							? `${styles.menuHeader} ${styles.hidden}`
							: styles.menuHeader
					}`}
				>
					<div
						type="button"
						role="button"
						className={`${
							!usuario || width >= 768
								? `${styles.btnClose} ${styles.hidden}`
								: styles.btnClose
						}`}
						onClick={hideMenu}
					>
						<i className="fas fa-times"></i>
					</div>
					<div
						type="button"
						role="button"
						className={`${
							usuario && width >= 768
								? `${styles.btnCloseSession} ${styles.show}`
								: styles.btnCloseSession
						}`}
						onClick={cerrarSesion}
					>
						<i className="fas fa-times"></i>
					</div>
					{getMenuHeader()}
				</div>

				{/* cuerpo menu*/}
				<div
					className={`${
						usuario && width >= 768
							? `${styles.menuBody} ${styles.hidden}`
							: styles.menuBody
					}`}
				>
					{getMenuBody(location.pathname)}
					<Redes className={styles.iconoRedes} />
				</div>
			</div>
		</nav>
	);

	function getMenuHeader() {
		if (!usuario)
			return (
				<>
					{/* {setLog(true)} */}
					<h5 className={styles.textMenu}>MENÚ</h5>
				</>
			);
		else {
			return (
				<>
					{/* {setLog(false)} */}
					<div className={styles.headerLoggedIn}>
						<div className={styles.avatar}>{`${usuario.nombre.charAt(
							0
						)}${usuario.apellido.charAt(0)}`}</div>
						<div>
							<h5 className={styles.hello}>Hola,</h5>
							<h5
								className={styles.userName}
							>{`${usuario.nombre} ${usuario.apellido}`}</h5>
						</div>
					</div>
				</>
			);
		}
	}

	function getMenuBody(currentPath) {
		/*
      validacion del estado del log del usuario y de ruta para visualizar el menu

      si login = false y esta en el page=home           -> mostrar el #1
      si login = false y se encuentra en la page=signup -> mostrar el #2
      si login = false y se encuentra en la page=login  -> mostrar el #3
      si login = true                                   -> mostrar el #4
    */

		const menu1 = (
			<div className={styles.link}>
				<div className={styles.menuLink}>
					<Button
						id="btnCrearCuenta"
						className={styles.btnLinkMenu}
						to="/register"
						aria-current="page"
						onClick={hideMenu}
						text="Crear cuenta"
					/>
				</div>

				<div className={styles.borde}></div>

				<div className={styles.menuLink}>
					<Button
						id="btnIniciar"
						className={styles.btnLinkMenu}
						to="/login"
						aria-current="page"
						onClick={hideMenu}
						text="Iniciar sesión"
					/>
				</div>
			</div>
		);

		const menu2 = (
			<ul className={styles.link}>
				<li className={styles.menuLink}>
					<Button
						className={styles.btnLinkMenu}
						to="/login"
						ariaCurrent="page"
						onClick={hideMenu}
						text="Iniciar sesión"
					/>
				</li>
			</ul>
		);

		const menu3 = (
			<ul className={styles.link}>
				<li className={styles.menuLink}>
					<Button
						className={styles.btnLinkMenu}
						to="/register"
						aria-current="page"
						onClick={hideMenu}
						text="Crear cuenta"
					/>
				</li>
			</ul>
		);

		const menu4 = (
			<div className={styles.menu4}>
				<div className={styles.logout}>
					¿Deseas <span onClick={cerrarSesion}>cerrar sesión</span>?
				</div>
				<div>
					<div className={styles.bordeLogout}></div>
				</div>
			</div>
		);

		if (!usuario) {
			if (currentPath === "/register") return menu2;
			else if (currentPath === "/login") return menu3;
			else return menu1;
		} else return menu4;
	}
}

export default Nav;
