import axios from "axios";
import { CityService } from "../../Service/CityService";

const BASE_URL = "http://localhost:8080/ciudades";

jest.mock("axios");

describe("<CityService/>", () => {
	it("debe probar el endpoint de 'ciudades' que trae todas las ciudades", async () => {
		const mockCiudad = "Mendoza";
		axios.get.mockResolvedValueOnce(mockCiudad);
		const cityServiceMock = new CityService();
		await cityServiceMock.getAllCity();
		//=====VIENE CON TOKEN: expect(axios.get).toHaveBeenCalledWith(`${BASE_URL}`);
		expect(axios.get).toHaveBeenCalled();
	});
});
