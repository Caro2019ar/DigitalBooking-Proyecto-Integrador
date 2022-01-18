import React from "react";
import { shallow } from "enzyme";
import FormBooking from "../../component/booking/FormBooking";

const mockUsuario = { nombre: "Pepe" };
const wrapper = shallow(<FormBooking usuario={mockUsuario} />);

describe("<FormBooking />", () => {
	it("Renderiza un solo h2", () => {
		expect(wrapper.find("h2")).toHaveLength(1);
	});
	it('El elemento "h2" contiene exactamente el texto "Completá tus datos"', () => {
		expect(wrapper.find("h2").first().text()).toEqual("Completá tus datos");
	});
	it('El elemento "p" contiene el texto "Nombre"', () => {
		expect(wrapper.find("p").first().text()).toContain("Nombre");
	});
	it('El elemento "p" contiene el texto "Apellido"', () => {
		expect(wrapper.find("p").at(1).text()).toContain("Apellido");
	});
	it('El elemento "p" contiene el texto "Correo electrónico"', () => {
		expect(wrapper.find("p").at(2).text()).toContain("Correo electrónico");
	});
	it('El elemento "p" contiene el texto "Ciudad"', () => {
		expect(wrapper.find("p").at(3).text()).toContain("Ciudad");
	});
	it('El elemento "p" contiene el texto "Información COVID"', () => {
		expect(wrapper.find("p").at(4).text()).toContain("Información COVID");
	});
	it('El elemento "p" contiene el texto "Datos de interés para el vendedor"', () => {
		expect(wrapper.find("p").at(5).text()).toContain(
			"Datos de interés para el vendedor"
		);
	});
	it('El elemento "p" contiene el texto "Está vacunado contra el COVID-19"', () => {
		expect(wrapper.find("p").at(6).text()).toContain(
			"Está vacunado contra el COVID-19"
		);
	});
});
