import React from "react";
import "@testing-library/jest-dom/extend-expect";
import Button from "../component/Button";
import { shallow, mount } from "enzyme";
import { MemoryRouter } from 'react-router';

describe("<Button/>", () => {

	it('Las props se asignan correctamente', () => {
		const initialProps = { className: "class", id: "id", to: "/home" };
		const container = shallow(<Button {...initialProps} />);
		expect(container.name()).toEqual("Link");
		expect(container.props()).toHaveProperty('className', 'class');
		expect(container.props()).toHaveProperty('id', 'id');
		expect(container.props()).toHaveProperty('to', '/home');
	});

	it('Existe un link con path igual al de la prop "to"', () => {
		const initialProps = { className: "class", id: "id", to: "/home" };
		const container = mount(<MemoryRouter> <Button {...initialProps} /> </MemoryRouter>);
		expect(container.find('a').exists()).toBeTruthy();
		expect(container.find('a').first().getDOMNode()).toHaveAttribute('href', '/home');
	});

});
