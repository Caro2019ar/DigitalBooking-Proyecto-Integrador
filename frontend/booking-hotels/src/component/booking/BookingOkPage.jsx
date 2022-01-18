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
        {/* <i className={`far fa-check-circle ${bookingOkStyle.icon}`}></i> */}
        <img src={icon} className={` ${bookingOkStyle.icon}`}></img>
        <h3 className={bookingOkStyle.h3}>¡Muchas gracias!</h3>
        <h5 className={bookingOkStyle.h5}>
          Su reserva se ha realizado con éxito
        </h5>
        <Button
          to="/"
          className={`${global.button} ${bookingOkStyle.button}`}
          id="bookingOk"
          text="OK"
        />
      </div>
    </div>
  );
};

export default BookingOkPage;
