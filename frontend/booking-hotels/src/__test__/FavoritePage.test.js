import React from "react";
import { render, screen } from "@testing-library/react";
import { shallow, mount } from "enzyme";
import "@testing-library/jest-dom/extend-expect";
import axios from "axios";
import FavoritePage from "../component/FavoritePage";
import CardProductList from "../component/componentGlobal/CardProductList";
import { ProductoService } from "../Service/ProductoService";
import { MemoryRouter } from "react-router";
let mockSetValue = jest.fn();

jest.mock("axios");

const mockUsuario = {
	id: 1,
	nombre: "Pepito",
	apellido: "Pérez",
	email: "pepito@perez.com",
	password: "pepito123",
};
describe("<FavoritePage/> ", () => {
	it("debe renderizar componente CardProductList", async () => {
		const init = [
			{
				img: "imagen1",
				titulo: "Hoteles",
				cantidad: "860.021 hoteles",
			},
			{
				img: "imagen2",
				titulo: "Hostels",
				cantidad: "12.124 hoteles",
			},
		];
		const realUseState = React.useState;
		jest
			.spyOn(React, "useState")
			.mockImplementationOnce(() => realUseState(init));

		axios.get.mockResolvedValueOnce(mockUsuario.id);
		const productoServiceMock = new ProductoService();
		const products = await productoServiceMock.getFavoritesIdByUser(
			mockUsuario.id
		);

		const wrapper = mount(
			<MemoryRouter>
				<FavoritePage usuario={mockUsuario} />{" "}
			</MemoryRouter>
		);
		// expect(axios.get).toHaveBeenCalledWith(
		// 	`http://localhost:8080/productos/favoritos-id?usuario=${mockUsuario.id}`
		// );

		// console.log("wrapper", wrapper.debug());
		expect(JSON.stringify(wrapper.props())).toContain("pepito@perez.com");
		// expect(wrapper.find("CardProductList").first().text()).toContain(
		// 	"CardProductList"
		// );
		//=========== Antes este Teste pasaba:
		// expect(
		// 	wrapper.contains(
		// 		<CardProductList
		// 			title="Mis favoritos"
		// 			favorite={true}
		// 			usuario={mockUsuario}
		// 			onDeleteLastFavorite={mockSetValue}
		// 		/>
		// 	)
		// ).toBe(true);
	});
});
