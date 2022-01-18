import React from "react";
import { shallow, mount } from "enzyme";
import Location from "../../component/detailsPage/Location";

const mockUsuario = {
	id: 1,
	nombre: "Pepe",
	apellido: "Pérez",
	email: "pepito@perez.com",
	password: "pepito123",
};
const mockProduct = {
	id: 1,
	categoria: {
		titulo: "Hotel urbano",
	},
	descripcion: "Hotel mock",
	nombre: "Hotel Bello",
	imagenes: ["1", "2"],
	ciudad: { nombre: "Mendoza" },
	valoracion: { cantidadVotos: 0, puntajeTotal: 10 },
	caracteristicas: [{ icono: "999" }, { icono: "888" }, { icono: "888" }],
};
const wrapper = shallow(
	<Location producto={mockProduct} usuario={mockUsuario} />
);

describe("<Location />", () => {
	it("Renderiza tres h4", () => {
		expect(wrapper.find("h4")).toHaveLength(3);
	});

	it("Renderiza una div con clase textLocationContainer", () => {
		expect(wrapper.find("div").at(1).hasClass("textLocationContainer")).toBe(
			true
		);
	});
	it("Renderiza una div con clase reviewContainer", () => {
		expect(wrapper.find("div").at(3).hasClass("reviewContainer")).toBe(true);
	});
});
