import axios from "axios"
import apiUrl from "../apiUrl.js"


export class ProductoService {

    constructor()
    {
        this.config = {
            headers: { Authorization: `Bearer ${JSON.parse(localStorage.getItem("token"))}` }
        };
    }

    async getAllProducts() {
        let apiCall = await axios.get(apiUrl+"/productos")
        return Promise.resolve(apiCall.data)
    }

    async getAllProductsPaginado(page) {
        let apiCall = await axios.get(apiUrl+"/productos/paginado?page="+page)
        return Promise.resolve(apiCall.data)
    }

    async getProductByCategory(categoria) {
        let apiCall = await axios.get(apiUrl+"/productos/buscar?categoria="+categoria)
        return Promise.resolve(apiCall.data)
    }

    async getProductByCategoryPaginado(categoria, page) {
        let apiCall = await axios.get(apiUrl+"/productos/paginado/buscar?categoria="+categoria+"&page="+page)
        return Promise.resolve(apiCall.data)
    }

    async getProductByCity(city) {
        let apiCall = await axios.get(apiUrl+"/productos/buscar?ciudad="+city)
        return Promise.resolve(apiCall.data)
    }

    async getProductByCityPaginado(city, page) {
        let apiCall = await axios.get(apiUrl+"/productos/paginado/buscar?ciudad="+city+"&page="+page)
        return Promise.resolve(apiCall.data)
    }

    async getProductById(id) {
        let apiCall = await axios.get(apiUrl+"/productos/"+id)
        return Promise.resolve(apiCall.data)
    }

    async getNotAvailableDays(id) {
        let apiCall = await axios.get(apiUrl+"/productos/"+id+"/fechas-no-disponible")
        return Promise.resolve(apiCall.data)
    }

    async getProductByFechas(inicio, fin, ciudad) {
        let apiCall = ciudad ?  await axios.get(apiUrl+"/productos/buscar-con-fechas?ciudad="+ciudad+"&inicio="+inicio+"&fin="+fin) :
                                await axios.get(apiUrl+"/productos/buscar-con-fechas?inicio="+inicio+"&fin="+fin);
        return Promise.resolve(apiCall.data)
    }

    async getProductByFechasPaginado(inicio, fin, ciudad, page) {
        let apiCall = ciudad ?  await axios.get(apiUrl+"/productos/paginado/buscar-con-fechas?ciudad="+ciudad+"&inicio="+inicio+"&fin="+fin+"&page="+page) :
                                await axios.get(apiUrl+"/productos/paginado/buscar-con-fechas?inicio="+inicio+"&fin="+fin+"&page="+page);
        return Promise.resolve(apiCall.data)
    }

    async getFavoritesByUser(id) {
        let apiCall = await axios.get(apiUrl+"/productos/favoritos?usuario="+id, this.config);
        return Promise.resolve(apiCall.data)
    }

    async getFavoritesByUserPaginado(id, page) {
        let apiCall = await axios.get(apiUrl+"/productos/paginado/favoritos?usuario="+id+"&page="+page, this.config)
        return Promise.resolve(apiCall.data)
    }

    async getFavoritesIdByUser(id) {
        let apiCall = await axios.get(apiUrl+"/productos/favoritos-id?usuario="+id, this.config);
        return Promise.resolve(apiCall.data)
    }

    async isFavoriteByUser(productoId, usuarioId) {
        let apiCall = await axios.get(apiUrl+"/productos/"+productoId+"/es-favorito?usuario="+usuarioId, this.config);
        return Promise.resolve(apiCall.data)
    }

    async agregarPuntuacion(idProducto, idCliente, puntos)
    {
        let apiCall = await axios.put(apiUrl+"/productos/"+idProducto+"/agregar-puntuacion", { cliente: {id: idCliente}, puntos: puntos }, this.config);
        return Promise.resolve(apiCall.data);
    }

    async actualizarPuntuacion(idProducto, idCliente, puntos)
    {
        let apiCall = await axios.put(apiUrl+"/productos/"+idProducto+"/actualizar-puntuacion", { cliente: {id: idCliente}, puntos: puntos }, this.config);
        return Promise.resolve(apiCall.data);
    }

    async eliminarPuntuacion(idProducto, idCliente)
    {
        let apiCall = await axios.put(apiUrl+"/productos/"+idProducto+"/eliminar-puntuacion", { cliente: {id: idCliente} }, this.config);
        return Promise.resolve(apiCall.data);
    }

    async registrarProducto(model)
    {
        let apiCall = await axios.post(apiUrl+"/productos", model, this.config)
        return Promise.resolve(apiCall.data)
    }

    async getProductsByPuntuacionFromUsuario(id)
    {
        let apiCall = await axios.get(apiUrl+"/productos/puntuaciones-cliente?&usuario="+id, this.config)
        return Promise.resolve(apiCall.data)
    }

}