import { render, screen, within, logRoles } from "@testing-library/react";
import "@testing-library/jest-dom";
import { MemoryRouter } from "react-router";
import { shallow, mount } from "enzyme";
import MyBookingsPage from "../../component/myBookingsPage/MyBookingsPage";
import { ReservaService } from "../../Service/ReservaService";
import axios from "axios";
jest.mock("axios");
const mockUsuario = {
	id: 1,
	nombre: "Pepe",
	apellido: "Pérez",
	email: "pepito@perez.com",
	password: "pepito123",
	role: "user",
};
// const mockSetValue = jest.fn();
// jest.mock("react", () => ({
// 	...jest.requireActual("react"),
// 	useState: (initialState) => [initialState, mockSetValue],
// }));

describe("<MyBookingsPage />", () => {
	it("Se renderiza MyBookingsPage", async () => {
		axios.get.mockResolvedValueOnce(mockUsuario.id);
		const reservaServiceMock = new ReservaService();
		const products = await reservaServiceMock.reservaPorIdUsuario(
			mockUsuario.id
		);
		const wrapper = mount(
			<MemoryRouter>
				<MyBookingsPage user={mockUsuario} />
			</MemoryRouter>
		);
		console.log("1", wrapper.debug());
		expect(wrapper.find("div").at(2).hasClass("productsCardContainer")).toBe(
			true
		);
	});
	
});
