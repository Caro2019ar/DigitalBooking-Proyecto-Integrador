import { shallow, mount } from "enzyme";
import AvailableDate from "../../component/detailsPage/AvailableDate";
import { MemoryRouter } from "react-router";

const wrapper = mount(
	<MemoryRouter>
		<AvailableDate />
	</MemoryRouter>
);
let findByTestAttr = (wrapper, val) => wrapper.find(`[data-test='${val}']`);

describe("<AvailableDate/>", () => {
	it("debe renderizar el componente", () => {
		expect(wrapper.find("div").first().hasClass("availableDate")).toBe(true);

	});
	it('El elemento "p" contiene exactamente el texto "Agregá tus fechas de viaje para obtener precios exactos"', () => {
		expect(wrapper.find("p").first().text()).toEqual(
			"Agregá tus fechas de viaje para obtener precios exactos"
		);
	});
});
