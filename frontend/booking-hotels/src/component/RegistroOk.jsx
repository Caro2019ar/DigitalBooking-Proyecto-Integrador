import React from "react";

//css
import global from "../styles/global.module.css";
import "../styles/componentGlobal/button.module.css";
import registroOk from "../styles/registroOk.module.css";

//componenet
import Button from "./componentGlobal/Button";

//icono
import tilde from "../image/tilde-check.svg";

const RegistroOk = () => {
  return (
    <div className={`${registroOk.container}`}>
      <div className={registroOk.box}>
        <img
          src={tilde}
          alt="tilde-check.svg"
          className={`${registroOk.icon}`}
        />
        <h3 className={`${registroOk.titulo}`}>¡Muchas gracias!</h3>
        <h5 className={`${registroOk.mensaje}`}>
          Su registro se ha realizado con éxito.
        </h5>
        <h5 className={`${registroOk.mensaje}`}>
          Por favor, verifique su casilla de correo. Pronto recibirá un email
          para confirmar su cuenta.
        </h5>
        <Button
          to="/"
          className={`${global.button} ${registroOk.button}`}
          id="bookingOk"
          text="OK"
          onClick=""
        />
      </div>
    </div>
  );
};

export default RegistroOk;
