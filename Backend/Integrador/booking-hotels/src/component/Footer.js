import React from "react";
import Redes from "./Redes";

//CSS
import styles from "../styles/index/footer.module.css";

const Footer = () => {
  return (
    <footer>
      <div className={styles.creditos}>©2021 Digital Booking</div>
      <Redes className={styles.redes} />
    </footer>
  );
};

export default Footer;
