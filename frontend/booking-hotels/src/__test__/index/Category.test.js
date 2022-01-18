import React from "react";
import axios from "axios";
import { shallow, mount } from "enzyme";
import Category from "../../component/index/Category";
import { render, screen } from "@testing-library/react";
import dataCategorias from "../../dataCategorias";
import { MemoryRouter } from "react-router";
import { CategoriaService } from "../../Service/CategoriaService";
jest.mock("axios");
const BASE_URL = "http://localhost:8080/categorias";

// const mockSetValue = jest.fn();
// jest.mock("react", () => ({
// 	...jest.requireActual("react"),
// 	useState: (initialState) => [initialState, mockSetValue],
// }));

describe("Some message", () => {
	it("Category", async () => {
		const initialStateForSecondUseStateCall = [
			{
				img: "imagen1",
				titulo: "Hoteles",
				cantidad: "860.021 hoteles",
			},
			{
				img: "imagen2",
				titulo: "Hostels",
				cantidad: "12.124 hoteles",
			},
		];
		// const initialStateForFirstUseStateCall = false;
		// // axios.get.mockResolvedValueOnce();
		// // const categoriaService = new CategoriaService();
		// // await categoriaService.getAllCategorias();
		// React.useState = jest
		// 	.fn()
		// 	.mockReturnValueOnce(initialStateForFirstUseStateCall)
		// 	.mockReturnValueOnce(initialStateForSecondUseStateCall);
		const realUseState = React.useState;
		const realUseState2 = React.useState;
		const initialState = false;

		jest
			.spyOn(React, "useState")
			.mockImplementationOnce(() => realUseState(initialState))
			.mockImplementationOnce(() =>
				realUseState2([initialStateForSecondUseStateCall])
			);

		const wrapper = render(
			<MemoryRouter>
				<Category />
			</MemoryRouter>
		);
		console.log("wrapper", screen.debug());
	});
});
// describe("<Category />", () => {
// 	console.log(wrapper.debug());
// 	it("Renderiza un solo h2", () => {
// 		expect(wrapper.find("h2")).toHaveLength(1);
// 	});

// 	it('El elemento "div" tiene la clase "category"', () => {
// 		expect(wrapper.find("div").first().hasClass("category")).toBe(true);
// 	});

// 	it('El elemento "h2" contiene exactamente el texto "Buscar por tipo de alojamiento"', () => {
// 		expect(wrapper.find("h2").first().text()).toEqual(
// 			"Buscar por tipo de alojamiento"
// 		);
// 	});

// 	it('Renderiza un único div con la clase "typeContainer"', () => {
// 		expect(wrapper.find(".typeContainer")).toHaveLength(1);
// 		expect(wrapper.find(".typeContainer").first().name()).toEqual("div");
// 	});

// 	// it('Renderiza las tarjetas (<div>) necesarias con la clase "typeCard" dentro de "typeContainer"', () => {
// 	// 	expect(wrapper.find(".typeContainer").children()).toHaveLength(
// 	// 		dataCategorias.length
// 	// 	);
// 	// 	for (let i = 0; i < dataCategorias.length; i++) {
// 	// 		expect(
// 	// 			wrapper.find(".typeContainer").children().at(i).hasClass("typeCard")
// 	// 		).toBeTruthy;
// 	// 	}
// 	// 	expect(wrapper.find(".typeCard")).toHaveLength(dataCategorias.length);
// 	// 	for (let i = 0; i < dataCategorias.length; i++) {
// 	// 		expect(wrapper.find(".typeCard").at(i).name()).toEqual("div");
// 	// 	}
// 	// });

// 	// it("Renderiza un <a>, un <h3> y un <p> dentro de cada tarjeta", () => {
// 	// 	for (let i = 0; i < dataCategorias.length; i++) {
// 	// 		expect(wrapper.find(".typeCard").at(i).children().at(0).name()).toEqual(
// 	// 			"Link"
// 	// 		);
// 	// 		expect(wrapper.find(".typeCard").at(i).children().at(1).name()).toEqual(
// 	// 			"h3"
// 	// 		);
// 	// 		expect(wrapper.find(".typeCard").at(i).children().at(2).name()).toEqual(
// 	// 			"p"
// 	// 		);
// 	// 	}
// 	// });

// 	// it("Las tarjetas renderizan una imagen dentro de los <Link>", () => {
// 	// 	for (let i = 0; i < dataCategorias.length; i++) {
// 	// 		expect(
// 	// 			wrapper.find(".typeCard").at(i).find("Link").first().children()
// 	// 		).toHaveLength(1);
// 	// 		expect(
// 	// 			wrapper
// 	// 				.find(".typeCard")
// 	// 				.at(i)
// 	// 				.find("Link")
// 	// 				.first()
// 	// 				.children()
// 	// 				.first()
// 	// 				.name()
// 	// 		).toEqual("img");
// 	// 	}
// 	// });

// 	// it("Las tarjetas renderizan el nombre las categorías en los <h3>", () => {
// 	// 	for (let i = 0; i < dataCategorias.length; i++) {
// 	// 		expect(wrapper.find(".typeCard").at(i).find("h3").first().text()).toEqual(
// 	// 			dataCategorias[i].titulo
// 	// 		);
// 	// 	}
// 	// });

// 	// it("Las tarjetas renderizan la descripción de cada categoría en los <p>", () => {
// 	// 	for (let i = 0; i < dataCategorias.length; i++) {
// 	// 		expect(wrapper.find(".typeCard").at(i).find("p").first().text()).toEqual(
// 	// 			dataCategorias[i].cantidad
// 	// 		);
// 	// 	}
// 	// });
// });
