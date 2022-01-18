import axios from "axios";
import { ProductoService } from "../../Service/ProductoService";

const BASE_URL = "http://localhost:8080/productos";
const product = [
	{
		id: 1,
		img: "https://image.freepik.com/foto-gratis/tipo-complejo-entretenimiento-popular-complejo-piscinas-parques-acuaticos-turquia-mas-5-millones-visitantes-al-ano-amara-dolce-vita-hotel-lujo-recurso-tekirova-kemer_146671-18728.jpg",
		category: "Hotel",
		title: "Buena Vista",
		location: "Buenos aires",
		description: "Un hotel bastante bonito",
	},
];
jest.mock("axios");

describe("<ProductsService/>", () => {
	it("debe probar el endpoint de 'productos' que trae todos los productos", async () => {
		axios.get.mockResolvedValueOnce(product);

		const productServiceMock = new ProductoService();
		await productServiceMock.getAllProducts();

		expect(axios.get).toHaveBeenCalledWith(`${BASE_URL}`);
	});
	it("debe probar el endpoint de 'productos' que trae todos los productos por categoria", async () => {
		const categoriaMock = { nombre: "hotel" };
		axios.get.mockResolvedValueOnce(categoriaMock.nombre);

		const productServiceMock = new ProductoService();
		await productServiceMock.getProductByCategory(categoriaMock.nombre);

		expect(axios.get).toHaveBeenCalledWith(
			`${BASE_URL}/buscar?categoria=${categoriaMock.nombre}`
		);
	});
	it("debe probar el endpoint de 'productos' que trae todos los productos por ciudad", async () => {
		const categoriaCiudadMock = { nombre: "hotel", ciudad: "Mendoza" };
		axios.get.mockResolvedValueOnce(categoriaCiudadMock);

		const productServiceMock = new ProductoService();
		await productServiceMock.getProductByCity(categoriaCiudadMock.ciudad);

		expect(axios.get).toHaveBeenCalledWith(
			`${BASE_URL}/buscar?ciudad=${categoriaCiudadMock.ciudad}`
		);
	});
	it("debe probar el endpoint de 'productos' que trae los productos por ciudad y por fecha", async () => {
		const ciudadMock = { nombre: "Mendoza" };
		const inicioMock = "01-01-2022";
		const finMock = "02-01-2022";
		axios.get.mockResolvedValueOnce(ciudadMock);

		const productServiceMock = new ProductoService();
		await productServiceMock.getProductByFechas(
			inicioMock,
			finMock,
			ciudadMock.nombre
		);

		expect(axios.get).toHaveBeenCalledWith(
			`${BASE_URL}/buscar-con-fechas?ciudad=${ciudadMock.nombre}&inicio=${inicioMock}&fin=${finMock}`
		);
	});
	it("debe probar el endpoint de fechas no disponibles de productos por id", async () => {
		const idMock = "1";
		axios.get.mockResolvedValueOnce(idMock);

		const productServiceMock = new ProductoService();
		await productServiceMock.getNotAvailableDays(idMock);

		expect(axios.get).toHaveBeenCalledWith(
			`${BASE_URL}/${idMock}/fechas-no-disponible`
		);
	});
	it("debe probar el endpoint de productos favoritos por id de usuario", async () => {
		const idMock = "1";
		axios.get.mockResolvedValueOnce(idMock);

		const productServiceMock = new ProductoService();
		await productServiceMock.getFavoritesByUser(idMock);

		expect(axios.get).toHaveBeenCalledWith(
			`${BASE_URL}/favoritos?usuario=${idMock}`
		);
	});
	it("debe probar el endpoint de productos favoritos por su id", async () => {
		const idMock = "1";
		axios.get.mockResolvedValueOnce(idMock);

		const productServiceMock = new ProductoService();
		await productServiceMock.getFavoritesIdByUser(idMock);

		expect(axios.get).toHaveBeenCalledWith(
			`${BASE_URL}/favoritos-id?usuario=${idMock}`
		);
	});
	it("debe probar el endpoint de productos favoritos por usuario", async () => {
		const idMock = "1";

		axios.get.mockResolvedValueOnce(idMock);

		const productServiceMock = new ProductoService();
		await productServiceMock.isFavoriteByUser(idMock, product[0].id);

		expect(axios.get).toHaveBeenCalledWith(
			`${BASE_URL}/${product[0].id}/es-favorito?usuario=${idMock}`
		);
	});
});
