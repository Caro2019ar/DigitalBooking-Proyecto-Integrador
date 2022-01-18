
import { Link } from "react-router-dom";
import axios from 'axios';
import { useState, useEffect } from "react";
import { useParams, useLocation } from "react-router";
// import DetailsPage from "./detailsPage/DetailsPage";

import tarjetas from "../tarjetas.json";

// Librería Fontawesome, uso de íconos:
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMapMarkerAlt } from "@fortawesome/free-solid-svg-icons";
import { faStar } from "@fortawesome/free-solid-svg-icons";

import {ProductsService} from "../Service/ProductsService.jsx"
// CSS
import styles from "../styles/index/cardproduct.module.css";
import globalStyles from "../styles/global.module.css";

const CardProduct = () => {
	const [iconFavoritePressed, setIconFavoritePressed] = useState(false);
	const[listaP, setLista]= useState([])

	const {search} = useLocation()
	const searchParams = new URLSearchParams(search)
	const category = searchParams.get("categoria")
	const city = searchParams.get("ciudad")
	useEffect(()=> {
	  const productService = new ProductsService()
	  if (category) {
		productService.getProductByCategory(category).then(data=>setLista(data))
	  }
	  else if(city){
		productService.getProductByCity(city).then(data=>setLista(data))
	  }
	  else{
	  productService.getAllProducts().then(data=> setLista(data))}
	},[category, city]);
	let listOfUrl = []

	const handleClick = () => {
		console.log("Presionado");
		setIconFavoritePressed(true);
	};
	return (
		<div className={styles.productsCardContainer}>
			<h2 className={styles.h2}>Recomendaciones</h2>
			<div className={styles.cardContainer}>
				{listaP.map((item) => {
					return (
						listOfUrl = [...new Set(item.imagenes?.map(it => it.url))],
						<div
							id={item.title}
							key={item.title}
							className={styles.card}
							role="article"
						>
							<div className={styles.cardImage}>
								<Link to={`product/${item.id}`}>
									<img src={listOfUrl[0]} alt={item.title} />
								</Link>
							</div>
							<div className={styles.cardText}>
								<div className={styles.reviewContainer}>
									<div className={styles.scoreTitleContainer}>
										<div className={styles.scoreContainer}>
											<h4>{item.categoria.titulo}</h4>
											<ul className={styles.starContainer}>
												<li>
													<FontAwesomeIcon
														icon={faStar}
														className={styles.starIcon}
													/>
												</li>
												<li>
													<FontAwesomeIcon
														icon={faStar}
														className={styles.starIcon}
													/>
												</li>
												<li>
													<FontAwesomeIcon
														icon={faStar}
														className={styles.starIcon}
													/>
												</li>
												<li>
													<FontAwesomeIcon
														icon={faStar}
														className={styles.starIcon}
													/>
												</li>
												<li>
													<FontAwesomeIcon
														icon={faStar}
														className={styles.starIcon}
													/>
												</li>
												<li></li>
											</ul>
										</div>
										<h3>{item.nombre}</h3>
									</div>
									<div className={styles.scoreResultsContainer}>
										<span>
											<p>8</p>
										</span>
										<h3>Muy bueno</h3>
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
											<a href="">
												<p>MOSTRAR EN EL MAPA</p>
											</a>
										</span>
									</div>
									<div className={styles.featuresIcon}>
										<ul className={styles.listContainer}>
											<li>
												<i className="fas fa-swimmer"></i>
											</li>
											<li>
												<i className="fas fa-wifi"></i>
											</li>
										</ul>
									</div>
								</div>

								<div className={styles.descriptionContainer}>
									<p>{item.descripcion}</p>
								</div>
								<div className={styles.cardProductButtonContainer}>
									{/* mostrar categoria y nombre producto (cambiar por ID) */}
									<Link to={`product/${item.id}`}>
										<button className={globalStyles.button}>Ver detalle</button>
									</Link>
								</div>
							</div>
						</div>
					);
				})}
			</div>
		</div>
	);
};

export default CardProduct;
