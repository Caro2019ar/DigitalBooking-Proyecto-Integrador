import { useState } from "react";

//Hook que escucha el evento onChange de los input y setea el valor de cada input donde es utilizado
export default function useInput(initialValue, setErrors) {
	const [value, setValue] = useState(initialValue);

	function handleChange(e) {
		setValue(e.target.value);
		setErrors({});
	}
	return { value, onChange: handleChange };
}
