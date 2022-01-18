import React, { useState, useEffect } from "react";
import Button from "../componentGlobal/Button";
import Redes from "../componentGlobal/Redes";
import { useLocation, Link } from "react-router-dom";
import useWidthScreen from "../../hooks/useWidthScreen";

//CSS
import globalStyles from "../../styles/global.module.css";
import styles from "../../styles/index/nav.module.css";

function Nav({ usuario, onCerrarSesion }) {
  //para sacar props en location
  const location = useLocation();
  const [userAdmin, setUserAdmin] = useState(false);

  const widthScreen = useWidthScreen();
  const widthWindowTablet = 768;
  const [mostrarMenu, setMostrarMenu] = useState(false);

  const authorizedRole = "ROLE_ADMIN";

  useEffect(() =>
    !usuario || usuario.rol === authorizedRole
      ? setUserAdmin(true)
      : setUserAdmin(false)
  );

  const hideMenu = () => {
    setMostrarMenu(false);
    document.body.classList.remove(globalStyles["no-scroll"]);
  };

  const showMenu = () => {
    setMostrarMenu(true);
    document.body.classList.add(globalStyles["no-scroll"]);
  };

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
            !usuario && widthScreen >= widthWindowTablet
              ? `${styles.menuHeader} ${styles.hidden}`
              : styles.menuHeader
          }`}
        >
          <div
            type="button"
            role="button"
            className={`${
              !usuario || widthScreen >= widthWindowTablet
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
              usuario && widthScreen >= widthWindowTablet
                ? `${styles.btnCloseSession} ${styles.show}`
                : styles.btnCloseSession
            }`}
            onClick={onCerrarSesion}
          >
            <i className="fas fa-times"></i>
          </div>
          {getMenuHeader()}
        </div>

        {/* cuerpo menu*/}
        <div
          className={`${
            usuario && widthScreen >= widthWindowTablet
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
            {userAdmin ? (
              <>
                <div className={styles.link}>
                  <div className={styles.menuLink}>
                    <Button
                      id="btnAdmin"
                      className={`${styles.btnLinkMenu} ${styles.adminHeader}`}
                      to="/admin/newProduct"
                      aria-current="page"
                      onClick={hideMenu}
                      text="Administración"
                    />
                  </div>
                </div>
                <hr className={styles.verticalLine} />
                <div className={styles.avatar}>{`${usuario.nombre.charAt(
                  0
                )}${usuario.apellido.charAt(0)}`}</div>
                <div>
                  <h5 className={styles.hello}>Hola,</h5>
                  <h5
                    className={styles.userName}
                  >{`${usuario.nombre} ${usuario.apellido}`}</h5>
                </div>
              </>
            ) : (
              <>
                <ul className={styles.link}>
                  <li className={styles.menuLink}>
                    <Button
                      id="btnMyBooking"
                      className={`${styles.btnLinkMenu} ${styles.bookingHeader}`}
                      to="/my-reservations"
                      aria-current="page"
                      onClick={hideMenu}
                      text="Mis reservas"
                    />
                  </li>
                </ul>
                <ul className={styles.link}>
                  <li className={styles.menuLink}>
                    <Button
                      id="btnMyFavorite"
                      className={`${styles.btnLinkMenu} ${styles.favoriteHeader}`}
                      to={"/favorite"}
                      aria-current="page"
                      onClick={hideMenu}
                      text="Mis favoritos"
                    />
                  </li>
                </ul>
                <ul className={styles.link}>
                  <li className={styles.menuLink}>
                    <Button
                      id="btnMyScore"
                      className={`${styles.btnLinkMenu} ${styles.scoreHeader}`}
                      to="/my-reviews"
                      aria-current="page"
                      onClick={hideMenu}
                      text="Mis valoraciones"
                    />
                  </li>
                </ul>
                <hr className={styles.verticalLine} />
                <div className={styles.avatar}>{`${usuario.nombre.charAt(
                  0
                )}${usuario.apellido.charAt(0)}`}</div>
                <div>
                  <h5 className={styles.hello}>Hola,</h5>
                  <h5
                    className={styles.userName}
                  >{`${usuario.nombre} ${usuario.apellido}`}</h5>
                </div>
              </>
            )}
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

    const menu4 = userAdmin ? (
      <>
        <ul className={styles.link}>
          <li className={styles.menuLink}>
            <Button
              id="btnFavorite"
              className={`${styles.btnLinkMenu} ${styles.favorite}`}
              to="/admin/newProduct"
              aria-current="page"
              onClick={hideMenu}
              text="Administración"
            />
          </li>
        </ul>
        <div className={styles.menu4}>
          <div className={styles.logout}>
            ¿Deseas <span onClick={onCerrarSesion}>cerrar sesión</span>?
          </div>
          <div>
            <div className={styles.bordeLogout}></div>
          </div>
        </div>
      </>
    ) : (
      <>
        <ul className={styles.link}>
          <li className={styles.menuLink}>
            <Button
              id="btnMyBooking"
              className={`${styles.btnLinkMenu} ${styles.booking}`}
              to="/my-reservations"
              aria-current="page"
              onClick={hideMenu}
              text="Mis reservas"
            />
          </li>
        </ul>
        <div className={styles.borde}></div>
        <ul className={styles.link}>
          <li className={styles.menuLink}>
            <Button
              id="btnMyFavorite"
              className={`${styles.btnLinkMenu} ${styles.favorite}`}
              to="/favorite"
              aria-current="page"
              onClick={hideMenu}
              text="Mis favoritos"
            />
          </li>
        </ul>
        <div className={styles.borde}></div>
        <ul className={styles.link}>
          <li className={styles.menuLink}>
            <Button
              id="btnMyScore"
              className={`${styles.btnLinkMenu} ${styles.score}`}
              to="/my-reviews"
              aria-current="page"
              onClick={hideMenu}
              text="Mis valoraciones"
            />
          </li>
        </ul>

        <div className={styles.menu4}>
          <div className={styles.logout}>
            ¿Deseas <span onClick={onCerrarSesion}>cerrar sesión</span>?
          </div>
          <div>
            <div className={styles.bordeLogout}></div>
          </div>
        </div>
      </>
    );

    if (!usuario) {
      if (currentPath === "/register") return menu2;
      else if (currentPath === "/login") return menu3;
      else return menu1;
    } else return menu4;
  }
}

export default Nav;
