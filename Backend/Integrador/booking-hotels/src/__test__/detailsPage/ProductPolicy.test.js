import React from "react";
import { shallow, mount } from "enzyme";
import ProductPolicy from "../../component/detailsPage/ProductPolicy";

const wrapper = shallow(<ProductPolicy />);

describe("<ProductPolicy />", () => {
	it("Renderiza un solo h2", () => {
		expect(wrapper.find("h2")).toHaveLength(1);
	});
	it('El elemento "h2" contiene exactamente el texto "Qué tienes que saber"', () => {
		expect(wrapper.find("h2").first().text()).toEqual("Qué tienes que saber");
	});
});
