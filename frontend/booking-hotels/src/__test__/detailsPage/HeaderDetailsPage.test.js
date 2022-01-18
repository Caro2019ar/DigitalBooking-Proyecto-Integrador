import React from "react";
import { shallow } from "enzyme";
import HeaderDetailsPage from "../../component/detailsPage/HeaderDetailsPage";
const mockProduct = {
	id: 1,
	categoria: {
		titulo: "Hotel urbano",
	},
	nombre: "Hotel mock",
};
const mockTitle = "Hermitage Hotel";
const wrapper = shallow(
	<HeaderDetailsPage producto={mockProduct} title={mockTitle} />
);

describe("<HeaderDetailsPage />", () => {
	it("Renderiza un solo h4", () => {
		expect(wrapper.find("h4")).toHaveLength(1);
	});
	it("Renderiza un solo h3", () => {
		expect(wrapper.find("h2")).toHaveLength(1);
	});
	it("Renderiza un h2 con el texto 'Hermitage Hotel", () => {
		expect(wrapper.find("h2").first().text()).toContain("Hermitage Hotel");
	});
	it('El elemento "h4" contiene exactamente el texto "Hotel urbano"', () => {
		expect(wrapper.find("h4").first().text()).toEqual("Hotel urbano");
	});
});
