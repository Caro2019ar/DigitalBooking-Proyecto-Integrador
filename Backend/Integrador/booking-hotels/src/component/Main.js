import React from "react";
import axios from 'axios';
import { Route, Switch, useLocation } from "react-router-dom";
import { useState, useEffect } from "react";
import Buscador from "./Buscador";
import Category from "./Category";
import CardProduct from "./CardProduct";
import SignInForm from "./SignInForm";
import SignUpForm from "./SignUpForm";
import NotFound from "./NotFound";
import DetailsPage from "./detailsPage/DetailsPage";

// CSS
const Main = (props) => {


  
  return (
    <main>
      <Switch>
        <Route exact path="/">
          <Buscador />
          <Category />
          <CardProduct  />
        </Route>
    
  
        <Route exact path="/login" component={SignInForm} />
        <Route exact path="/register" component={SignUpForm} />
        <Route exact path="/product/:id" component={DetailsPage} />
        <Route path="*" component={NotFound} />
      </Switch>
    </main>
  );
};

export default Main;
