import React from "react";
import { Link } from "react-router-dom";
import logo from "../../image/logo.png";

//components
import Nav from "./Nav";

//css
import style from "../../styles/index/header.module.css";

const Header = (props) => {
  return (
    <header>
      <Link to="/">
        <div className={style.logo}>
          <img src={logo} alt="logo" className={style.imgLogo} />

          <h2 className={style.slogan}>Sentite como en tu hogar</h2>
        </div>
      </Link>
      <Nav {...props} />
    </header>
  );
};

export default Header;