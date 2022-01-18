import React from "react";
import { shallow, mount } from "enzyme";
import Description from "../../component/detailsPage/Description";

const wrapper = shallow(<Description />);

describe("<Description />", () => {
	it("Renderiza un solo h2", () => {
		expect(wrapper.find("h2")).toHaveLength(1);
	});
	it('El elemento "h2" contiene exactamente el texto "Alójate en el corazón de Buenos Aires"', () => {
		expect(wrapper.find("h2").first().text()).toEqual(
			"Alójate en el corazón de Buenos Aires"
		);
	});
	it('El elemento "p" contiene el texto', () => {
		expect(wrapper.find("p").first().text()).toContain(
			"Está situado a solo unas calles de la avenida Alvear, de la avenida Quintana, del parque San Martín y del distrito de Recoleta. En las inmediaciones también hay varios lugares de interés, como la calle Florida, el centro comercial Galerías Pacífico, la zona de Puerto Madero, la plaza de Mayo y el palacio Municipal."
		);
	});
	it('El segundo elemento "p" contiene el texto', () => {
		expect(wrapper.find("p").at(1).text()).toContain(
			"Nuestros clientes dicen que esta parte de Buenos Aires es su favorita, según los comentarios independientes."
		);
	});
	it('El tercer elemento "p" contiene el texto', () => {
		expect(wrapper.find("p").at(2).text()).toContain(
			"El Hotel es un hotel sofisticado de 4 estrellas que goza de una ubicación tranquila, a poca distancia de prestigiosas galerías de arte, teatros, museos y zonas comerciales. Además, hay WiFi gratuita. El establecimiento sirve un desayuno variado de 07:00 a 10:30."
		);
	});
});
