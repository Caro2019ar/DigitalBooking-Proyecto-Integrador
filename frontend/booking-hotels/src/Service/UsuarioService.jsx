import axios from "axios";
import apiUrl from "../apiUrl.js"


export class UsuarioService {
    
    async getUsuarioByEmail(email)
    {
        let apiCall = await axios.get(apiUrl+"/usuarios/buscar?email="+email);
        return Promise.resolve(apiCall.data);
    }

    async validateUserLogin(model)
    {
        let apiCall = await axios.post(apiUrl+"/usuarios/validate-user-login", model);
        return Promise.resolve(apiCall.data);
    }

    async login(model)
    {
        let apiCall = await axios.post(apiUrl+"/usuarios/login", model);
        return Promise.resolve(apiCall.data);
    }
    
}