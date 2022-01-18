import Footer from "../component/componentGLobal/Footer";
import "@testing-library/jest-dom/extend-expect";
import { render } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";

describe("Footer", () => {
	it("debe encontrar texto en la página", async () => {
		const component = render(
			<BrowserRouter>
				<Footer />
			</BrowserRouter>
		);
		expect(component.container).toHaveTextContent(/©2021 Digital Booking/i);
	});
});
