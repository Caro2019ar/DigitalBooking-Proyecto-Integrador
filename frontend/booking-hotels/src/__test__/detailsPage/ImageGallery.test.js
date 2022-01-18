import React from "react";
import "@testing-library/jest-dom/extend-expect";
import { shallow, mount } from "enzyme";
import ImageGallery from "../../component/detailsPage/ImageGallery";
import { render, screen, waitFor } from "@testing-library/react";
import { MemoryRouter } from "react-router";
import userEvent from "@testing-library/user-event";
import SignInForm from "../../component/SignInForm";

const mockProduct = {
	id: 1,
	categoria: {
		titulo: "Hotel urbano",
	},
	nombre: "Hotel mock",
	imagenes: ["1", "2", "3"],
	ciudad: { nombre: "Mendoza", pais: "Argentina" },
};
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

describe("<ImageGallery />", () => {
	it("Renderiza div con clase galleryContainer", () => {
		const wrapper = shallow(
			<ImageGallery producto={mockProduct} usuario={mockUsuario} />
		);
		expect(wrapper.find("div").first().hasClass("galleryContainer")).toBe(true);
	});
	it("Renderiza una div con clase galleryLibraryContainer", () => {
		const wrapper = shallow(
			<ImageGallery producto={mockProduct} usuario={mockUsuario} />
		);
		expect(wrapper.find("div").at(9).hasClass("galleryLibraryContainer")).toBe(
			true
		);
	});
	it("debe encontrar el span de favorite", () => {
		const wrapper = shallow(
			<ImageGallery producto={mockProduct} usuario={mockUsuario} />
		);
		expect(wrapper.find("span").first().hasClass("favoriteIconNo")).toBe(true);
	});
	it("debe encontrar icono de compartir", () => {
		const wrapper = shallow(
			<ImageGallery producto={mockProduct} usuario={mockUsuario} />
		);
		expect(wrapper.find("div").at(3).hasClass("shareIconContainer")).toBe(true);
		const favoriteIcon = wrapper.find("div").at(3);
		favoriteIcon.simulate("click", { preventDefault() {} });
		//console.log(wrapper.debug());

		// expect(wrapper.find("img").first().getDOMNode()).toHaveAttribute(
		// 	"src",
		// 	"imageShareOnClick"
		// );
	});
});

//========================== Testar teste de integração com login
// it("debe loguerarse, renderizar el icono de favorito y clickearlo", async () => {
// 	//mockSetValue.mockClear();
// 	const view = render(
// 		<MemoryRouter>
// 			<SignInForm />
// 		</MemoryRouter>
// 	);
// 	const inputEmail = view.getByLabelText("Correo Electrónico");
// 	userEvent.type(inputEmail, "argente2019@gmail.com");
// 	const inputPassword = view.getByLabelText("Contraseña");
// 	userEvent.type(inputPassword, "111111");
// 	userEvent.click(view.getByRole("button"));
// 	const wrapper = mount(
// 		<ImageGallery producto={mockProduct} usuario={mockUsuario} />
// 	);
// 	//console.log(wrapper.debug());
// 	const favoriteIcon = wrapper.find("span").first();
// 	favoriteIcon.simulate("click", { preventDefault() {} });
// 	// const favoriteOK = await waitFor(() =>
// 	// 	wrapper.find("span").first().hasClass("favoriteIconYes")
// 	// );
// 	expect(wrapper.find("span").first().hasClass("favoriteIconYes")).toBe(true);
// });
