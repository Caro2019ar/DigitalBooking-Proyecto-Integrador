import React from "react";
import "@testing-library/jest-dom/extend-expect";
import Buscador from "../../component/index/Buscador";
import { Calendar } from "../../component/index/BuscadorComponentes";
import { render } from "@testing-library/react";
import { MemoryRouter } from "react-router";
import { shallow, mount } from "enzyme";
import routeData from "react-router";

let findByTestAttr = (wrapper, val) => wrapper.find(`[data-test='${val}']`);
const mockSetValue = jest.fn();
jest.mock("react", () => ({
	...jest.requireActual("react"),
	useState: (initialState) => [initialState, mockSetValue],
}));
jest.mock("react-router-dom", () => ({
	...jest.requireActual("react-router"),
	useLocation: jest.fn().mockImplementation(() => {
		return { pathname: "/localhost" };
	}),
}));

describe("Login Page UnitTests", () => {
	// ===== Tentar usar o search=======
	// const useLocation = jest.spyOn(routeData, "useLocation");
	// beforeEach(() => {
	// 	useLocation.mockReturnValue({
	// 		search: "/?ciudad=Mendoza",
	// 	});
	// });
	it("debe encontrar en botón Buscar en la pagina", () => {
		const wrapper = mount(
			<MemoryRouter>
				<Buscador />
			</MemoryRouter>
		);
		expect(wrapper.find("button").first().text()).toContain("Buscar");
	});
});
// describe("<Buscador/>", () => {
// it("debe tener un botón que cuando se busca se setee los mensajes", () => {
// 	const wrapper = shallow(<Buscador onClick={mockSetValue} />);
// 	const button = findByTestAttr(wrapper, "buscarButton");
// 	button.simulate("click", { preventDefault() {} });
// 	expect(mockSetValue).toHaveBeenCalled();
// });
// it("debe renderizar el Calendar", () => {
//   const wrapper = mount(
//     <Buscador>
//       <Calendar />
//     </Buscador>
//   );
//   const calendar = findByTestAttr(wrapper, "calendar");
//   expect(calendar).toHaveLength(1);
// });
// it("debe renderizar el Calendar y ejecutar la búsqueda", () => {
// 	mockSetValue.mockClear();
// 	const wrapper = mount(
// 		<MemoryRouter>
// 			<Buscador>
// 				<Calendar pull_date={mockSetValue} />
// 			</Buscador>
// 		</MemoryRouter>
// 	);
// 	const button = findByTestAttr(wrapper, "buscarButton");
// 	button.simulate("click", { preventDefault() {} });
// 	expect(mockSetValue).toHaveBeenCalled();
// });
// });
