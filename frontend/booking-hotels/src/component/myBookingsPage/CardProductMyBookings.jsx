import { Link } from "react-router-dom";

// Librería Fontawesome, uso de íconos:
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMapMarkerAlt } from "@fortawesome/free-solid-svg-icons";

// CSS
import globalStyles from "../../styles/global.module.css";
import styles from "../../styles/componentGlobal/cardproduct.module.css";
import myBookingsPageStyles from "../../styles/myBookingsPage/myBookingsPage.module.css";
import "../../styles/index/cardproducttransitions.css";

const CardProductMyBookings = (props) => {
  const item = props.product;
  // La adición es para que no se cambie el día al día anterior
  const ckechInDate = new Date(item.fechaInicial + "T00:00");
  const ckechOutDate = new Date(item.fechaFinal + "T00:00");
  const CheckInDateFormat = ckechInDate.toLocaleDateString('en-GB');
  const CheckOutDateFormat = ckechOutDate.toLocaleDateString('en-GB');

  return (
    <div
      className={`${styles.card} ${myBookingsPageStyles.cardProduct}`}
      role="article"
    >
      <div className={styles.cardImage}>
        <Link to={`product/${item.producto.id}`}>
          <img src={item.producto.imagenes[0].url} alt={item.producto.nombre} />
        </Link>
      </div>
      <div className={styles.cardText}>
        <div className={styles.reviewContainer}>
          <div className={styles.scoreTitleContainer}>
            <div className={styles.scoreContainer}>
              <h4>{item.producto.categoria.titulo}</h4>
            </div>
            <h3>{item.producto.nombre}</h3>
          </div>
        </div>

        <div className={myBookingsPageStyles.datesContainer}>
          <div
            className={`${styles.locationContainer} ${myBookingsPageStyles.locationContainer}`}
          >
            <FontAwesomeIcon
              icon={faMapMarkerAlt}
              className={styles.headerIcon}
            />
            <p>{item.producto.ciudad.nombre}</p>
          </div>
        </div>
        <div className={myBookingsPageStyles.datesContainer}>
          <div className={myBookingsPageStyles.dateRow}>
            <h2>CheckIn</h2>
            <h3>{CheckInDateFormat}</h3>
          </div>
          <hr className={myBookingsPageStyles.verticalLine} />
          <div className={myBookingsPageStyles.dateRow}>
            <h2>CheckOut</h2>
            <h3>{CheckOutDateFormat}</h3>
          </div>
        </div>
        <div className={styles.cardProductButtonContainer}>
          <Link to={`product/${item.producto.id}`}>
            <button className={globalStyles.button}>{props.buttonText}</button>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default CardProductMyBookings;
