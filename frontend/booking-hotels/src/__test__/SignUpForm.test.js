import React from "react";
import { render, screen, within, logRoles } from "@testing-library/react";
import "@testing-library/jest-dom";
import "@testing-library/user-event";
import { MemoryRouter } from "react-router";

import SignUpForm from "../component/SignUpForm";
import userEvent from "@testing-library/user-event";

describe("<SignUpForm />", () => {
	it('Existe un título con el texto "Crear cuenta"', () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<SignUpForm />{" "}
			</MemoryRouter>
		);
		expect(view.queryByRole("heading")).toBeInTheDocument();
		expect(view.getByRole("heading")).toHaveTextContent("Crear cuenta");
	});

	it("Existe un form", () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<SignUpForm />{" "}
			</MemoryRouter>
		);
		expect(view.queryByRole("form")).toBeInTheDocument();
	});

	it('Dentro del form existe un input de tipo "text" que tiene un label asociado con el texto "Nombre"', () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<SignUpForm />{" "}
			</MemoryRouter>
		);
		// El ByLabelText devuelve un INPUT
		expect(
			within(view.getByRole("form")).queryByLabelText("Nombre")
		).toBeInTheDocument();
		expect(
			within(view.getByRole("form")).getByLabelText("Nombre")
		).toHaveAttribute("type", "text");
	});

	it('Dentro del form existe un input de tipo "text" que tiene un label asociado con el texto "Apellido"', () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<SignUpForm />{" "}
			</MemoryRouter>
		);
		// El ByLabelText devuelve un INPUT
		expect(
			within(view.getByRole("form")).queryByLabelText("Apellido")
		).toBeInTheDocument();
		expect(
			within(view.getByRole("form")).getByLabelText("Apellido")
		).toHaveAttribute("type", "text");
	});

	it('Dentro del form existe un input de tipo "email" que tiene un label asociado con el texto "Correo electrónico"', () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<SignUpForm />{" "}
			</MemoryRouter>
		);
		// El ByLabelText devuelve un INPUT
		expect(
			within(view.getByRole("form")).queryByLabelText("Correo electrónico")
		).toBeInTheDocument();
		expect(
			within(view.getByRole("form")).getByLabelText("Correo electrónico")
		).toHaveAttribute("type", "email");
	});

	it('Dentro del form existe un input de tipo "password" que tiene un label asociado con el texto "Contraseña"', () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<SignUpForm />{" "}
			</MemoryRouter>
		);
		// El ByLabelText devuelve un INPUT
		expect(
			within(view.getByRole("form")).queryByLabelText("Contraseña")
		).toBeInTheDocument();
		expect(
			within(view.getByRole("form")).getByLabelText("Contraseña")
		).toHaveAttribute("type", "password");
	});

	it('Dentro del form existe un input de tipo "password" que tiene un label asociado con el texto "Confirmar contraseña"', () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<SignUpForm />{" "}
			</MemoryRouter>
		);
		// El ByLabelText devuelve un INPUT
		expect(
			within(view.getByRole("form")).queryByLabelText("Confirmar contraseña")
		).toBeInTheDocument();
		expect(
			within(view.getByRole("form")).getByLabelText("Confirmar contraseña")
		).toHaveAttribute("type", "password");
	});

	it('Dentro del form existe un botón con el texto "Crear Cuenta" y está habilitado', () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<SignUpForm />{" "}
			</MemoryRouter>
		);
		expect(
			within(view.getByRole("form")).queryByRole("button")
		).toBeInTheDocument();
		expect(
			within(view.getByRole("form")).getByRole("button")
		).toHaveTextContent("Crear Cuenta");
		expect(within(view.getByRole("form")).getByRole("button")).toBeEnabled();
	});

	it("El form renderiza los datos que se ingresan en los input", () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<SignUpForm />{" "}
			</MemoryRouter>
		);

		userEvent.type(view.getByLabelText("Nombre"), "nombre-ingresado");
		userEvent.type(view.getByLabelText("Apellido"), "apellido-ingresado");
		userEvent.type(
			view.getByLabelText("Correo electrónico"),
			"email-ingresado"
		);
		userEvent.type(view.getByLabelText("Contraseña"), "password-ingresado");
		userEvent.type(
			view.getByLabelText("Confirmar contraseña"),
			"password-repetido-ingresado"
		);

		//userEvent.click(view.getByRole('button'));

		expect(view.getByRole("form")).toHaveFormValues({
			firstName: "nombre-ingresado",
			lastName: "apellido-ingresado",
			email: "email-ingresado",
			password: "password-ingresado",
			"password-repeat": "password-repetido-ingresado",
		});
	});

	it("El form muestra mensajes de error si se intenta crear una cuenta con los input vacíos", () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<SignUpForm />{" "}
			</MemoryRouter>
		);

		// Probablemente no haga falta hacer esto (los input's están vacíos desde el principio del test)
		userEvent.type(view.getByLabelText("Nombre"), "");
		userEvent.type(view.getByLabelText("Apellido"), "");
		userEvent.type(view.getByLabelText("Correo electrónico"), "");
		userEvent.type(view.getByLabelText("Contraseña"), "");
		userEvent.type(view.getByLabelText("Confirmar contraseña"), "");

		userEvent.click(view.getByRole("button"));

		expect(
			within(view.getByRole("form")).getAllByText("Este campo es obligatorio")
		).toHaveLength(5);
	});

	it("El form muestra un mensaje de error cuando el nombre es menor a 3 caracteres", () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<SignUpForm />{" "}
			</MemoryRouter>
		);

		userEvent.type(view.getByLabelText("Nombre"), "aa");
		userEvent.click(view.getByRole("button"));
		expect(
			within(view.getByRole("form")).queryByText(
				"El nombre es muy corto, debe tener al menos 3 caracteres"
			)
		).toBeInTheDocument();
	});

	it("El form muestra un mensaje de error cuando el apellido es menor a 3 caracteres", () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<SignUpForm />{" "}
			</MemoryRouter>
		);

		userEvent.type(view.getByLabelText("Apellido"), "aa");
		userEvent.click(view.getByRole("button"));
		expect(
			within(view.getByRole("form")).queryByText(
				"El apellido es muy corto, debe tener al menos 3 caracteres"
			)
		).toBeInTheDocument();
	});

	it("El form muestra un mensaje de error cuando el correo electrónico ingresao no es válido", () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<SignUpForm />{" "}
			</MemoryRouter>
		);

		userEvent.type(view.getByLabelText("Correo electrónico"), "aabbcc@gmail.c");
		userEvent.click(view.getByRole("button"));
		expect(
			within(view.getByRole("form")).queryByText(
				"El email ingresado no es válido"
			)
		).toBeInTheDocument();
	});

	it("El form muestra un mensaje de error cuando las contraseñas no coinciden", () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<SignUpForm />{" "}
			</MemoryRouter>
		);

		userEvent.type(view.getByLabelText("Contraseña"), "Aaaaaaaa1!");
		userEvent.type(view.getByLabelText("Confirmar contraseña"), "Bbbbbbbb2#");
		userEvent.click(view.getByRole("button"));
		expect(
			within(view.getByRole("form")).queryByText(
				"Las contraseñas ingresadas no coinciden"
			)
		).toBeInTheDocument();
	});

	it("Dentro del form existe un ícono para togglear el cifrado de la contraseña", () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<SignUpForm />{" "}
			</MemoryRouter>
		);
		const inputPassword = view.getByLabelText("Contraseña");
		const inputPasswordRepeat = view.getByLabelText("Confirmar contraseña");

		expect(
			within(view.getByRole("form")).queryByTestId("icono-ojo-tachado")
		).not.toBeNull();
		expect(
			within(view.getByRole("form")).queryByTestId("icono-ojo-normal")
		).toBeNull();
		expect(inputPassword).toHaveAttribute("type", "password");
		expect(inputPasswordRepeat).toHaveAttribute("type", "password");

		userEvent.click(
			within(view.getByRole("form")).getByTestId("icono-ojo-tachado")
		);

		expect(
			within(view.getByRole("form")).queryByTestId("icono-ojo-tachado")
		).toBeNull();
		expect(
			within(view.getByRole("form")).queryByTestId("icono-ojo-normal")
		).not.toBeNull();
		expect(inputPassword).toHaveAttribute("type", "text");
		expect(inputPasswordRepeat).toHaveAttribute("type", "text");

		userEvent.click(
			within(view.getByRole("form")).getByTestId("icono-ojo-normal")
		);

		expect(
			within(view.getByRole("form")).queryByTestId("icono-ojo-tachado")
		).not.toBeNull();
		expect(
			within(view.getByRole("form")).queryByTestId("icono-ojo-normal")
		).toBeNull();
		expect(inputPassword).toHaveAttribute("type", "password");
		expect(inputPasswordRepeat).toHaveAttribute("type", "password");
	});

	it("El mensaje de error del Nombre desaparece al escribir en su correspondiente input", () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<SignUpForm />{" "}
			</MemoryRouter>
		);

		const inputNombre = view.getByLabelText("Nombre");
		userEvent.type(inputNombre, "");

		userEvent.click(view.getByRole("button"));

		expect(
			within(inputNombre.parentElement).queryByText("Este campo es obligatorio")
		).not.toBeNull();
		userEvent.type(inputNombre, "a");
		expect(
			within(inputNombre.parentElement).queryByText("Este campo es obligatorio")
		).toBeNull();
	});

	it("El mensaje de error del Apellido desaparece al escribir en su correspondiente input", () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<SignUpForm />{" "}
			</MemoryRouter>
		);

		const inputApellido = view.getByLabelText("Apellido");
		userEvent.type(inputApellido, "");

		userEvent.click(view.getByRole("button"));

		expect(
			within(inputApellido.parentElement).queryByText(
				"Este campo es obligatorio"
			)
		).not.toBeNull();
		userEvent.type(inputApellido, "a");
		expect(
			within(inputApellido.parentElement).queryByText(
				"Este campo es obligatorio"
			)
		).toBeNull();
	});

	it("El mensaje de error del Correo electrónico desaparece al escribir en su correspondiente input", () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<SignUpForm />{" "}
			</MemoryRouter>
		);

		const inputEmail = view.getByLabelText("Correo electrónico");
		userEvent.type(inputEmail, "");

		userEvent.click(view.getByRole("button"));

		expect(
			within(inputEmail.parentElement).queryByText("Este campo es obligatorio")
		).not.toBeNull();
		userEvent.type(inputEmail, "a");
		expect(
			within(inputEmail.parentElement).queryByText("Este campo es obligatorio")
		).toBeNull();
	});

	it("El mensaje de error de la Contraseña desaparece al escribir en su correspondiente input", () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<SignUpForm />{" "}
			</MemoryRouter>
		);

		const inputPassword = view.getByLabelText("Contraseña");
		userEvent.type(inputPassword, "");

		userEvent.click(view.getByRole("button"));

		expect(
			within(inputPassword.parentElement).queryByText(
				"Este campo es obligatorio"
			)
		).not.toBeNull();
		userEvent.type(inputPassword, "a");
		expect(
			within(inputPassword.parentElement).queryByText(
				"Este campo es obligatorio"
			)
		).toBeNull();
	});

	it('El mensaje de error de "Confirmar contraseña" desaparece al escribir en su correspondiente input', () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<SignUpForm />{" "}
			</MemoryRouter>
		);

		const inputPasswordRepeat = view.getByLabelText("Contraseña");
		userEvent.type(inputPasswordRepeat, "");

		userEvent.click(view.getByRole("button"));

		expect(
			within(inputPasswordRepeat.parentElement).queryByText(
				"Este campo es obligatorio"
			)
		).not.toBeNull();
		userEvent.type(inputPasswordRepeat, "a");
		expect(
			within(inputPasswordRepeat.parentElement).queryByText(
				"Este campo es obligatorio"
			)
		).toBeNull();
	});

	it('Existe un link a la página de inicio de sesión con el texto "Iniciar sesión"', () => {
		const view = render(
			<MemoryRouter>
				{" "}
				<SignUpForm />{" "}
			</MemoryRouter>
		);

		expect(view.queryByText("Iniciar sesión")).toBeInTheDocument();
		expect(view.getByText("Iniciar sesión").tagName).toEqual("A");
		expect(view.getByText("Iniciar sesión")).toHaveAttribute("href", "/login");
	});

	//  ======expect(jest.fn()).toHaveBeenCalledTimes(expected)

	//     Expected number of calls: 1
	//     Received number of calls: 0
	//     it('Se dispara una alerta cuando se produce un registro exitoso', () => {
	//         const view = render(<MemoryRouter> <SignUpForm /> </MemoryRouter>);

	//         userEvent.type(view.getByLabelText("Nombre"), 'MiNombre');
	//         userEvent.type(view.getByLabelText("Apellido"), 'MiApellido');
	//         userEvent.type(view.getByLabelText("Correo electrónico"), 'MiEmail@gmail.com');
	//         userEvent.type(view.getByLabelText("Contraseña"), 'MiPassword4%');
	//         userEvent.type(view.getByLabelText("Confirmar contraseña"), 'MiPassword4%');

	//         const alertMock = jest.spyOn(window,'alert').mockImplementation();
	//         userEvent.click(view.getByRole('button'));
	//         expect(alertMock).toHaveBeenCalledTimes(1);
	//     });

	//const view = render(<MemoryRouter> <SignUpForm /> </MemoryRouter>).getAllByRole("dhgdfhdf");;
});
