import React from "react";

//css
import global from "../../styles/global.module.css";
import "../../styles/componentGlobal/button.module.css";
import bookingOkStyle from "../../styles/bookingPage/bookingOkStyle.module.css";

//componenet
import Button from "../componentGlobal/Button";

//image
import icon from "../../image/iconOk.png";

const BookingOkPage = () => {
  return (
    <div
      className={`${global.container} ${global.bookingBG} ${bookingOkStyle.container}`}
    >
      <div className={bookingOkStyle.box}>
        <img src={icon} className={` ${bookingOkStyle.icon}`}></img>
        <h5 className={bookingOkStyle.h5}>
          Tu propiedad se ha creado con éxito
        </h5>
        <Button
          to="/"
          className={`${global.button} ${bookingOkStyle.button}`}
          id="newProductOk"
          text="Volver"
        />
      </div>
    </div>
  );
};

export default BookingOkPage;
