import React from "react";
import "@testing-library/jest-dom/extend-expect";
import Buscador from "../component/Buscador";
import { Calendar } from "../component/BuscadorComponentes";
import { render } from "@testing-library/react";
import { MemoryRouter } from "react-router";
import { shallow, mount } from "enzyme";

let findByTestAttr = (wrapper, val) => wrapper.find(`[data-test='${val}']`);
const mockSetValue = jest.fn();
jest.mock("react", () => ({
	...jest.requireActual("react"),
	useState: (initialState) => [initialState, mockSetValue],
}));

describe("<Buscador/>", () => {
	it("debe encontrar texto en la página", () => {
		const component = render(
			<MemoryRouter>
				<Buscador />
			</MemoryRouter>
		);
		expect(document.body.textContent).toMatch(/Buscar/i);
	});
	it("debe tener un botón de búsqueda", () => {
		const wrapper = mount(<Buscador />);
		const button = findByTestAttr(wrapper, "buscarButton");
		expect(button.exists()).toBe(true);
	});
	it("debe tener un botón que cuando se busca se setee los mensajes", () => {
		const wrapper = shallow(<Buscador onClick={mockSetValue} />);
		const button = findByTestAttr(wrapper, "buscarButton");
		button.simulate("click", { preventDefault() {} });
		expect(mockSetValue).toHaveBeenCalled();
	});
	it("debe renderizar el Calendar", () => {
		const wrapper = mount(
			<Buscador>
				<Calendar />
			</Buscador>
		);
		const calendar = findByTestAttr(wrapper, "calendar");
		expect(calendar).toHaveLength(1);
	});
	it("debe renderizar el Calendar y ejecutar la búsqueda", () => {
		mockSetValue.mockClear();
		const wrapper = mount(
			<MemoryRouter>
				<Buscador>
					<Calendar pull_date={mockSetValue} />
				</Buscador>
			</MemoryRouter>
		);
		const button = findByTestAttr(wrapper, "buscarButton");
		button.simulate("click", { preventDefault() {} });
		expect(mockSetValue).toHaveBeenCalled();
	});
});
