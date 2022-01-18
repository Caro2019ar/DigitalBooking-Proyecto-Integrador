import axios from "axios"


export class ProductsService {
    
    async getAllProducts(){
        let product = null
        product = await axios.get("http://localhost:8080/productos")
        return Promise.resolve(product.data)
    }

    async getProductByCategory(categoria){
        let product = null
        product = await axios.get("http://localhost:8080/productos/buscar?categoria="+categoria)
        return Promise.resolve(product.data)
    }
    async getProductByCity(city){
        let product = null
        product = await axios.get("http://localhost:8080/productos/buscar?ciudad="+city)
        return Promise.resolve(product.data)
    }
    async getProductById(id){
        let product = null
        product = await axios.get("http://localhost:8080/productos/"+id)
        return Promise.resolve(product.data)
    }
}