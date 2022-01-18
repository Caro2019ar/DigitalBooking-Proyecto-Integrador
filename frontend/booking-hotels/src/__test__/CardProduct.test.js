import React from "react";
import { render, screen, within, logRoles } from "@testing-library/react";
import "@testing-library/jest-dom";
import { MemoryRouter } from "react-router";
import userEvent from "@testing-library/user-event";
import CardProduct from "../component/componentGlobal/CardProduct";
import dataProductos from "../tarjetas.json";

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
	rol: "ROLE_CUSTOMER",
};
const mockCantidadResultados = "1";
const mockSetValue = jest.fn();
jest.mock("react", () => ({
	...jest.requireActual("react"),
	useState: (initialState) => [initialState, mockSetValue],
}));

describe("<CardProduct />", () => {
	it("Se renderizan todas las tarjetas en el JSON", () => {
		const view = render(
			<MemoryRouter>
				<CardProduct
					item={mockProduct}
					favorite={mockFavorite}
					usuarioId={mockUsuario.id}
					onEliminacionFavorito={mockSetValue}
					onDeleteLastFavorite={mockSetValue}
					cantidadResultados={mockCantidadResultados}
				/>
			</MemoryRouter>
		);
		//console.log(view.debug());
		expect(view.getAllByRole("article")).toHaveLength(1);
	});
	it("click en el spanFavoriteIcon ", async () => {
		const view = render(
			<MemoryRouter>
				<CardProduct
					item={mockProduct}
					favorite={mockFavorite}
					usuario={mockUsuario}
					onEliminacionFavorito={mockSetValue}
					onDeleteLastFavorite={mockSetValue}
					cantidadResultados={mockCantidadResultados}
				/>
			</MemoryRouter>
		);
		// console.log(view.debug());
		mockSetValue.mockClear();
		const button = view.getByTestId("spanFavoriteIcon");
		userEvent.click(button);
		expect(mockSetValue).toHaveBeenCalled();
	});
	it("promedioActual>4,5 ", () => {
		const mockProductProm = {
			id: 1,
			categoria: {
				titulo: "Hotel urbano",
			},
			title: "Hotel mock",
			nombre: "Hotel Bello",
			imagenes: ["1", "2"],
			ciudad: { nombre: "Mendoza" },
			valoracion: { cantidadVotos: 1, puntajeTotal: 10 },
			caracteristicas: [{ icono: "999" }, { icono: "888" }, { icono: "888" }],
		};
		const view = render(
			<MemoryRouter>
				<CardProduct
					item={mockProductProm}
					favorite={mockFavorite}
					usuario={mockUsuario}
					onEliminacionFavorito={mockSetValue}
					onDeleteLastFavorite={mockSetValue}
					cantidadResultados={mockCantidadResultados}
				/>
			</MemoryRouter>
		);
		expect(view.getAllByRole("heading")[2]).toHaveTextContent("Divino");
	});
	it("promedioActual>1,5 ", () => {
		const mockProductProm = {
			id: 1,
			categoria: {
				titulo: "Hotel urbano",
			},
			title: "Hotel mock",
			nombre: "Hotel Bello",
			imagenes: ["1", "2"],
			ciudad: { nombre: "Mendoza" },
			valoracion: { cantidadVotos: 10, puntajeTotal: 15 },
			caracteristicas: [{ icono: "999" }, { icono: "888" }, { icono: "888" }],
		};
		const view = render(
			<MemoryRouter>
				<CardProduct
					item={mockProductProm}
					favorite={mockFavorite}
					usuario={mockUsuario}
					onEliminacionFavorito={mockSetValue}
					onDeleteLastFavorite={mockSetValue}
					cantidadResultados={mockCantidadResultados}
				/>
			</MemoryRouter>
		);
		expect(view.getAllByRole("heading")[2]).toHaveTextContent("Horrible");
	});
	it("promedioActual<1,5 ", () => {
		const mockProductProm = {
			id: 1,
			categoria: {
				titulo: "Hotel urbano",
			},
			title: "Hotel mock",
			nombre: "Hotel Bello",
			imagenes: ["1", "2"],
			ciudad: { nombre: "Mendoza" },
			valoracion: { cantidadVotos: 10, puntajeTotal: 14 },
			caracteristicas: [{ icono: "999" }, { icono: "888" }, { icono: "888" }],
		};
		const view = render(
			<MemoryRouter>
				<CardProduct
					item={mockProductProm}
					favorite={mockFavorite}
					usuario={mockUsuario}
					onEliminacionFavorito={mockSetValue}
					onDeleteLastFavorite={mockSetValue}
					cantidadResultados={mockCantidadResultados}
				/>
			</MemoryRouter>
		);
		expect(view.getAllByRole("heading")[2]).toHaveTextContent("Desastroso");
	});
});
