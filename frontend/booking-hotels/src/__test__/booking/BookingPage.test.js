import React from "react";
import { render } from "@testing-library/react";
import BookingPage from "../../component/booking/BookingPage";
import FormBooking from "../../component/booking/FormBooking";
import HeaderDetailsPage from "../../component/detailsPage/HeaderDetailsPage";
import { Router, Route } from "react-router-dom";
import { createMemoryHistory } from "history";
import "@testing-library/jest-dom/extend-expect";

const mockProduct = {
	id: 1,
	categoria: {
		titulo: "Hotel urbano",
	},
	nombre: "Hotel mock",
};

describe("<BookingPage />", () => {
	it("Se prueba que se renderice el producto del componente hijo", () => {
		const { getByText } = renderWithRouter(BookingPage, {
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


