import React from "react";
import { render, screen, within, logRoles } from "@testing-library/react";
import "@testing-library/jest-dom";
import { MemoryRouter } from "react-router";
import { shallow, mount } from "enzyme";

import CardProductList from "../component/componentGlobal/CardProductList";

const mockProduct = {
	id: 1,
	categoria: {
		titulo: "Hotel urbano",
	},
	title: "Hotel mock",
	nombre: "Hotel Bello",
	imagenes: ["1", "2"],
	ciudad: { nombre: "Mendoza" },
	valoracion: { cantidadVotos: 0, puntajeTotal: 10 },
	caracteristicas: [{ icono: "999" }, { icono: "888" }, { icono: "888" }],
};
const mockTitle = "Recomendaciones";
const mockFavorite = true;
const mockUsuario = {
	id: 1,
	nombre: "Pepe",
	apellido: "Pérez",
	email: "pepito@perez.com",
	password: "pepito123",
};

let mockSetValue = jest.fn();
jest.mock("react", () => ({
	...jest.requireActual("react"),
	useState: (initialState) => [initialState, mockSetValue],
}));

describe("<CardProductList />", () => {
	afterEach(() => {
		jest.clearAllMocks();
	});
	it("Se prueba que tenga la clase productsCardContainer", () => {
		let realUseState = React.useState;
		jest
			.spyOn(React, "useState")
			.mockImplementationOnce(() => realUseState(false));
		const wrapper = mount(
			<MemoryRouter>
				<CardProductList
					title={mockTitle}
					favorite={mockFavorite}
					usuario={mockUsuario}
					onDeleteLastFavorite={mockSetValue}
				/>
			</MemoryRouter>
		);
		// console.log(wrapper.debug());
		expect(wrapper.find("div").first().hasClass("productsCardContainer")).toBe(
			true
		);
	});
});
