import axios from "axios";
import apiUrl from "../apiUrl.js"


export class CityService {
    
    async getAllCity()
    {
        let city = await axios.get(apiUrl+"/ciudades");
        return Promise.resolve(city.data);
    }
    
}