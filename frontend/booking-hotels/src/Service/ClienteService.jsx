import axios from "axios";
import apiUrl from "../apiUrl.js"


export class ClienteService {

    constructor()
    {
        this.config = {
            headers: { Authorization: `Bearer ${JSON.parse(localStorage.getItem("token"))}` }
        };
    }
    
    async registrarCliente(model)
    {
        let apiCall = await axios.post(apiUrl+"/clientes/disabled", model);
        return Promise.resolve(apiCall.data);
    }

    async confirmacionCuenta(token)
    {
        let apiCall = await axios.get(apiUrl+"/clientes/registrationConfirm?token="+token);
        return Promise.resolve(apiCall.data);
    }

    async agregarFavorito(idCliente, idProducto)
    {
        let apiCall = await axios.put(apiUrl+"/clientes/"+idCliente+"/agregar-favorito", {id: idProducto}, this.config);
        return Promise.resolve(apiCall.data);
    }

    async eliminarFavorito(idCliente, idProducto)
    {
        let apiCall = await axios.put(apiUrl+"/clientes/"+idCliente+"/eliminar-favorito", {id: idProducto}, this.config);
        return Promise.resolve(apiCall.data);
    }

}