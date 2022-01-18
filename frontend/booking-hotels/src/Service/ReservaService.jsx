import axios from "axios"
import apiUrl from "../apiUrl.js"

export class ReservaService {

    constructor()
    {
        //console.log(JSON.parse(localStorage.getItem("token")));
        this.config = {
            headers: { Authorization: `Bearer ${JSON.parse(localStorage.getItem("token"))}` }
        };
    }
    
    async guardarReserva(model)
    {
        let apiCall = await axios.post(apiUrl+"/reservas", model, this.config)
        return Promise.resolve(apiCall.data)
    }
    
    async reservaPorIdUsuario(id)
    {
        let apiCall = await axios.get(apiUrl+"/reservas/buscar?usuario="+id, this.config)
        return Promise.resolve(apiCall.data)
    }

}