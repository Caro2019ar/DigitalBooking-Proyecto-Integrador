import { Link } from "react-router-dom";
import { useState, useEffect } from "react";

import { CategoriaService } from "../../Service/CategoriaService";
import Loader from "../componentGlobal/Loader";

//CSS
import styles from "../../styles/index/categories.module.css";


function Category() {
  
  const [loading, setLoading] = useState(null);
  const [listaCategorias, setListaCategorias] = useState([]);


  useEffect( () => {

    setLoading(true);
    const categoriaService = new CategoriaService();
    categoriaService.getAllCategorias()
    .then( (data) => setListaCategorias(data) )
    .catch( (e) => {
      if (e.response) console.log(e.response.data.error);
      else console.log("Ha ocurrido un error. Por favor intente más tarde.");
    })
    .finally( () => setLoading(false) );

  }, []);


  /*if (loading)
    return <Loader absolute={true} />;*/

  return (
    <div className={styles.category}>
      <h2>Buscar por tipo de alojamiento</h2>
      <div className={styles.typeContainer}>
        {listaCategorias.map((item) => (
          <div key={item.titulo} className={styles.typeCard}>
            <Link to={`./?categoria=${item.titulo}`}>
              <img src={item.urlImagen} alt={item.titulo} />
            </Link>

            <h3>{item.titulo}</h3>
            <p>{item.descripcion}</p>
          </div>
        ))}
      </div>
    </div>
  );
}


export default Category;