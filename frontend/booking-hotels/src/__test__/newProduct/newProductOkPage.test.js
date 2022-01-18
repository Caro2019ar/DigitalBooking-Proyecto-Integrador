import { shallow } from "enzyme";
import BookingOkPage from "../../component/newProduct/newProductOkPage";
import { render } from "@testing-library/react";
import { MemoryRouter } from "react-router";
import "@testing-library/user-event";
import "@testing-library/jest-dom/extend-expect";
const wrapper = shallow(<BookingOkPage />);

describe("<newProductOkPage />", () => {
	it("Renderiza un solo h5", () => {
		expect(wrapper.find("h5")).toHaveLength(1);
	});
	it('El elemento "h5" contiene exactamente el texto "Tu propiedad se ha creado con éxito"', () => {
		expect(wrapper.find("h5").first().text()).toEqual(
			"Tu propiedad se ha creado con éxito"
		);
	});
	it("Renderiza un botón de Volver", () => {
		const view = render(
			<MemoryRouter>
				<BookingOkPage />
			</MemoryRouter>
		);
		expect(view.getByRole("link")).toHaveTextContent("Volver");
	});
});
