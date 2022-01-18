import React from "react";
import "@testing-library/jest-dom/extend-expect";
import { shallow } from "enzyme";
import { render } from "@testing-library/react";
import { MemoryRouter } from "react-router";
import Maps from "../../component/detailsPage/Maps";
import HorizontalLine from "../../component/HorizontalLine";

const wrapper = shallow(<Maps />);
describe("<Maps/>", () => {
	it("debe encontrar texto en la página", () => {
		const component = render(
			<MemoryRouter>
				<Maps />
			</MemoryRouter>
		);
		expect(document.body.textContent).toMatch(/Dónde vas a estar/i);
	});
	it("renderiza el componente 'HorizontalLine'", () => {
		expect(wrapper.contains(<HorizontalLine />)).toBe(true);
	});
	it("debe renderizar una div con el texto 'Buenos Aires, Argentina'", () => {
		expect(wrapper.find("div").at(1).text()).toEqual("Buenos Aires, Argentina");
	});
	it("debe renderizar una img con la clase 'imgMap'", () => {
		expect(wrapper.find("img").first().hasClass("imgMap")).toBe(true);
	});
});
