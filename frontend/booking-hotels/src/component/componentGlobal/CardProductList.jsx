import { useState, useEffect } from "react";
import { useLocation } from "react-router";
// import DetailsPage from "./detailsPage/DetailsPage";

//component
import { ProductoService } from "../../Service/ProductoService.jsx";
import CardProduct from "./CardProduct";
import Paginacion from "./Paginacion";
import Loader from "../componentGlobal/Loader";

// Animaciones
import { CSSTransition, TransitionGroup } from "react-transition-group";

// CSS
import globalStyles from "../../styles/global.module.css";
import styles from "../../styles/componentGlobal/cardproduct.module.css";
import "../../styles/index/cardproducttransitions.css";

const CardProductList = ({  title, favorite, usuario, onDeleteLastFavorite }) => {

  const [loading, setLoading] = useState(null);
  const [listaProductos, setListaProductos] = useState([]);
  // Para la paginación
  const [cantidadResultados, setCantidadResultados] = useState(0);
  const [resultadosPorPagina, setResultadosPorPagina] = useState(0);

  const { search } = useLocation();
  const searchParams = new URLSearchParams(search);
  const category = searchParams.get("categoria");
  const city = searchParams.get("ciudad");
  const inicio = searchParams.get("inicio");
  const fin = searchParams.get("fin");
  const page = searchParams.get("page") ? searchParams.get("page") : 1;
  const inicioFormateado = new Date(inicio).toISOString().split("T")[0];
  const finFormateado = new Date(fin).toISOString().split("T")[0];

  //console.log(listaProductos);

  useEffect(() => {

    setCantidadResultados(0);
    serviceCalls();

  }, [category, city, inicio, fin, favorite, usuario]);

  useEffect(() => {

    serviceCalls();

  }, [page]);

  function serviceCalls() {

    setListaProductos([]);
    setLoading(true);
    const productService = new ProductoService();

    // Filtros de búsqueda
    if (category) {
      productService
        .getProductByCategoryPaginado(category, page)
        .then((data) => {
          setCantidadResultados(data.resultadosTotales);
          setResultadosPorPagina(data.resultadosPorPagina);
          setListaProductos(data.productos);
        })
        .catch((e) => {
          if (e.response) console.log(e.response.data.error);
          else
            console.log("Ha ocurrido un error. Por favor intente más tarde.");
        })
        .finally( () => setLoading(false) );
    } else if (inicio && fin && city) {
      productService
        .getProductByFechasPaginado(inicioFormateado, finFormateado, city, page)
        .then((data) => {
          setCantidadResultados(data.resultadosTotales);
          setResultadosPorPagina(data.resultadosPorPagina);
          setListaProductos(data.productos);
        })
        .catch((e) => {
          if (e.response) console.log(e.response.data.error);
          else
            console.log("Ha ocurrido un error. Por favor intente más tarde.");
        })
        .finally( () => setLoading(false) );
    } else if (inicio && fin) {
      productService
        .getProductByFechasPaginado(inicioFormateado, finFormateado, null, page)
        .then((data) => {
          setCantidadResultados(data.resultadosTotales);
          setResultadosPorPagina(data.resultadosPorPagina);
          setListaProductos(data.productos);
        })
        .catch((e) => {
          if (e.response) console.log(e.response.data.error);
          else
            console.log("Ha ocurrido un error. Por favor intente más tarde.");
        })
        .finally( () => setLoading(false) );
    } else if (city) {
      productService
        .getProductByCityPaginado(city, page)
        .then((data) => {
          setCantidadResultados(data.resultadosTotales);
          setResultadosPorPagina(data.resultadosPorPagina);
          setListaProductos(data.productos);
        })
        .catch((e) => {
          if (e.response) console.log(e.response.data.error);
          else
            console.log("Ha ocurrido un error. Por favor intente más tarde.");
        })
        .finally( () => setLoading(false) );
    } else if (favorite && usuario && usuario.rol === "ROLE_CUSTOMER") {
      productService
        .getFavoritesByUser(usuario.id)
        .then((data) => {
          setListaProductos(data);
        })
        .catch((e) => {
          if (e.response) console.log(e.response.data.error);
          else
            console.log("Ha ocurrido un error. Por favor intente más tarde.");
        })
        .finally( () => setLoading(false) );
    } else {
      productService
        .getAllProductsPaginado(page)
        .then((data) => {
          setCantidadResultados(data.resultadosTotales);
          setResultadosPorPagina(data.resultadosPorPagina);
          setListaProductos(data.productos);
        })
        .catch((e) => {
          if (e.response) console.log(e.response.data.error);
          else
            console.log("Ha ocurrido un error. Por favor intente más tarde.");
        })
        .finally( () => setLoading(false) );
    }
  }

  function handleEliminacionFavorito(productoId)
  {
    // Para que se actualice la eliminación en /favoritos
    setListaProductos(listaProductos.filter((producto) => producto.id !== productoId));
    setCantidadResultados((cantidadResultados) => cantidadResultados - 1);
  }


  if (loading)
    return <Loader absolute={false} />;

  return (
    <div className={styles.productsCardContainer}>
      {!favorite ? (<h2 className={styles.h2}>{`${title} (${cantidadResultados})`}</h2>) :( null)}
      {/* <h2 className={styles.h2}>{`${title} (${
        !favorite ? listaProductos.length : cantidadResultados
      })`}</h2> */}
      <TransitionGroup className={styles.cardContainer}>
        {listaProductos.map((item) => {
          return favorite ? (
            <CSSTransition
              key={item.id}
              timeout={500}
              classNames="card-transition-favorite"
            >
              <CardProduct
                item={item}
                favorite={favorite}
                usuario={usuario}
                onEliminacionFavorito={handleEliminacionFavorito}
                onDeleteLastFavorite={onDeleteLastFavorite}
                cantidadResultados={listaProductos.length}
              />
            </CSSTransition>
          ) : (
            <CSSTransition key={item.id} timeout={0}>
              <CardProduct
                item={item}
                favorite={favorite}
                usuario={usuario}
              />
            </CSSTransition>
          );
        })}
        <div className={styles.alineacionCantidadImpar} />
        <div className={styles.alineacionCantidadImpar} />
      </TransitionGroup>
      {!favorite && listaProductos.length !== 0 ? (
        <Paginacion
          page={page}
          cantidadResultados={cantidadResultados}
          resultadosPorPagina={resultadosPorPagina}
        />
      ) : null}
    </div>
  );
};


export default CardProductList;