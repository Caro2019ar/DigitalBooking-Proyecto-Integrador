import React from "react";
import { shallow, mount } from "enzyme";
import Location from "../../component/detailsPage/Location";

const wrapper = shallow(<Location />);

describe("<Location />", () => {
	it("Renderiza tres h4", () => {
		expect(wrapper.find("h4")).toHaveLength(3);
	});
	it('El elemento "h4" contiene exactamente el texto "Buenos aires, Cartagena, Colombia"', () => {
		expect(wrapper.find("h4").first().text()).toEqual(
			"Buenos Aires, Cartagena, Colombia"
		);
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
