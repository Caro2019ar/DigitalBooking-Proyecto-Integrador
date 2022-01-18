import React from "react";
import { shallow } from "enzyme";
import BookingOkPage from "../../component/booking/BookingOkPage";

const wrapper = shallow(<BookingOkPage />);

describe("<BookingOkPage />", () => {
	it("Renderiza un solo h3", () => {
		expect(wrapper.find("h3")).toHaveLength(1);
	});
	it('El elemento "h3" contiene exactamente el texto "¡Muchas gracias!"', () => {
		expect(wrapper.find("h3").first().text()).toEqual("¡Muchas gracias!");
	});
	it('El elemento "h5" contiene exactamente el texto "Su reserva se ha realizado con éxito"', () => {
		expect(wrapper.find("h5").first().text()).toEqual(
			"Su reserva se ha realizado con éxito"
		);
	});
});
