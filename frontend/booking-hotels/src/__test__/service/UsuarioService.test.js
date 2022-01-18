import axios from "axios";
import { UsuarioService } from "../../Service/UsuarioService";

const BASE_URL = "http://localhost:8080/usuarios";
jest.mock("axios");

describe("<UsuarioService/>", () => {
	it("debe probar el endpoint de 'usuarios/buscar?email", async () => {
		const email = "pepito@perez.com";
		axios.get.mockResolvedValueOnce(email);

		const usuarioServiceMock = new UsuarioService();
		await usuarioServiceMock.getUsuarioByEmail("pepito@perez.com");

		expect(axios.get).toHaveBeenCalledWith(
			`${BASE_URL}/buscar?email=pepito@perez.com`
		);
	});
	it("debe probar el endpoint de 'usuarios/validate-user-login", async () => {
		const users = [
			{
				apellido: "perez",
				contrasena: "pepito123",
				email: "pepito@perez.com",
				nombre: "Pepito",
			},
		];
		axios.post.mockResolvedValueOnce(users);

		const usuarioServiceMock = new UsuarioService();
		await usuarioServiceMock.validateUserLogin(users);

		// expect(axios.post).toHaveBeenCalledWith(`${BASE_URL}/validate-user-login`);
	});
	it("debe probar el login de cliente", async () => {
		const loginMock = [
			{
				email: "pepito@perez.com",
				contrasena: "123456",
			},
		];
		axios.post.mockResolvedValueOnce(loginMock);
		const clienteMock = new UsuarioService();
		await clienteMock.login(JSON.stringify(loginMock));

		expect(axios.post).toHaveBeenCalledWith(
			`${BASE_URL}/login`,
			`${JSON.stringify(loginMock)}`
		);
	});
});
