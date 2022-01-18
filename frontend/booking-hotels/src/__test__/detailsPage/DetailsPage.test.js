import React from "react";
import { shallow, mount } from "enzyme";
import DetailsPage from "../../component/detailsPage/DetailsPage";
import HeaderDetailsPage from "../../component/detailsPage/HeaderDetailsPage";
import Location from "../../component/detailsPage/Location";
import ImageGallery from "../../component/detailsPage/ImageGallery";
import DesktopImageGallery from "../../component/detailsPage/DesktopImageGallery";
import Description from "../../component/detailsPage/Description";
import ProductFeatures from "../../component/detailsPage/ProductFeatures";
import AvailableDate from "../../component/detailsPage/AvailableDate";
import Maps from "../../component/detailsPage/Maps";
import ProductPolicy from "../../component/detailsPage/ProductPolicy";
import { render } from "@testing-library/react";
import { Router, Route } from "react-router-dom";
import { createMemoryHistory } from "history";
import "@testing-library/jest-dom/extend-expect";

const mockProduct = {
	id: 1,
	categoria: {
		titulo: "Hotel urbano",
	},
	nombre: "Hotel mock",
	ciudad: { nombre: "Mendoza" },
	imagenes: [{ imagen: "1" }, { imagen: "2" }],
};

describe("<DetailsPage />", () => {
	it("renderiza el componente 'HeaderDetailsPage'", () => {
		const { getByText } = renderWithRouter(DetailsPage, {
			route: "/localhost:3000/product/1/booking",
			path: "/localhost:3000/product/:id/booking",
		});
		expect(getByText("Hotel mock")).toBeInTheDocument();
	});
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
				<HeaderDetailsPage producto={mockProduct} />
			</Router>
		),
	};
}
//const wrapper = shallow(<DetailsPage />);

// describe("<DetailsPage />", () => {
// 	it("renderiza el componente 'Location'", () => {
// 		expect(wrapper.contains(<Location producto={mockProduct} />)).toBe(true);
// 	});
// 	it("renderiza el componente 'ImageGallery'", () => {
// 		expect(wrapper.contains(<ImageGallery producto={mockProduct} />)).toBe(
// 			true
// 		);
// 	});
// 	it("renderiza un 'div' con la clase detailsPageContainer", () => {
// 		expect(wrapper.find("div").first().hasClass("detailsPageContainer")).toBe(
// 			true
// 		);
// 	});
// 	it("renderiza una 'div' con el componente DesktopImageGallery", () => {
// 		expect(
// 			wrapper
// 				.find("div")
// 				.first()
// 				.contains(<DesktopImageGallery producto={mockProduct} />)
// 		).toBe(true);
// 	});
// 	it("renderiza una 'div' con el componente Description", () => {
// 		expect(wrapper.contains(<Description producto={mockProduct} />)).toBe(true);
// 	});
// 	it("renderiza una 'div' con el componente ProductFeatures", () => {
// 		expect(wrapper.contains(<ProductFeatures producto={mockProduct} />)).toBe(
// 			true
// 		);
// 	});
// 	it("renderiza una 'div' con el componente AvailableDate", () => {
// 		expect(wrapper.contains(<AvailableDate producto={mockProduct} />)).toBe(
// 			true
// 		);
// 	});
// 	it("renderiza una 'div' con el componente Maps", () => {
// 		expect(wrapper.contains(<Maps />)).toBe(true);
// 	});
// 	it("renderiza una 'div' con el componente ProductPolicy", () => {
// 		expect(wrapper.contains(<ProductPolicy />)).toBe(true);
// 	});
// });
