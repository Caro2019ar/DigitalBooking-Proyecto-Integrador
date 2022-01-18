import React from "react";
import { shallow } from "enzyme";
import HeaderDetailsPage from "../../component/detailsPage/HeaderDetailsPage";

const wrapper = shallow(<HeaderDetailsPage />);

describe("<HeaderDetailsPage />", () => {
	it("Renderiza un solo h4", () => {
		expect(wrapper.find("h4")).toHaveLength(1);
	});
	it("Renderiza un solo h3", () => {
		expect(wrapper.find("h3")).toHaveLength(1);
	});
	it("Renderiza un h3 con el texto 'Hermitage Hotel", () => {
		expect(wrapper.find("h3").text()).toContain("Hermitage Hotel");
	});
	it('El elemento "h4" contiene exactamente el texto "Hotel"', () => {
		expect(wrapper.find("h4").first().text()).toEqual("Hotel");
	});
});
