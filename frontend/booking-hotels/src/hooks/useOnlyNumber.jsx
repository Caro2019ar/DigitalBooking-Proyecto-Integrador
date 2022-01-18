import { useState } from "react";

//Hook que escucha el evento onChange de los input y setea el valor de cada input donde es utilizado
export default function useInput(initialValue, setErrors) {
  const [value, setValue] = useState(initialValue);

  function handleChange(e) {
    let out = "";
    const filtro = "-1234567890"; //Caracteres validos

    //Recorrer el texto y verificar si el caracter se encuentra en la lista de validos
    for (let i = 0; i < e.target.value.length; i++)
      if (filtro.indexOf(e.target.value.charAt(i)) !== -1)
        //Se añaden a la salida los caracteres validos
        out += e.target.value.charAt(i);

    setValue(out);
    setErrors({});
  }
  return { value, onChange: handleChange };
}
