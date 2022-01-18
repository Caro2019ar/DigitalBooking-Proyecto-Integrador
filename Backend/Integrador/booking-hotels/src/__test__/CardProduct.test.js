import React from "react";
import { render, screen, within, logRoles } from "@testing-library/react";
import "@testing-library/jest-dom";
import { MemoryRouter } from "react-router";

import CardProduct from "../component/CardProduct";
import dataProductos from "../tarjetas.json";

describe("<CardProduct />", () => {
	it('El primer elemento con el rol "heading" contiene el texto "Recomendaciones" y es un <h2>', () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<CardProduct />{" "}
			</MemoryRouter>
		);
		expect(view.getAllByRole("heading")[0].textContent).toEqual(
			"Recomendaciones"
		);
		expect(view.getAllByRole("heading")[0].tagName).toEqual("H2");
	});

	it("Se renderizan todas las tarjetas en el JSON", () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<CardProduct />{" "}
			</MemoryRouter>
		);
		expect(view.getAllByRole("article")).toHaveLength(dataProductos.length);
	});

	it("Cada tarjeta contiene una imagen", () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<CardProduct />{" "}
			</MemoryRouter>
		);
		for (let i = 0; i < dataProductos.length; i++) {
			expect(
				within(view.getAllByRole("article")[i]).queryByRole("img")
			).not.toBeNull();
		}
		//logRoles(view.getAllByRole("article")[0]);
	});

	// it("Cada tarjeta contiene tres heading (categoría y título)", () => {
	// 	const view = render(
	// 		<MemoryRouter>
	// 			{" "}
	// 			<CardProduct />{" "}
	// 		</MemoryRouter>
	// 	);
	// 	for (let i = 0; i < dataProductos.length; i++) {
	// 		expect(
	// 			within(view.getAllByRole("article")[i]).getAllByRole("heading")
	// 		).toHaveLength(2);
	// 	}
	// });

	it('Cada tarjeta contiene un button con el texto "Ver detalle"', () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<CardProduct />{" "}
			</MemoryRouter>
		);
		for (let i = 0; i < dataProductos.length; i++) {
			expect(
				within(view.getAllByRole("article")[i]).queryByRole("button", {
					name: "Ver detalle",
				})
			).not.toBeNull();
		}
	});

	it('Cada button tiene un link al path "/producto"', () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<CardProduct />{" "}
			</MemoryRouter>
		);
		for (let i = 0; i < dataProductos.length; i++) {
			expect(
				within(view.getAllByRole("article")[i]).queryByRole("link", {
					name: "Ver detalle",
				})
			).not.toBeNull();
			// expect(
			// 	within(view.getAllByRole("article")[i]).getByRole("link", {
			// 		name: "Ver detalle",
			// 	})
			// ).toHaveAttribute("href", "/producto/1");

			// // Otra manera de testearlo
			// expect(
			// 	within(view.getAllByRole("article")[i])
			// 		.getByText("Ver detalle")
			// 		.closest("a")
			// ).toHaveAttribute("href", "/producto");
		}
	});
});
