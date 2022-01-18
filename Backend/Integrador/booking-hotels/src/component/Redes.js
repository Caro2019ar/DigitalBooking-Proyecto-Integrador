import React from "react";

import styles from "../styles/index/footer.module.css";

const Redes = (props) => {
  return (
    <div className={props.className}>
      <a
        id="facebook"
        href="https://facebook.com"
        target="_blank"
        rel="noopener noreferrer"
      >
        <i className={`${styles["icono-redes"]} fab fa-facebook`} />
      </a>
      <a
        id="linkedin"
        href="https://linkedin.com"
        target="_blank"
        rel="noopener noreferrer"
      >
        <i className={`${styles["icono-redes"]} fab fa-linkedin-in`} />
      </a>
      <a
        id="twitter"
        href="https://twitter.com"
        target="_blank"
        rel="noopener noreferrer"
      >
        <i className={`${styles["icono-redes"]} fab fa-twitter`} />
      </a>
      <a
        id="instagram"
        href="https://instagram.com"
        target="_blank"
        rel="noopener noreferrer"
      >
        <i className={`${styles["icono-redes"]} fab fa-instagram`} />
      </a>
    </div>
  );
};

export default Redes;
