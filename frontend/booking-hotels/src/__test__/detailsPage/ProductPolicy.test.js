import React from "react";
import { shallow, mount } from "enzyme";
import ProductPolicy from "../../component/detailsPage/ProductPolicy";
const mockTitle = "Qué tienes que saber";
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
	politica: {
		normas: "respecto",
		saludYSeguridad: "limpieza",
		cancelacion: "24hs",
	},
};
const wrapper = shallow(
	<ProductPolicy title={mockTitle} producto={mockProduct} />
);
describe("<ProductPolicy />", () => {
	it("Renderiza un solo h2", () => {
		expect(wrapper.find("h2")).toHaveLength(1);
	});
	it('El elemento "h2" contiene exactamente el texto "Qué tienes que saber"', () => {
		expect(wrapper.find("h2").first().text()).toEqual("Qué tienes que saber");
	});
});
