import React from "react";
import { shallow } from "enzyme";
import RegistroOk from "../component/RegistroOk";

const wrapper = shallow(<RegistroOk />);

describe("<RegistroOk />", () => {
	it("Renderiza un solo h3", () => {
		expect(wrapper.find("h3")).toHaveLength(1);
	});
	it('El elemento "h3" contiene exactamente el texto "¡Muchas gracias!"', () => {
		expect(wrapper.find("h3").first().text()).toEqual("¡Muchas gracias!");
	});
	it('El elemento "h5" contiene exactamente el texto "Su registro se ha realizado con éxito."', () => {
		expect(wrapper.find("h5").first().text()).toEqual(
			"Su registro se ha realizado con éxito."
		);
	});
	it('El elemento "h5" contiene exactamente el texto "Por favor, verifique su casilla de correo. Pronto recibirá un email para confirmar su cuenta."', () => {
		expect(wrapper.find("h5").at(1).text()).toEqual(
			"Por favor, verifique su casilla de correo. Pronto recibirá un email para confirmar su cuenta."
		);
	});
});
