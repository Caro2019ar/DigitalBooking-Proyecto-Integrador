import React from "react";
import Header from "../component/header/Header";
import "@testing-library/jest-dom/extend-expect";
import { render } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";

describe("<Header/>", () => {
	it("debe encontrar texto en la página", () => {
		const textoBuscado = /Sentite como en tu hogar/i;
		const component = render(
			<BrowserRouter>
				<Header location={"/localhost"} />
			</BrowserRouter>
		);
		expect(component.container).toHaveTextContent(textoBuscado);
	});
});
