import { Link } from "react-router-dom";
import { HashLink } from "react-router-hash-link";
import { useState, useEffect } from "react";
// import DetailsPage from "./detailsPage/DetailsPage";

//component
import { ClienteService } from "../../Service/ClienteService";
import { ProductoService } from "../../Service/ProductoService.jsx";

// Librería Fontawesome, uso de íconos:
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMapMarkerAlt } from "@fortawesome/free-solid-svg-icons";

// CSS
import globalStyles from "../../styles/global.module.css";
import styles from "../../styles/componentGlobal/cardproduct.module.css";
import "../../styles/index/cardproducttransitions.css";

const CardProduct = ({
	item,
	favorite,
	usuario,
	onEliminacionFavorito,
	onDeleteLastFavorite,
	cantidadResultados,
}) => {
	const [listaFavoritosId, setListaFavoritosId] = useState([]);
	// Para manejo de la animación del like
	const [productIdJustLiked, setProductIdJustLiked] = useState(null);

	useEffect(() => {
		const productService = new ProductoService();

		if (usuario && usuario.rol === "ROLE_CUSTOMER") {
			productService
				.getFavoritesIdByUser(usuario.id)
				.then((data) => setListaFavoritosId(data))
				.catch((e) => {
					if (e.response) console.log(e.response.data.error);
					else
						console.log("Ha ocurrido un error. Por favor intente más tarde.");
				});
		}
	}, [usuario]);

	function handleFavoriteIconClick(productoId) {
		const esFavorito = listaFavoritosId.includes(productoId);

		const clienteService = new ClienteService();
		if (esFavorito) {
			setProductIdJustLiked(null);
			clienteService
				.eliminarFavorito(usuario.id, productoId)
				.then((data) => {
					// Para que se actualice la eliminación en /index
					setListaFavoritosId(
						listaFavoritosId.filter((id) => id !== productoId)
					);
					if (favorite) {
						// Para que se renderice el modal cuando se elimina el último favorito
						if (cantidadResultados === 1) onDeleteLastFavorite();
						// Para que se actualice la eliminación en /favoritos
						onEliminacionFavorito(productoId);
					}
				})
				.catch((e) => {
					if (e.response) console.log(e.response.data.error);
					else
						console.log("Ha ocurrido un error. Por favor intente más tarde.");
				});
		} else {
			setProductIdJustLiked(productoId);
			clienteService
				.agregarFavorito(usuario.id, productoId)
				.then((data) =>
					setListaFavoritosId((oldList) => [...oldList, productoId])
				) // Para que se actualice la adición en /index
				.catch((e) => {
					if (e.response) console.log(e.response.data.error);
					else
						console.log("Ha ocurrido un error. Por favor intente más tarde.");
				});
		}
	}

	function getRatingText(item) {
		if (item.valoracion.cantidadVotos === 0) return "";

		const promedioActual = (
			item.valoracion.puntajeTotal / item.valoracion.cantidadVotos
		).toFixed(1);

		if (promedioActual >= 4.5) return "Divino";
		else if (promedioActual >= 4.0) return "Excelente";
		else if (promedioActual >= 3.5) return "Muy bueno";
		else if (promedioActual >= 3.0) return "Bueno";
		else if (promedioActual >= 2.5) return "Aceptable";
		else if (promedioActual >= 2.0) return "Pobre";
		else if (promedioActual >= 1.5) return "Horrible";
		else return "Desastroso";
	}

  function trimDescription(description)
  {
    const maxLength = 200;
    let trimmedDescription = description.substr(0, maxLength);
    if (description.length > 200)
    {
      // Controlar si cortamos alguna palabra por el medio
      trimmedDescription = trimmedDescription.substr(0, trimmedDescription.lastIndexOf(" "));
      trimmedDescription = trimmedDescription+'...';
    }

    return trimmedDescription;
  }


  return (
    <div
      id={item.title}
      key={item.title}
      className={styles.card}
      role="article"
    >
      <div className={styles.cardImage}>
        <Link to={`product/${item.id}`}>
          <img src={(item.imagenes.length > 0) ? item.imagenes[0].url : ''} alt={item.title} />
        </Link>
        {usuario && usuario.rol === "ROLE_CUSTOMER" && favorite ? (
          <span
            className={
              listaFavoritosId.includes(item.id)
                ? styles.favoriteIconYes
                : styles.favoriteIconNo
            }
            onClick={() => handleFavoriteIconClick(item.id)}
            data-testid="spanFavoriteIcon"
          >
            <i
              className={
                listaFavoritosId.includes(item.id)
                  ? "fas fa-heart"
                  : "far fa-heart"
              }
            ></i>
          </span>
        ) : null}
        {usuario && usuario.rol === "ROLE_CUSTOMER" && !favorite ? (
          <span
            className={`
                    ${
											listaFavoritosId.includes(item.id)
												? styles.favoriteIconYes
												: styles.favoriteIconNo
										}
                    ${
											item.id === productIdJustLiked
												? styles.favoriteIconYesOnClick
												: ""
										}
                  `}
						onClick={() => handleFavoriteIconClick(item.id)}
					>
						<i
							className={
								listaFavoritosId.includes(item.id)
									? "fas fa-heart"
									: "far fa-heart"
							}
						></i>
					</span>
				) : null}
			</div>
			<div className={styles.cardText}>
				<div className={styles.reviewContainer}>
					<div className={styles.scoreTitleContainer}>
						<div className={styles.scoreContainer}>
							<h4>{item.categoria.titulo}</h4>
						</div>
						<h3>{item.nombre}</h3>
					</div>
					<div className={styles.scoreResultsContainer}>
						<span>
							<p>
								{item.valoracion.cantidadVotos === 0
									? "-"
									: (
											item.valoracion.puntajeTotal /
											item.valoracion.cantidadVotos
									  ).toFixed(1)}
							</p>
						</span>
						<h3>{getRatingText(item)}</h3>
					</div>
				</div>
				<div>
					<div className={styles.locationContainer}>
						<FontAwesomeIcon
							icon={faMapMarkerAlt}
							className={styles.headerIcon}
						/>
						<p>{item.ciudad.nombre}</p>
						<p>-</p>
						<span>
							<HashLink to={`product/${item.id}#map`}>
								<p>MOSTRAR EN EL MAPA</p>
							</HashLink>
						</span>
					</div>
					<div className={styles.featuresIcon}>
						<ul className={styles.listContainer}>
							{item.caracteristicas.map((caracteristica) => (
								<li key={caracteristica.nombre}>
									<i className={caracteristica.icono}></i>
								</li>
							))}
						</ul>
					</div>
				</div>

        <div className={styles.descriptionContainer}>
          <p>{trimDescription(item.descripcion)}</p>
        </div>
        <div className={styles.cardProductButtonContainer}>
          <Link to={`product/${item.id}`}>
            <button className={globalStyles.button}>Ver detalle</button>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default CardProduct;
