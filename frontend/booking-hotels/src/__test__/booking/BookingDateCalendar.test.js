import React from "react";
import { shallow } from "enzyme";
import BookingDateCalendar from "../../component/booking/BookingDateCalendar";
import { render } from "@testing-library/react";
import "@testing-library/jest-dom/extend-expect";
// import "@testing-library/jest-dom";
import userEvent from "@testing-library/user-event";

let mockSetValue = jest.fn();
let wrapper = shallow(<BookingDateCalendar />);
jest.mock("react", () => ({
	...jest.requireActual("react"),
	useState: (initialState) => [initialState, mockSetValue],
}));

describe("<BookingDateCalendar />", () => {
	it("Renderiza un solo h2", () => {
		expect(wrapper.find("h2")).toHaveLength(1);
	});
	it('El elemento "h2" contiene exactamente el texto "Seleccioná tu fecha de reserva"', () => {
		expect(wrapper.find("h2").first().text()).toEqual(
			"Seleccioná tu fecha de reserva"
		);
	});
	it("se prueba la selección de fecha en el calendario", () => {
		const view = render(<BookingDateCalendar onChange={mockSetValue} />);
		//======== Identifica fechas deshabilitadas solamente
		userEvent.click(
			view.getByLabelText("Not available martes, 30 de noviembre de 2021")
		);
		expect(mockSetValue).toHaveBeenCalled();
	});
});
