import axios from "axios"
import apiUrl from "../apiUrl.js"

export class PuntuacionService {
    
    async getPuntuacionByProductoAndUsuario(producto, usuario)
    {
        let apiCall = await axios.get(apiUrl+"/puntuaciones/buscar?producto="+producto+"&usuario="+usuario)
        return Promise.resolve(apiCall.data)
    }

}