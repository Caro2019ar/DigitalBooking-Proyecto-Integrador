import Main from "../../component/index/Main";
import "@testing-library/jest-dom/extend-expect";
import { render, screen } from "@testing-library/react";
import { shallow, mount } from "enzyme";
import { MemoryRouter } from "react-router";
import { createMemoryHistory } from "history";
import { Router, Route } from "react-router-dom";
const mockUsuario = {
	id: 1,
	nombre: "Pepe",
	apellido: "Pérez",
	email: "pepito@perez.com",
	password: "pepito123",
};
const mockSetValue = jest.fn();
jest.mock("react", () => ({
	...jest.requireActual("react"),
	useState: (initialState) => [initialState, mockSetValue],
}));

describe("<Main/>", () => {
	it("Debe renderizar las categorias", () => {
		const { debug } = renderWithRouter(Main, {
			route: "/localhost",
			path: "/localhost",
		});
		// debug();
		expect(screen.getByRole("main")).toBeInTheDocument();
	});
});

function renderWithRouter(
	ui,
	{
		path = "/",
		route = "/",
		usuario = { mockUsuario },
		onIniciarSesion = { mockSetValue },
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
