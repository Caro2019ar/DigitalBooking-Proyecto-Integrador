import React from 'react';
import { render, screen, within, logRoles } from '@testing-library/react'
import "@testing-library/jest-dom";
import "@testing-library/user-event";
import { MemoryRouter } from 'react-router';

import SignInForm from "../component/SignInForm";
import userEvent from '@testing-library/user-event';

describe('<SignInForm />', () => {

    it('Existe un título con el texto "Iniciar Sesión"', () => {
        const view = render(<MemoryRouter> <SignInForm /> </MemoryRouter>);
        expect(view.queryByRole("heading")).toBeInTheDocument();
        expect(view.getByRole("heading")).toHaveTextContent("Iniciar Sesión");
    });

    it('Existe un form', () => {
        const view = render(<MemoryRouter> <SignInForm /> </MemoryRouter>);
        expect(view.queryByRole("form")).toBeInTheDocument();
    });

    it('Dentro del form existe un input de tipo "email" que tiene un label asociado con el texto "Correo Electrónico"', () => {
        const view = render(<MemoryRouter> <SignInForm /> </MemoryRouter>);
        // El ByLabelText devuelve un INPUT
        expect(within(view.getByRole('form')).queryByLabelText("Correo Electrónico")).toBeInTheDocument();
        expect(within(view.getByRole('form')).getByLabelText("Correo Electrónico")).toHaveAttribute('type', 'email');
        //expect(within(view.getByRole('form')).getByRole('textbox', { name: "Correo Electrónico" })).toHaveAttribute('type', 'email');
    });

    it('Dentro del form existe un input de tipo "password" que tiene un label asociado con el texto "Contraseña"', () => {
        const view = render(<MemoryRouter> <SignInForm /> </MemoryRouter>);
        // El ByLabelText devuelve un INPUT
        expect(within(view.getByRole('form')).queryByLabelText("Contraseña")).toBeInTheDocument();
        expect(within(view.getByRole('form')).getByLabelText("Contraseña")).toHaveAttribute('type', 'password');
        //expect(within(view.getByRole('form')).getByRole('textbox', { name: "Correo Electrónico" })).toHaveAttribute('type', 'email');
    });

    it('Dentro del form existe un botón con el texto "Ingresar" y está habilitado', () => {
        const view = render(<MemoryRouter> <SignInForm /> </MemoryRouter>);
        expect(within(view.getByRole('form')).queryByRole("button")).toBeInTheDocument();
        expect(within(view.getByRole('form')).getByRole("button")).toHaveTextContent("Ingresar");
        expect(within(view.getByRole('form')).getByRole("button")).toBeEnabled();
    });

    it('El form renderiza los datos que se ingresan en los input', () => {
        const view = render(<MemoryRouter> <SignInForm /> </MemoryRouter>);
    
        const inputEmail = view.getByLabelText("Correo Electrónico");
        userEvent.type(inputEmail, 'email-ingresado');
        const inputPassword = view.getByLabelText("Contraseña");
        userEvent.type(inputPassword, 'contraseña-ingresada');

        //userEvent.click(view.getByRole('button'));

        expect(view.getByRole('form')).toHaveFormValues({
            email: 'email-ingresado',
            password: 'contraseña-ingresada',
        });
    });

    it('El form muestra mensajes de error si se intenta ingresar con los input vacíos', () => {
        const view = render(<MemoryRouter> <SignInForm /> </MemoryRouter>);

        const inputEmail = view.getByLabelText("Correo Electrónico");
        userEvent.type(inputEmail, '');
        const inputPassword = view.getByLabelText("Contraseña");
        userEvent.type(inputPassword, '');

        userEvent.click(view.getByRole('button'));

        expect(within(view.getByRole('form')).getAllByText("Este campo es obligatorio")).toHaveLength(2);
    });

    it('Dentro del form existe un ícono para togglear el cifrado de la contraseña', () => {
        const view = render(<MemoryRouter> <SignInForm /> </MemoryRouter>);
        const inputPassword = view.getByLabelText("Contraseña");

        expect(within(view.getByRole('form')).queryByTestId("icono-ojo-tachado")).not.toBeNull();
        expect(within(view.getByRole('form')).queryByTestId("icono-ojo-normal")).toBeNull();
        expect(inputPassword).toHaveAttribute('type', 'password');

        userEvent.click(within(view.getByRole('form')).getByTestId("icono-ojo-tachado"));

        expect(within(view.getByRole('form')).queryByTestId("icono-ojo-tachado")).toBeNull();
        expect(within(view.getByRole('form')).queryByTestId("icono-ojo-normal")).not.toBeNull();
        expect(inputPassword).toHaveAttribute('type', 'text');

        userEvent.click(within(view.getByRole('form')).getByTestId("icono-ojo-normal"));

        expect(within(view.getByRole('form')).queryByTestId("icono-ojo-tachado")).not.toBeNull();
        expect(within(view.getByRole('form')).queryByTestId("icono-ojo-normal")).toBeNull();
        expect(inputPassword).toHaveAttribute('type', 'password');
    });

    it('El mensaje de error del Correo Electrónico desaparece al escribir en su correspondiente input', () => {
        const view = render(<MemoryRouter> <SignInForm /> </MemoryRouter>);

        const inputEmail = view.getByLabelText("Correo Electrónico");
        userEvent.type(inputEmail, '');

        userEvent.click(view.getByRole('button'));

        expect(within(inputEmail.parentElement).queryByText("Este campo es obligatorio")).not.toBeNull();
        userEvent.type(inputEmail, 'a');
        expect(within(inputEmail.parentElement).queryByText("Este campo es obligatorio")).toBeNull();
    });

    it('El mensaje de error de la Contraseña desaparece al escribir en su correspondiente input', () => {
        const view = render(<MemoryRouter> <SignInForm /> </MemoryRouter>);

        const inputPassword = view.getByLabelText("Contraseña");
        userEvent.type(inputPassword, '');

        userEvent.click(view.getByRole('button'));

        expect(within(inputPassword.parentElement).queryByText("Este campo es obligatorio")).not.toBeNull();
        userEvent.type(inputPassword, 'a');
        expect(within(inputPassword.parentElement).queryByText("Este campo es obligatorio")).toBeNull();
    });

    it('Se muestra un mensaje de error al intentar ingresar con credenciales inválidas', () => {
        const view = render(<MemoryRouter> <SignInForm /> </MemoryRouter>);

        const inputEmail = view.getByLabelText("Correo Electrónico");
        userEvent.type(inputEmail, 'credenciales@invalidas.com');
        const inputPassword = view.getByLabelText("Contraseña");
        userEvent.type(inputPassword, 'password');

        userEvent.click(view.getByRole('button'));

        expect(view.queryByText("Por favor, vuelva a intentarlo, sus credenciales son inválidas")).not.toBeNull();
    });

    it('Existe un link a la página de registro con el texto "Regístrate"', () => {
        const view = render(<MemoryRouter> <SignInForm /> </MemoryRouter>);

        expect(view.queryByText("Regístrate")).toBeInTheDocument();
        expect(view.getByText("Regístrate").tagName).toEqual('A');
        expect(view.getByText("Regístrate")).toHaveAttribute('href', '/register');
    });

    //Para ver los roles en consola
    //const view = render(<MemoryRouter> <SignInForm /> </MemoryRouter>).getAllByRole("dhgdfhdf");;
});