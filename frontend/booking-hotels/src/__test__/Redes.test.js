import React from "react";
import { render, screen, within, logRoles } from "@testing-library/react";
import "@testing-library/jest-dom";
import { MemoryRouter } from "react-router";

import Redes from "../component/componentGLobal/Redes";

describe("<Redes />", () => {
	it("Existen 4 links", () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<Redes />{" "}
			</MemoryRouter>
		);
		expect(view.getAllByRole("link")).toHaveLength(4);

		//Para ver los roles en consola
		//view.getAllByRole("dhgdfhdf");
	});

	it("Cada link tiene un ícono", () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<Redes />{" "}
			</MemoryRouter>
		);
		for (let i = 0; i <= 3; i++) {
			expect(view.getAllByRole("link")[i].children[0].tagName).toEqual("I");
		}
	});

	it("Cada ícono tiene su link correspondiente", () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<Redes />{" "}
			</MemoryRouter>
		);
		for (let i = 0; i <= 3; i++) {
			let iconoElemento = view.getAllByRole("link")[i].children[0];
			if (iconoElemento.classList.contains("fa-facebook"))
				expect(iconoElemento.closest("a")).toHaveAttribute(
					"href",
					"https://facebook.com"
				);
			if (iconoElemento.classList.contains("fa-linkedin"))
				expect(iconoElemento.closest("a")).toHaveAttribute(
					"href",
					"https://linkedin.com"
				);
			if (iconoElemento.classList.contains("fa-instagram"))
				expect(iconoElemento.closest("a")).toHaveAttribute(
					"href",
					"https://instagram.com"
				);
			if (iconoElemento.classList.contains("fa-twitter"))
				expect(iconoElemento.closest("a")).toHaveAttribute(
					"href",
					"https://twitter.com"
				);
		}
	});
});
