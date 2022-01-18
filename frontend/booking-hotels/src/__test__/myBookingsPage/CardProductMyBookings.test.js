import { render, screen, within, logRoles } from "@testing-library/react";
import "@testing-library/jest-dom";
import { MemoryRouter } from "react-router";
import { shallow, mount } from "enzyme";
import CardProductMyBookings from "../../component/myBookingsPage/CardProductMyBookings";

const mockItem = {
	producto: {
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
	},
};

const mockSetValue = jest.fn();
jest.mock("react", () => ({
	...jest.requireActual("react"),
	useState: (initialState) => [initialState, mockSetValue],
}));

describe("<CardProductMyBookings />", () => {
	it("Se renderiza el CardProductMyBookings", () => {
		const wrapper = mount(
			<MemoryRouter>
				<CardProductMyBookings product={mockItem} />
			</MemoryRouter>
		);
		// console.log(wrapper.debug());
		expect(wrapper.find("h4")).toHaveLength(1);
		expect(wrapper.find("h4").first().text()).toContain("Hotel urbano");
		expect(wrapper.find("h3").first().text()).toContain("Hotel Bello");
		expect(wrapper.find("h2").first().text()).toContain("CheckIn");
		expect(wrapper.find("h2").at(1).text()).toContain("CheckOut");
	});
});
