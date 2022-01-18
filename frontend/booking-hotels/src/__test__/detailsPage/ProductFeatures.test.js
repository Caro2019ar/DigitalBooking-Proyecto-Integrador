import React from "react";
import { shallow, mount } from "enzyme";
import ProductFeatures from "../../component/detailsPage/ProductFeatures";
import HorizontalLine from "../../component/componentGlobal/HorizontalLine";

const mockProduct = {
	id: 1,
	categoria: {
		titulo: "Hotel urbano",
	},
	nombre: "Hotel mock",
};
const wrapper = shallow(<ProductFeatures producto={mockProduct} />);

describe("<ProductFeatures />", () => {
	it("Renderiza un solo h2", () => {
		expect(wrapper.find("h2")).toHaveLength(1);
	});
	it('El elemento "h2" contiene exactamente el texto "¿Qué ofrece este lugar?"', () => {
		expect(wrapper.find("h2").first().text()).toEqual(
			"¿Qué ofrece este lugar?"
		);
	});
	it("renderiza el componente 'HorizontalLine'", () => {
		expect(wrapper.contains(<HorizontalLine />)).toBe(true);
	});
});
