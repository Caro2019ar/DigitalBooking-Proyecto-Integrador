import axios from "axios";
import { ReservaService } from "../../Service/ReservaService";
import "@testing-library/jest-dom/extend-expect";

jest.mock("axios");
export const BASE_URL = "http://localhost:8080/reservas";

describe("<ReservaService/>", () => {
	it("debe probar el registro de reserva", async () => {
		const reservaMock = [
			{
				apellido: "perez",
				contrasena: "pepito123",
				email: "pepito@perez.com",
				nombre: "Pepito",
				checkin: "01/01/2022",
				checkout: "31/01/2022",
			},
		];
		axios.post.mockResolvedValueOnce(reservaMock);

		const reservaServiceMock = new ReservaService();
		await reservaServiceMock.guardarReserva(reservaMock);
		expect(axios.post).toHaveBeenCalled();
		// expect(axios.post).toHaveBeenCalledWith(
		// 	`${BASE_URL}/clientes-disabled`,
		// 	`${JSON.stringify(users)}`
		// );
	});
});
