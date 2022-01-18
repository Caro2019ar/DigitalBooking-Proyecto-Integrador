import React from "react";
import { shallow } from "enzyme";
import ImageGallery from "../../component/detailsPage/ImageGallery";

const wrapper = shallow(<ImageGallery />);

describe("<ImageGallery />", () => {
	it("Renderiza div con clase galleryContainer", () => {
		expect(wrapper.find("div").first().hasClass("galleryContainer")).toBe(true);
	});
	it("Renderiza una div con clase galleryLibraryContainer", () => {
		expect(wrapper.find("div").at(2).hasClass("galleryLibraryContainer")).toBe(
			true
		);
	});
});
