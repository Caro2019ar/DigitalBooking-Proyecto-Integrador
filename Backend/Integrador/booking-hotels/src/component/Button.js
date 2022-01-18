import React from "react";
import { Link } from "react-router-dom";
import "../styles/button.module.css";

const Button = (props) => {
  return (
    <Link
      to={props.to}
      className={props.className}
      id={props.id}
      aria-current={props.ariaCurrent}
      onClick={props.onClick}
    >
      {props.text}
    </Link>
  );
};

export default Button;
