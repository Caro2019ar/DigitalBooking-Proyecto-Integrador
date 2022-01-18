import axios from "axios";
import token from "../token.js";

const config = {
    headers: { Authorization: `Bearer ${token}` }
};

export class CityService {
    
    async getAllCity()
    {
        console.log(token);
        let city = null;
        city = await axios.get("http://localhost:8080/ciudades", config);
        return Promise.resolve(city.data);
    }
}