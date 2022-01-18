import React from "react";
import { shallow } from "enzyme";
import Entrytime from "../../component/booking/Entrytime";

const wrapper = shallow(<Entrytime />);

describe("<Entrytime />", () => {
	it("Renderiza un solo h2", () => {
		expect(wrapper.find("h2")).toHaveLength(1);
	});
	it('El elemento "h2" contiene exactamente el texto "Tu horario de llegada"', () => {
		expect(wrapper.find("h2").first().text()).toEqual("Tu horario de llegada");
	});
	it('El elemento "p" contiene exactamente el texto "Indicá tu horario estimado de llegada"', () => {
		expect(wrapper.find("p").first().text()).toEqual(
			"Indicá tu horario estimado de llegada"
		);
	});
});
