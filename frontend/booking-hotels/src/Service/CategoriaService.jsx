import axios from "axios";
import apiUrl from "../apiUrl.js"


export class CategoriaService {
    
    async getAllCategorias(){
        let apiCall = await axios.get(apiUrl+"/categorias")
        return Promise.resolve(apiCall.data)
    }
    
}