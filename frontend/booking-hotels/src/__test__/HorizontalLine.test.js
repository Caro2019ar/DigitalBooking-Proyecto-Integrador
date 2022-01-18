import HorizontalLine from "../component/componentGLobal/HorizontalLine";
import { shallow } from "enzyme";

describe("<HorizontalLine/>", () => {
	it("debe renderizar la linea horizontal", () => {
		const wrapper = shallow(<HorizontalLine />);
		const hr = wrapper.find("hr");
		expect(hr).toHaveLength(1);
	});
});
