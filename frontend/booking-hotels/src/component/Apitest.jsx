import React from "react";
import jwt from "jsonwebtoken";
import { ProductoService } from "../Service/ProductoService";

function Apitest() {

    // API tests

    /*console.log("entra");

    const productService = new ProductsService();
    const res = productService.getProductById(99);

    console.log(res);

    res.then( data => console.dir(data))
    .catch( error => console.dir(error))*/




    // JWT tests

    /*const token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VybmFtZS10ZXN0IiwiZXhwIjoxNjM3MDIxNzcxLCJpYXQiOjE2MzcwMDAxNzF9.HBkH4BFw7G7ZUZuJ90SB_sfF9oFlnIKAGHncoBl2NZd8aTEK6C89D1N-82TqWISL5vuJyTBdFkZly98g69c5vQ";
    var decodedToken = jwt.decode(token, {complete: true});

    console.log(decodedToken);

    console.log(Date.now())
    console.log(decodedToken.payload.exp * 1000);*/







    return null;
}

export default Apitest;