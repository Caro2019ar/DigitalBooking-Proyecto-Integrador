import React from "react";
import Paginacion from "../component/componentGlobal/Paginacion";
import "@testing-library/jest-dom/extend-expect";
import { render } from "@testing-library/react";
import { shallow, mount } from "enzyme";
import { MemoryRouter } from "react-router-dom";

const mockPage = 2;
const mockCantidadResultados = 5;
const mockresultadosPorPagina = 10;
describe("<Header/>", () => {
	it("debe encontrar texto en la página", () => {
		const wrapper = mount(
			<MemoryRouter>
				<Paginacion
					location={"/localhost"}
					page={mockPage}
					cantidadResultados={mockCantidadResultados}
					resultadosPorPagina={mockresultadosPorPagina}
				/>
			</MemoryRouter>
		);
		expect(wrapper.find("div").first().hasClass("contenedor")).toBe(true);
	});
});
