import React from 'react';

//css
import global from "../../styles/global.module.css";
import "../../styles/componentGlobal/button.module.css";
import bookingOkStyle from "../../styles/bookingPage/bookingOkStyle.module.css";
import modalStyles from "../../styles/modal/modal.module.css";

//componenet
import Button from "../componentGlobal/Button";


const BanModal = (props) => {
    return (
        <div
        className={`${global.container} ${bookingOkStyle.container} ${modalStyles.modalContainer}`}
      >
        <div className={bookingOkStyle.box}>
        <i className={`${props.iconName} ${modalStyles.icon} ${modalStyles.banIcon}`} ></i>
          <h5 className={bookingOkStyle.h5}>
            {props.text}
          </h5>
          <Button
            to="/"
            className={`${global.button} ${bookingOkStyle.button}`}
            id="newProductOk"
            text={`${props.buttonText}`}
          />
        </div>
      </div>
    );
};

export default BanModal;
