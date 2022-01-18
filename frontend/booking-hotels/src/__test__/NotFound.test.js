import React from 'react';
import { shallow, render } from 'enzyme';
import NotFound from "../component/NotFound";

const wrapper = shallow(<NotFound />);
//console.log(wrapper.html());
//console.log(wrapper.text());

describe('<NotFound />', () => {

    it('Renderiza un solo h1', () => {
        expect(wrapper.find("h1")).toHaveLength(1);
    });

    it('Renderiza un solo h3', () => {
        expect(wrapper.find("h3")).toHaveLength(1);
    });

    it('El elemento "h1" tiene la clase "title"', () => {
        expect(wrapper.find("h1").first().hasClass("title")).toBeTruthy;
    });

    it('El elemento "h3" tiene la clase "msg"', () => {
        expect(wrapper.find("h3").first().hasClass("msg")).toBeTruthy;
    });

    it('El elemento "h1" contiene exactamente el texto "404 Not Found"', () => {
        expect(wrapper.find("h1").first().text()).toEqual('404 Not Found');
    });

    it('El elemento "h3" contiene el texto "no existe"', () => {
        expect(wrapper.find("h3").first().text()).toContain('no existe');
    });

    //console.log(wrapper.find("h3").first().text());
});