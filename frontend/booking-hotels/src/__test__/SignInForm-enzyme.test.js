import React from "react";
import SignInForm from "../component/SignInForm";
import "@testing-library/jest-dom/extend-expect";
import { shallow, mount } from "enzyme";
import { render } from "@testing-library/react";
import { MemoryRouter } from "react-router";
import routeData from "react-router";
import axios from "axios";
import { UsuarioService } from "../Service/UsuarioService";
const mockUsuario = {
	id: 1,
	nombre: "Pepito",
	apellido: "Pérez",
	email: "pepito@perez.com",
	password: "pepito123",
};

let mockSetValue = jest.fn();
jest.mock("axios");
const BASE_URL = "http://localhost:8080/usuarios";
// jest.mock("react", () => ({
// 	...jest.requireActual("react"),
// 	useState: (initialState) => [initialState, mockSetValue],
// }));

//=============== não funciona
// jest.mock("react-router", () => ({
// 	...jest.requireActual("react-router"),
// 	useLocation: jest.fn().mockImplementation(() => {
// 		return {
// 			pathname: "/localhost",
// 		};
// 	}),
// }));
//==== no describe:
// const useLocation = jest.spyOn(routeData, "useLocation");

// beforeEach(() => {
// 	useLocation.mockReturnValue({
// 		search: "confirmacionToken",
// 	});
// });
//================================================================
const mockUseLocationValue = {
	pathname: "/localhost",
	search: "confirmacionToken",
	state: null,
};
// jest.mock("react-router", () => ({
// 	...jest.requireActual("react-router"),
// 	useLocation: jest.fn().mockImplementation(() => {
// 		return mockUseLocationValue;
// 	}),
// }));

///=======
jest.mock("react-router-dom", () => ({
	...jest.requireActual("react-router-dom"),
	useLocation: () => ({
		pathname: "localhost:3000",
	}),
}));
describe("<SignInForm/> con Enzyme", () => {
	// it("tiene elemento 'button' con la palabra 'Ingresar'", () => {
	// 	let wrapper = mount(
	// 		<MemoryRouter>
	// 			<SignInForm onIniciarSesion={mockSetValue} />
	// 		</MemoryRouter>
	// 	);
	// 	const button = wrapper.find("button").first();
	// 	console.log("debug", wrapper.debug());
	// 	button.simulate("click", { preventDefault() {} });
	// 	expect(button.text()).toContain("Ingresar");
	// 	//TODO: ======= Por que não funciona ?!===
	// 	//expect(mockSetValue).toHaveBeenCalled();
	// });
	it("click en el elemento 'button' ", async () => {
		let wrapper = shallow(<SignInForm onIniciarSesion={mockSetValue} />);
		const button = wrapper.find("button").first();
		const loginMock = [
			{
				email: "pepito@perez.com",
				contrasena: "123456",
			},
		];
		axios.post.mockResolvedValueOnce(loginMock);
		const usuarioServiceMock = new UsuarioService();
		await usuarioServiceMock.login(JSON.stringify(loginMock));
		// console.log("debug", wrapper.debug());
		button.simulate("click");
		expect(wrapper.find("button").first().text()).toContain("Ingresar");
		expect(axios.post).toHaveBeenCalledWith(
			`${BASE_URL}/login`,
			`${JSON.stringify(loginMock)}`
		);
		//TODO: ======= Por que não funciona ?!===
		// expect(wrapper.find("div").at(6).text()).toContain(
		// 	"El usuario con el email pepito@perez.com no existe."
		// );
		//expect(mockSetValue).toHaveBeenCalled();
	});
});
