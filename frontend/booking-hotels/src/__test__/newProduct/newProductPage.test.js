import React from "react";
import { shallow, mount } from "enzyme";
import NewProductPage from "../../component/newProduct/NewProductPage";
import { render } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import { MemoryRouter } from "react-router";
import { Router, Route } from "react-router-dom";
import { createMemoryHistory } from "history";
import { ProductoService } from "../../Service/ProductoService";
import "@testing-library/jest-dom/extend-expect";
import axios from "axios";
jest.mock("axios");
const apiUrl = "http://localhost:8080";
const mockUsuario = {
	id: 1,
	nombre: "Pepe",
	apellido: "Pérez",
	email: "pepito@perez.com",
	password: "pepito123",
	rol: "otro",
};
// let mockSetValue = jest.fn();
// jest.mock("react", () => ({
// 	...jest.requireActual("react"),
// 	useState: (initialState) => [initialState, mockSetValue],
// }));

describe("<NewProductPage />", () => {
	it("Hace llamada a la URL de caracteristicas", async () => {
		await axios.get(apiUrl + "/caracteristicas");
		expect(axios.get).toHaveBeenCalledWith(`${apiUrl}/caracteristicas`);
	});

	it("Caso del Loader", async () => {
		// await axios.get(apiUrl + "/categorias");
		const wrapper = mount(
			<BrowserRouter>
				<NewProductPage usuario={mockUsuario} />
			</BrowserRouter>
		);
		console.log(wrapper.debug());
		expect(wrapper.find("div").first()).hasClass("lds-hourglass").toBe(true);
		//await waitForElementToBeRemoved(() => screen.getByTestId("loader"));
		// expect(wrapper.find("h2").first().text()).toEqual("Crear propiedad");
	});
	// it("Renderiza el titulo 'Crear propiedad'", () => {
	// 	const { getByText } = renderWithRouter(NewProductPage, {
	// 		route: "/localhost:3000/admin/newProduct",
	// 		path: "/localhost:3000/admin/newProduct",
	// 	});
	//expect(getByText("Crear propiedad")).toBeInTheDocument();
	// });
});
function renderWithRouter(
	ui,
	{
		path = "/",
		route = "/",
		history = createMemoryHistory({ initialEntries: [route] }),
	} = {}
) {
	return {
		...render(
			<Router history={history}>
				<Route path={path} component={ui} />
			</Router>
		),
	};
}
