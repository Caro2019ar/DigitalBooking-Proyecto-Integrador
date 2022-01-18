import React from "react";
import { shallow, mount } from "enzyme";
import DetailsPage from "../../component/detailsPage/DetailsPage";
import HeaderDetailsPage from "../../component/detailsPage/HeaderDetailsPage";
import Location from "../../component/detailsPage/Location";
import ImageGallery from "../../component/detailsPage/ImageGallery";
import DesktopImageGallery from "../../component/detailsPage/DesktopImageGallery";
import Description from "../../component/detailsPage/Description";
import ProductFeatures from "../../component/detailsPage/ProductFeatures";
import AvailableDate from "../../component/detailsPage/AvailableDate";
import Maps from "../../component/detailsPage/Maps";
import ProductPolicy from "../../component/detailsPage/ProductPolicy";

const wrapper = shallow(<DetailsPage />);

describe("<DetailsPage />", () => {
	it("renderiza el componente 'HeaderDetailsPage'", () => {
		expect(wrapper.contains(<HeaderDetailsPage />)).toBe(true);
	});
	it("renderiza el componente 'Location'", () => {
		expect(wrapper.contains(<Location />)).toBe(true);
	});
	it("renderiza el componente 'ImageGallery'", () => {
		expect(wrapper.contains(<ImageGallery />)).toBe(true);
	});
	it("renderiza un 'div' con la clase detailsPageContainer", () => {
		expect(wrapper.find("div").first().hasClass("detailsPageContainer")).toBe(
			true
		);
	});
	it("renderiza una 'div' con el componente DesktopImageGallery", () => {
		expect(
			wrapper
				.find("div")
				.first()
				.contains(<DesktopImageGallery />)
		).toBe(true);
	});
	it("renderiza una 'div' con el componente Description", () => {
		expect(wrapper.contains(<Description />)).toBe(true);
	});
	it("renderiza una 'div' con el componente ProductFeatures", () => {
		expect(wrapper.contains(<ProductFeatures />)).toBe(true);
	});
	it("renderiza una 'div' con el componente AvailableDate", () => {
		expect(wrapper.contains(<AvailableDate />)).toBe(true);
	});
	it("renderiza una 'div' con el componente Maps", () => {
		expect(wrapper.contains(<Maps />)).toBe(true);
	});
	it("renderiza una 'div' con el componente ProductPolicy", () => {
		expect(wrapper.contains(<ProductPolicy />)).toBe(true);
	});
});
