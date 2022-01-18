import React from "react";
import Nav from "../component/header/Nav";
import "@testing-library/jest-dom/extend-expect";
import { shallow, mount } from "enzyme";
import { render } from "@testing-library/react";
import { MemoryRouter } from "react-router";
import userEvent from "@testing-library/user-event";
const mockUsuario = {
	id: 1,
	nombre: "Pepito",
	apellido: "Pérez",
	email: "pepito@perez.com",
	password: "pepito123",
};
let findByTestAttr = (wrapper, val) => wrapper.find(`[data-test='${val}']`);
let mockSetValue = jest.fn();
let wrapper = shallow(
	<Nav usuario={mockUsuario} onCerrarSesion={mockSetValue} />
);
jest.mock("react", () => ({
	...jest.requireActual("react"),
	useState: (initialState) => [initialState, mockSetValue],
}));

jest.mock("react-router-dom", () => ({
	useLocation: jest.fn().mockReturnValue({
		pathname: "/localhost",
	}),
}));

describe("<Nav/>", () => {
	afterEach(() => {
		window.localStorage.clear();
		mockSetValue.mockClear();
	});
	it("tiene elemento 'h5' con la palabra 'Hola'", () => {
		expect(wrapper.find("h5").first().text()).toContain("Hola");
	});

	it("encontrar el usuario pasado por mock", () => {
		expect(wrapper.find("h5").at(1).text()).toContain("Pepito Pérez");
	});
});
