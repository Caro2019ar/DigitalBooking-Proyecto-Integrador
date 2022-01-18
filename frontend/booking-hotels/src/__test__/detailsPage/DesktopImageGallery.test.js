import React from "react";
import { shallow, mount } from "enzyme";
import DesktopImageGallery from "../../component/detailsPage/DesktopImageGallery";
import { MemoryRouter } from "react-router";

const mockProduct = {
	id: 1,
	categoria: {
		titulo: "Hotel urbano",
		urlImagen:
			"https://image.freepik.com/foto-gratis/tipo-complejo-entretenimiento-popular-complejo-piscinas-parques-acuaticos-turquia-mas-5-millones-visitantes-al-ano-amara-dolce-vita-hotel-lujo-recurso-tekirova-kemer_146671-18728.jpg",
	},
	title: "Buena Vista",
	ciudad: { nombre: "Buenos aires" },
	nombre: "Un hotel bastante bonito",
	imagenes: [{ imagen: "1" }, { imagen: "2" }],
};

let findByTestAttr = (wrapper, val) => wrapper.find(`[data-test='${val}']`);
let mockSetValue = jest.fn();
const wrapper = shallow(<DesktopImageGallery producto={mockProduct} />);

jest.mock("react", () => ({
	...jest.requireActual("react"),
	useState: (initialState) => [initialState, mockSetValue],
}));

describe("<DesktopImageGallery/>", () => {
	it("debe renderizar el componente de imagenes", () => {
		expect(wrapper.find("div").first().hasClass("galleryContainer")).toBe(true);
	});
	it('El elemento "p" contiene el texto "Ver más"', () => {
		expect(wrapper.find("p").first().text()).toEqual("Ver más");
	});
	it("click en el elemento 'button' que dispara la función openGallery", () => {
		mockSetValue.mockClear();
		const button = wrapper.find("button").first();
		button.simulate("click", { preventDefault() {} });
		expect(mockSetValue).toHaveBeenCalled();
	});
	/* it("click en el elemento 'button' con clase closeIcon que dispara la función closeGallery", async () => {
		//=============TODO: Encontrar manera de cambiar showGallery de (false) a (true) para mostrar div
		mockSetValue.mockClear();
		const openButton = wrapper.find("button").first();
		await act(async () => {
			openButton.simulate("click", { preventDefault() {} });
		});
		console.log("debug", wrapper.debug());
		const closeButton =findByTestAttr(wrapper, "fontAwesomeIcon" });
		closeButton.simulate("click", { preventDefault() {} });

		wrapper.update();
		expect(mockSetValue).toHaveBeenCalled();
	}); */
});
