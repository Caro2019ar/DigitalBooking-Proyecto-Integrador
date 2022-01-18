import { Link, useHistory } from "react-router-dom";
import { useState, useEffect, useContext } from "react";
import { ReservaService } from "../../Service/ReservaService";

// Librería Fontawesome, uso de íconos:
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMapMarkerAlt } from "@fortawesome/free-solid-svg-icons";
import { faStar } from "@fortawesome/free-solid-svg-icons";

// CSS
import styles from "../../styles/componentGlobal/cardproduct.module.css";
import globalStyles from "../../styles/global.module.css";
import productBooking from "../../styles/bookingPage/productBooking.module.css";
import formStyles from "../../styles//forms/form.module.css";

const Product = ({
  producto,
  usuario,
  startDate,
  endDate,
  city,
  infoCovid,
  vacunadoCovid,
  entryTime,
  className,
}) => {
  const [errorMSG, setErrorMSG] = useState("");
  const [resumeBooking, setResumeBooking] = useState(false);

  let history = useHistory();

  const validateForm = (city, startDate, endDate) => {
    if (!city) {
      setErrorMSG("Por favor, informe la ciudad");
      return false;
    }
    if (!startDate || !endDate) {
      setErrorMSG("Por favor, seleccione fechas de reserva");
      return false;
    }
    if (!entryTime) {
      setErrorMSG("Por favor, seleccione horario estimado de llegada");
      return false;
    }
    return true;
  };

  function handleConfirmacionReserva() {
    const dataReserva = {
      horaInicio: entryTime,
      fechaInicial: startDate.toISOString(),
      fechaFinal: endDate.toISOString(),
      producto: {
        id: producto.id,
      },
      cliente: {
        id: usuario.id,
      },
      ciudadOrigen: city,
      infoCovid: infoCovid,
      vacunadoCovid: vacunadoCovid,
    };

    const reservaService = new ReservaService();
    reservaService
      .guardarReserva(dataReserva)
      .then((data) => {
        history.push("/booking-success");
      })
      .catch((e) => {
        if (e.response) console.log(e.response.data.error);
        else
          console.log(
            "Lamentablemente no se ha podido efectuar la reserva. Por favor intente más tarde."
          );
      });
  }

  function handleResumeBooking() {
    if (usuario && validateForm(city, startDate, endDate)) {
      resumeBooking ? setResumeBooking(false) : setResumeBooking(true);
    }
  }

  // Para transorfmar, por ejemplo, '19:00' en '07:00 PM'
  function toTimeDisplayFormat(time)
  {
    let date = new Date();
    date.setHours(time.split(':')[0]);
    date.setMinutes(time.split(':')[1]);
    const config = { hour: 'numeric', minute: 'numeric', hour12: true };
    return date.toLocaleTimeString('en-US', config).padStart(8, '0');
  }

  
  return (
    <>
      <div
        id={producto.title}
        key={producto.title}
        className={`${productBooking.productContainer}`}
        role="article"
      >
        <h2>Detalle de la reserva</h2>
        <div className={productBooking.cardImage}>
          <Link to={`product/${producto.id}`}>
            <img
              src={producto.categoria.urlImagen}
              alt={producto.categoria.titulo}
            />
          </Link>
        </div>
        <div className={`${styles.cardText} ${productBooking.cardText}`}>
          <div className={styles.reviewContainer}>
            <div className={styles.scoreTitleContainer}>
              <div className={styles.scoreContainer}>
                <h4>{producto.categoria.titulo}</h4>
              </div>
              <h3>{producto.nombre}</h3>
            </div>
          </div>
          <div className={styles.scoreContainer}>
            <ul className={styles.starContainer}>
              <li>
                <FontAwesomeIcon icon={faStar} className={styles.starIcon} />
              </li>
              <li>
                <FontAwesomeIcon icon={faStar} className={styles.starIcon} />
              </li>
              <li>
                <FontAwesomeIcon icon={faStar} className={styles.starIcon} />
              </li>
              <li>
                <FontAwesomeIcon icon={faStar} className={styles.starIcon} />
              </li>
              <li>
                <FontAwesomeIcon icon={faStar} className={styles.starIcon} />
              </li>
            </ul>
          </div>
          <div
            className={` ${styles.locationContainer} ${productBooking.locationContainer}`}
          >
            <FontAwesomeIcon
              icon={faMapMarkerAlt}
              className={styles.headerIcon}
            />
            <p>{producto.ciudad?.nombre}</p>
          </div>

          <div className={productBooking.checked}>
            <div className={productBooking.check}>
              <p>Check in</p>
              <div>{startDate?.toLocaleDateString('en-GB')}</div>
            </div>

            <div className={productBooking.check}>
              <p>Check out</p>
              <div>{endDate?.toLocaleDateString('en-GB')}</div>
            </div>
          </div>
          <div
            className={`${styles.cardProductButtonContainer} ${productBooking.cardProductButtonContainer}`}
          >
            <button
              className={`
                ${globalStyles.button}
                ${usuario.rol === 'ROLE_ADMIN' ? globalStyles.disabledButton : ''}
              `}
              onClick={handleResumeBooking}
              disabled={usuario.rol === 'ROLE_ADMIN' ? true : false}
            >
              Confirmar reserva
            </button>
          </div>
          <div className={`${formStyles.errorBookingMsg}`}>{errorMSG}</div>
        </div>
      </div>
      <div
        className={`
        ${productBooking.overlay} 
        ${resumeBooking ? productBooking.active : ""}
        `}
      >
        <div
          className={`${productBooking.popUp} ${
            resumeBooking ? productBooking.active : ""
          }
        `}
        >
          <h3 className={productBooking.h3}>
            Por favor verifica los datos de tu reserva
          </h3>
          <div className={productBooking.boxInfo}>
            <div className={productBooking.titleInfo}>
              Alojamiento:
              <span className={productBooking.info}>
                {producto.nombre}
              </span>
            </div>
            <div className={productBooking.titleInfo}>
              Ciudad:
              <span className={productBooking.info}>
                {producto.ciudad.nombre}
              </span>
            </div>
            <div className={productBooking.titleInfo}>
              Dirección:
              <span className={productBooking.info}>
                {producto.direccion}
              </span>
            </div>
            <div className={productBooking.titleInfo}>
              Check in:
              <span className={productBooking.info}>
                {startDate?.toLocaleDateString('en-GB')}
              </span>
            </div>
            <div className={productBooking.titleInfo}>
              Horario de llegada:
              <span className={productBooking.info}>{entryTime ? toTimeDisplayFormat(entryTime) : null}</span>
            </div>
            <div className={productBooking.titleInfo}>
              Check out:
              <span className={productBooking.info}>
                {endDate?.toLocaleDateString('en-GB')}
              </span>
            </div>
          </div>
          <div className={productBooking.boxButton}>
            <div
              className={`${productBooking.modifyInfo} ${globalStyles.button}`}
              onClick={handleResumeBooking}
            >
              Modificar datos
            </div>
            <div
              className={`${productBooking.infoOk} ${globalStyles.button}`}
              onClick={handleConfirmacionReserva}
            >
              Confirmar datos
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default Product;
