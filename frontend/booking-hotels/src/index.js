import React from "react";
import ReactDOM from "react-dom";

import App from "./component/App";

console.warn = () => {};

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById("root")
);