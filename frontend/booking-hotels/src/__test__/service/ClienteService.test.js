import axios from "axios";
import { ClienteService } from "../../Service/ClienteService";
import "@testing-library/jest-dom/extend-expect";

jest.mock("axios");
const BASE_URL = "http://localhost:8080";
const mockConfig = {
	headers: {
		Authorization: `Bearer null`,
	},
};
describe("ClienteService/>", () => {
	it("debe probar el registro de cliente", async () => {
		const users = [
			{
				apellido: "perez",
				contrasena: "pepito123",
				email: "pepito@perez.com",
				nombre: "Pepito",
			},
		];
		axios.post.mockResolvedValueOnce(users);

		const clienteMock = new ClienteService();
		await clienteMock.registrarCliente(JSON.stringify(users));

		expect(axios.post).toHaveBeenCalledWith(
			`${BASE_URL}/clientes/disabled`,
			`${JSON.stringify(users)}`
		);
	});

	it("debe probar el endpoint de 'clientes/registrationConfirm?token", async () => {
		const token = "token_mendoza";
		axios.get.mockResolvedValueOnce(token);
		const clienteServiceMock = new ClienteService();
		await clienteServiceMock.confirmacionCuenta(token);
		expect(axios.get).toHaveBeenCalledWith(
			`${BASE_URL}/clientes/registrationConfirm?token=${token}`
		);
	});
	it("debe probar el endpoint de agregar favorito al cliente", async () => {
		const idMock = "1";
		axios.put.mockResolvedValueOnce(idMock);
		const clienteServiceMock = new ClienteService();
		await clienteServiceMock.agregarFavorito(idMock, idMock);
		expect(axios.put).toHaveBeenCalledWith(
			`${BASE_URL}/clientes/${idMock}/agregar-favorito`,
			{ id: idMock },
			mockConfig
		);
	});
	it("debe probar el endpoint de eliminar favorito del cliente", async () => {
		const idMock = "1";
		axios.put.mockResolvedValueOnce(idMock);
		const clienteServiceMock = new ClienteService();
		await clienteServiceMock.eliminarFavorito(idMock, idMock);
		expect(axios.put).toHaveBeenCalledWith(
			`${BASE_URL}/clientes/${idMock}/eliminar-favorito`,
			{ id: idMock },
			mockConfig
		);
	});
});
