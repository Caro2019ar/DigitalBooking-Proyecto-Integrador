import { render, screen, within, logRoles } from "@testing-library/react";
import "@testing-library/jest-dom";
import { MemoryRouter } from "react-router";
import { shallow, mount } from "enzyme";
import MyReviewsPage from "../../component/myReviewsPage/MyReviewsPage";
import { ProductoService } from "../../Service/ProductoService";
import axios from "axios";
jest.mock("axios");
const mockUsuario = {
	id: 1,
	nombre: "Pepe",
	apellido: "Pérez",
	email: "pepito@perez.com",
	password: "pepito123",
	rol: "user",
};

describe("<MyReviewsPage />", () => {
	it("Se renderiza MyReviewsPage", async () => {
		axios.get.mockResolvedValueOnce(mockUsuario.id);
		const productoServiceMock = new ProductoService();
		const products =
			await productoServiceMock.getProductsByPuntuacionFromUsuario(
				mockUsuario.id
			);
		const wrapper = mount(
			<MemoryRouter>
				<MyReviewsPage user={mockUsuario} />
			</MemoryRouter>
		);
		// console.log("wrapper", wrapper.debug());
		expect(wrapper.find("div").at(2).hasClass("productsCardContainer")).toBe(
			true
		);
	});
});
