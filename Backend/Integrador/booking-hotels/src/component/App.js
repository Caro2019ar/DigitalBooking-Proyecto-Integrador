import React from "react";
import { BrowserRouter, HashRouter } from "react-router-dom";

//component
import Main from "./Main";
import Header from "./header/Header";
import Footer from "./Footer";

//CSS
import globalStyles from "../styles/global.module.css";
import "../styles/index/main.module.css";

function App() {
  return (
    <div className={globalStyles.App}>
      <HashRouter>
        <Header />
        <Main />
        <Footer />
      </HashRouter>
    </div>
  );
}

export default App;