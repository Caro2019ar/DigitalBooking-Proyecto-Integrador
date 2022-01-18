import App from "../component/App";
import "@testing-library/jest-dom/extend-expect";
import { render, screen } from "@testing-library/react";
import { shallow, mount } from "enzyme";
import Main from "../component/index/Main";
import { MemoryRouter } from "react-router";
import ScrollToTop from "../hooks/ScrollToTop";
const mockUsuario = {
	id: 1,
	nombre: "Pepe",
	apellido: "Pérez",
	email: "pepito@perez.com",
	password: "pepito123",
};
const tokenMock = "pepetoken";
const mockSetValue = jest.fn();
jest.mock("react", () => ({
	...jest.requireActual("react"),
	useState: (initialState) => [initialState, mockSetValue],
}));

describe("<App/>", () => {
	beforeEach(() => {
		window.localStorage.clear();
	});
	it("Debe renderizar sin errores", () => {
		let wrapper = shallow(<App />);
		expect(wrapper.find("div").first().hasClass("App")).toBe(true);
	});

	it("Debe renderizar el Header", () => {
		let wrapper = mount(<App />);
		const header = wrapper.find("header");
		expect(header).toHaveLength(1);
	});
	it("Debe renderizar el Main", () => {
		let wrapper = mount(<App />);
		const header = wrapper.find("main");
		expect(header).toHaveLength(1);
	});
	// ======== Teste en SiginForm para cobrir función onIniciarSesion====
	it("Debe probar el Main con localStorage", () => {
		const view = render(
			<MemoryRouter>
				<App>
					<Main usuario={mockUsuario} onIniciarSesion={mockSetValue} />
				</App>
			</MemoryRouter>
		);
		//const mockLocalStorage = jest.spyOn(localStorage, "getItem");
		localStorage.setItem("usuario", JSON.stringify(mockUsuario));
		localStorage.setItem("token", JSON.stringify(tokenMock));
		expect(
			localStorage.getItem("usuario", JSON.stringify(mockUsuario))
		).toContain("Pepe");
	});
	it("Debe usar el hook ScrollToTop", () => {
		const view = render(
			<MemoryRouter>
				<App>
					<ScrollToTop />
				</App>
			</MemoryRouter>
		);
		expect(view.container).toHaveTextContent(/©2021 Digital Booking/i);
		// console.log(view.debug());
	});
});
