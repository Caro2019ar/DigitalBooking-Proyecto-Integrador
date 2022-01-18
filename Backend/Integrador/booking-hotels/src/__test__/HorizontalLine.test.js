import HorizontalLine from "../component/HorizontalLine";
import { shallow } from "enzyme";

describe("<HorizontalLine/>", () => {
	it("debe renderizar la linea horizontal", () => {
		const wrapper = shallow(<HorizontalLine />);
		const hr = wrapper.find("hr");
		expect(hr).toHaveLength(1);
	});
});
