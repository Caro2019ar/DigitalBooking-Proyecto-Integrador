import { shallow, mount } from "enzyme";
import Product from "../../component/booking/Product";
import "@testing-library/jest-dom/extend-expect";
import { MemoryRouter } from "react-router";
import { ReservaService } from "../../Service/ReservaService";
import axios from "axios";
jest.mock("axios");
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
};
const mockUsuario = {
	id: 1,
	nombre: "Pepe",
	apellido: "Pérez",
	email: "pepito@perez.com",
	password: "pepito123",
	rol: "user",
};
const wrapper = mount(
	<MemoryRouter>
		<Product
			producto={mockProduct}
			usuario={mockUsuario}
			startDate={new Date()}
			endDate={new Date()}
			city={mockProduct.ciudad.nombre}
			infoCovid="sintomas"
			vacunadoCovid="si"
			entryTime="04:00"
			className="cardProduct"
		/>
	</MemoryRouter>
);

describe("<Product />", () => {
	it("Renderiza un solo h2", () => {
		expect(wrapper.find("h2")).toHaveLength(1);
	});
	it('El elemento "h2" contiene exactamente el texto "Detalle de la reserva"', () => {
		expect(wrapper.find("h2").first().text()).toEqual("Detalle de la reserva");
	});
	it('El segundo elemento "p" contiene exactamente el texto "Check in"', () => {
		expect(wrapper.find("p").at(1).text()).toEqual("Check in");
	});
	it('El tercer elemento "p" contiene exactamente el texto "Check out"', () => {
		expect(wrapper.find("p").at(2).text()).toEqual("Check out");
	});
	it('El elemento "button" contiene exactamente el texto "Confirmar reserva"', () => {
		expect(wrapper.find("button").first().text()).toEqual("Confirmar reserva");
	});
	it("Se renderiza Product", async () => {
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
		const wrapper = mount(
			<MemoryRouter>
				<Product
					producto={mockProduct}
					usuario={mockUsuario}
					startDate={new Date()}
					endDate={new Date()}
					city={mockProduct.ciudad.nombre}
					infoCovid="sintomas"
					vacunadoCovid="si"
					entryTime="04:00"
					className="cardProduct"
				/>
			</MemoryRouter>
		);
		// console.log("wrapper", wrapper.debug());
		expect(axios.post).toHaveBeenCalled();
	});
	// it("Sin informar ciudad", () => {
	// 	const wrapper = mount(
	// 		<MemoryRouter>
	// 			<Product
	// 				producto={mockProduct}
	// 				usuario={mockUsuario}
	// 				startDate={new Date()}
	// 				endDate={new Date()}
	// 				city={false}
	// 				infoCovid="sintomas"
	// 				vacunadoCovid="si"
	// 				entryTime="04:00"
	// 				className="cardProduct"
	// 			/>
	// 		</MemoryRouter>
	// 	);
	// 	expect(wrapper.find("div").at(14).text()).toContain(
	// 		"Por favor, informe la ciudad"
	// 	);
	// });
});
