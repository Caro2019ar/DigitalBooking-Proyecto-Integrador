import App from "../component/App";
import "@testing-library/jest-dom/extend-expect";
import { render } from "@testing-library/react";
import { shallow, mount } from "enzyme";
import Header from "../component/header/Header";

describe("<App/>", () => {

	it("Debe renderizar sin errores", () => {
		const wrapper = shallow(<App />);
		expect(wrapper.find("div").first().hasClass("App")).toBe(true);
	});

	it("Debe renderizar el Header", () => {
		const wrapper = mount(<App />);
		const header = wrapper.find("header");
		expect(header).toHaveLength(1);
	});
	
});