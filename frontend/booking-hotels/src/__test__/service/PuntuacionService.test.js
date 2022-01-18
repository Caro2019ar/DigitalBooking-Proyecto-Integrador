import axios from "axios";
import { PuntuacionService } from "../../Service/PuntuacionService";

const mockUsuario = {
	id: 1,
	nombre: "Pepe",
	apellido: "Pérez",
	email: "pepito@perez.com",
	password: "pepito123",
};
const mockProduct = {
	id: 1,
	img: "https://image.freepik.com/foto-gratis/tipo-complejo-entretenimiento-popular-complejo-piscinas-parques-acuaticos-turquia-mas-5-millones-visitantes-al-ano-amara-dolce-vita-hotel-lujo-recurso-tekirova-kemer_146671-18728.jpg",
	category: "Hotel",
	title: "Buena Vista",
	location: "Buenos aires",
	description: "Un hotel bastante bonito",
};

jest.mock("axios");
const BASE_URL = "http://localhost:8080/puntuaciones";

describe("<PuntuacionService/>", () => {
	it("debe probar el endpoint de Puntuación", async () => {
		axios.get.mockResolvedValueOnce(mockProduct);
		const puntuacionServiceMock = new PuntuacionService();
		await puntuacionServiceMock.getPuntuacionByProductoAndUsuario(
			mockProduct.id,
			mockUsuario.id
		);
		expect(axios.get).toHaveBeenCalledWith(
			`${BASE_URL}/buscar?producto=${mockProduct.id}&usuario=${mockUsuario.id}`
		);
	});
});
