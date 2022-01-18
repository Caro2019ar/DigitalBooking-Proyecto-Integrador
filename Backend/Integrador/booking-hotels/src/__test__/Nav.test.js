import React from "react";
import Nav from "../component/header/Nav";
import "@testing-library/jest-dom/extend-expect";
import { shallow } from "enzyme";
import { render } from "@testing-library/react";
import { MemoryRouter } from "react-router";
import userEvent from "@testing-library/user-event";

let findByTestAttr = (wrapper, val) => wrapper.find(`[data-test='${val}']`);
let mockSetValue = jest.fn();
let wrapper = shallow(<Nav />);
jest.mock("react", () => ({
	...jest.requireActual("react"),
	useState: (initialState) => [initialState, mockSetValue],
}));

jest.mock("react-router-dom", () => ({
	useLocation: jest.fn().mockReturnValue({
		pathname: "/localhost",
	}),
}));

describe("<Nav/>", () => {
	it("tiene elemento 'h5' con la palabra 'MENÚ'", () => {
		expect(wrapper.find("h5").first().text()).toContain("MENÚ");
	});

	it("click en el elemento 'button' de hamburguesa", () => {
		const hamburguesaButton = wrapper.find("button").first();
		hamburguesaButton.simulate("click", { preventDefault() {} });
		expect(wrapper.contains("MENÚ")).toBe(true);
	});
	// it("debe tener un botón que cierre el menu", () => {
	// 	wrapper = shallow(<Nav onClick={mockSetValue} />);
	// 	const button = findByTestAttr(wrapper, "buttonHideMenu");
	// 	button.simulate("click", { preventDefault() {} });
	// 	expect(mockSetValue).toHaveBeenCalled();
	// });
	// it("debe tener un botón para cerrar sesión", () => {
	// 	mockSetValue.mockClear();
	// 	wrapper = shallow(<Nav onClick={mockSetValue} />);
	// 	const button = findByTestAttr(wrapper, "buttonCerrarSesion");
	// 	button.simulate("click", { preventDefault() {} });
	// 	expect(mockSetValue).toHaveBeenCalled();
	// });
});
