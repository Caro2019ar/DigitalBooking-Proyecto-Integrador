import { useState } from "react";

//icons
// import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
// import { faStar } from "@fortawesome/free-solid-svg-icons";

//css
// import globalStyles from "../../styles/global.module.css";
import formStyles from "../../styles/forms/form.module.css";
import formBooking from "../../styles/bookingPage/formBooking.module.css";
import hsStyles from "../../styles/bookingPage/hours.module.css";

const EntryTime = (props) => {
  const [entryTime, setEntryTime] = useState("");

  const handleChange = (event) => {
    setEntryTime(event.target.value);
    props.onSelect(event.target.value);
  };

  function horaOptions()
  {
    const zeroPad = (num, cant) => String(num).padStart(cant, "0");

    let options = [];
    for (let i = 0; i < 24; i++) {
      const attributeValue = `${zeroPad(i, 2)}:00`;
      const displayValue = `${
        i >= 1 && i <= 12 ? zeroPad(i, 2) : zeroPad(Math.abs(i - 12), 2)
      }:00`;
      const amOrPm = i / 12 < 1 ? "AM" : "PM";
      options.push(
        <option key={attributeValue} value={attributeValue}>
          {displayValue} {amOrPm}
        </option>
      );
    }

    return options;
  }

  return (
    <>
      <div
        className={`${formStyles.form} ${formBooking.container} ${props.className}`}
      >
        <h2>Tu horario de llegada</h2>
        <div className={`${formBooking.form} ${hsStyles.form}`}>
          <i className="far fa-check-circle"></i>
          <h5>
            Tu habitación va a estar lista para el check-in entre las 10:00 AM y
            las 11:00 PM
          </h5>

          <label className={formBooking.label}>
            <p>Indicá tu horario estimado de llegada</p>
          </label>
          <div className={hsStyles.boxFormHs}>
            <select
              className={`${formStyles.formControl} ${hsStyles.formControl}`}
              type="datetime"
              name="Entrytime"
              id="Entrytime"
              list="hours"
              onChange={handleChange}
              value={entryTime}
              required
            >
              <option value="" disabled>
                Seleccionar hora
              </option>
              {horaOptions()}
            </select>
            <i></i>
          </div>
        </div>
      </div>
    </>
  );
};

export default EntryTime;
