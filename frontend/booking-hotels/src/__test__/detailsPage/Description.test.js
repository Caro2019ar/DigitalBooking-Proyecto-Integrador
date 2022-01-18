import React from "react";
import { shallow, mount } from "enzyme";
import Description from "../../component/detailsPage/Description";

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
	caracteristicas: [
		{ nombre: "WiFi", icono: "fas fa-wifi" },
		{ nombre: "Piscina", icono: "fas fa-swimmer" },
	],
};

const wrapper = shallow(<Description producto={mockProduct} />);

describe("<Description />", () => {
	it("Renderiza un solo h2", () => {
		expect(wrapper.find("h2")).toHaveLength(1);
	});
	it('El elemento "h2" contiene el texto "Mendoza"', () => {
		expect(wrapper.find("h2").first().text()).toContain("Mendoza");
	});
	it('El elemento "p" contiene el texto', () => {
		expect(wrapper.find("p").first().text()).toContain("Hotel mock");
	});
});
