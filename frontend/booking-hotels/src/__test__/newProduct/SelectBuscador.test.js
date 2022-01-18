import { shallow, mount } from "enzyme";
import SelectBuscador from "../../component/newProduct/SelectBuscador";
import { render } from "@testing-library/react";
import "@testing-library/jest-dom/extend-expect";
import axios from "axios";
const apiUrl = "http://localhost:8080";
// const token = "mocktoken";
let mockSetValue = jest.fn();
// jest.mock("react", () => ({
// 	...jest.requireActual("react"),
// 	useState: (initialState) => [initialState, mockSetValue],
// }));
jest.mock("axios");
describe("<SelectBuscador />", () => {
	it("Hace llamada a la URL de ciudades", async () => {
		await axios.get(apiUrl + "/ciudades");
		expect(axios.get).toHaveBeenCalledWith(`${apiUrl}/ciudades`);
	});
	it("Renderiza el SelectBuscador", async () => {
		const view = render(<SelectBuscador onChange={mockSetValue} />);
		expect(view.container).toHaveTextContent("Select");
	});
});
