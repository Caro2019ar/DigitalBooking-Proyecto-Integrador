import React from "react";
import { useState, useEffect } from "react";
import { Redirect, useHistory } from "react-router-dom";
import axios from "axios";
import apiUrl from "../../apiUrl.js";
import Loader from "../componentGlobal/Loader.jsx";

// Importación de componentes:
import HeaderDetailsPage from "../detailsPage/HeaderDetailsPage";
import { ProductoService } from "../../Service/ProductoService";
import SelectBuscador from "./SelectBuscador.jsx";

// Hooks
import useInput from "../../hooks/useInput";
import useInputOnlyNumber from "../../hooks/useOnlyNumber";

//css
import styles from "../../styles/global.module.css";
import stylesSelect from "../../styles/bookingPage/hours.module.css";
import formStyles from "../../styles/forms/form.module.css";
import stylesNewProduct from "../../styles/newProduct/newProduct.module.css";

const NewProductPage = ({ usuario }) => {
  const [selectedCity, setSelectedCity] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("");
  const [listFeatures, setListFeature] = useState([]);
  const [listFeaturesChecked, setListFeaturesChecked] = useState([]);
  const [nuevaImagen, setNuevaImagen] = useState("");

  //Uso directo del useState para setear los errores que contengan los input según la acción del usuario
  //sobre los form:
  const [nameErr, setnameErr] = useState({});
  const [descriptionErr, setDescriptionErr] = useState({});
  const [cityErr, setCityErr] = useState({});
  const [categoryErr, setCategoryErr] = useState({});
  const [imagesErr, setImagesErr] = useState({});
  const [featureErr, setFeatureErr] = useState({});
  const [addressErr, setAddressErr] = useState({});
  const [latitudeErr, setLatitudeErr] = useState({});
  const [longitudeErr, setLongitudeErr] = useState({});
  const [politicsErr, setPoliticsErr] = useState({});
  const [securityErr, setSecurityErr] = useState({});
  const [normsErr, setNormsErr] = useState({});
  const [arrayImages, setArrayImages] = useState([]);
  const [errorMSG, setErrorMSG] = useState("");

  // Llamada del hook useInput para el control del los valores ingresados en cada uno de los inputs de los formularios
  const name = useInput("", setnameErr);
  const description = useInput("", setDescriptionErr);
  const address = useInput("", setAddressErr);
  const latitude = useInputOnlyNumber("", setLatitudeErr);
  const longitude = useInputOnlyNumber("", setLongitudeErr);
  const politics = useInput("", setPoliticsErr);
  const security = useInput("", setSecurityErr);
  const norms = useInput("", setNormsErr);

  //Para redirección a login con registro exitoso
  const [registerSuccess, setRegisterSuccess] = useState(false);

  //Para errores en el llamado a la API de registro de clientes
  const [registerErr, setRegisterErr] = useState("");

  const [loading, setLoading] = useState(null);

  let history = useHistory();

  useEffect(() => {
    setLoading(true);
    const listFeatures = async () => {
      try {
        const response = await axios.get(apiUrl + "/caracteristicas");
        setListFeature(response.data);
        setLoading(false);
      } catch (err) {
        console.log(err);
      }
    };
    listFeatures();
  }, []);

  const handleSubmit = (event) => {
    event.preventDefault();
    setRegisterErr("");
    const isValid = formValidation();

    isValid
      ? createProduct(event)
      : setErrorMSG(
          "Por favor, verifica que todos los campos fueron completados"
        );
  };

  function handleCheckboxClick(e) {
    if (e.target.checked) {
      setListFeaturesChecked([
        ...listFeaturesChecked,
        parseInt(e.target.value),
      ]);
      setFeatureErr({});
    } else
      setListFeaturesChecked(
        listFeaturesChecked.filter((id) => id !== parseInt(e.target.value))
      );
  }

  function handleClickAgregarImagen(e) {
    if (
      !nuevaImagen.startsWith("https://") &&
      !nuevaImagen.startsWith("http://")
    )
      setImagesErr({
        urlInvalida: "La dirección ingresada no es una URL válida",
      });
    else if (arrayImages.includes(nuevaImagen))
      setImagesErr({
        urlRepetida:
          "La dirección ingresada corresponde a una imagen ya registrada",
      });
    else {
      setArrayImages([...arrayImages, nuevaImagen]);
      setNuevaImagen("");
      setImagesErr({});
    }
  }

  function handleClickEliminarImagen(e) {
    setArrayImages([
      ...arrayImages.filter((item, i) => i !== parseInt(e.target.id)),
    ]);
  }

  function getArrayImagesFormat() {
    const arrayImagenesFormat = [];
    for (let i = 0; i < arrayImages.length; i++) {
      const titulo = "imagen-" + i;
      const url = arrayImages[i];
      arrayImagenesFormat.push({ titulo, url });
    }

    return arrayImagenesFormat;
  }

  function getArrayFeaturesFormat() {
    const arrayFeaturesFormat = [];
    for (let i = 0; i < listFeaturesChecked.length; i++) {
      arrayFeaturesFormat.push({ id: listFeaturesChecked[i] });
    }

    return arrayFeaturesFormat;
  }

  function createProduct(event) {
    const dataProduct = {
      nombre: name.value,
      descripcion: description.value,
      ciudad: { id: selectedCity },
      categoria: { id: selectedCategory },
      imagenes: getArrayImagesFormat(),
      caracteristicas: getArrayFeaturesFormat(),
      direccion: address.value,
      latitud: latitude.value,
      longitud: longitude.value,
      politica: {
        normas: norms.value,
        saludYSeguridad: security.value,
        cancelacion: politics.value,
      },
    };

    const productService = new ProductoService();
    console.log(dataProduct);
    productService
      .registrarProducto(dataProduct)
      .then((data) => {
        setRegisterSuccess(true);
        history.push("/new-product-success");
      })
      .catch((e) => {
        if (e.response) setRegisterErr(e.response.data.error);
        else
          setRegisterErr(
            "Lamentablemente no ha podido registrarse este producto.\nPor favor intente más tarde."
          );
      });
  }

  const formValidation = () => {
    const nameErr = {};
    const descriptionErr = {};
    const cityErr = {};
    const categoryErr = {};
    const imagesErr = {};
    const featureErr = {};
    const addressErr = {};
    const latitudeErr = {};
    const longitudeErr = {};
    const politicsErr = {};
    const securityErr = {};
    const normsErr = {};
    let isValid = true;

    //Validate name
    if (name.value === "") {
      nameErr.emptyNameField = "Este campo es obligatorio";
      isValid = false;
    } else if (name.value.trim().length < 3) {
      nameErr.nameIsTooShort =
        "El nombre es muy corto, debe tener al menos 3 caracteres";
      isValid = false;
    }

    //Validate description
    if (description.value === "") {
      descriptionErr.emptyDescriptionField = "Este campo es obligatorio";
      isValid = false;
    } else if (description.value.trim().length < 30) {
      descriptionErr.descriptionIsTooShort =
        "La descripción es muy corta, debe tener al menos 30 caracteres";
      isValid = false;
    }

    //Validate city
    if (selectedCity === "") {
      cityErr.emptyCityField = "Este campo es obligatorio";
      isValid = false;
    }

    //Validate category
    if (selectedCategory === "") {
      categoryErr.emptyCategoryField = "Este campo es obligatorio";
      isValid = false;
    }

    //Validate images
    if (arrayImages.length === 0) {
      imagesErr.emptyImagesField = "Este campo es obligatorio";
      isValid = false;
    }

    //Validate feature
    if (listFeaturesChecked.length < 1) {
      featureErr.emptyFeatureField = "Debes marcar al menos un atributo";
      isValid = false;
    }

    //Validate address
    if (address.value === "") {
      addressErr.emptyAddressField = "Este campo es obligatorio";
      isValid = false;
    } else if (address.value.trim().length < 3) {
      addressErr.addressIsTooShort =
        "La dirección es muy corta, debe tener al menos 3 caracteres";
      isValid = false;
    }

    //Validate latitude
    if (latitude.value === "") {
      latitudeErr.emptyLatitudeField = "Este campo es obligatorio";
      isValid = false;
    } else if (latitude.value.trim().length < 3) {
      latitudeErr.latitudeIsTooShort =
        "La dirección es muy corta, debe tener al menos 3 caracteres";
      isValid = false;
    }

    //Validate longitude
    if (longitude.value === "") {
      longitudeErr.emptyLongitudeField = "Este campo es obligatorio";
      isValid = false;
    } else if (longitude.value.trim().length < 3) {
      longitudeErr.longitudeIsTooShort =
        "La dirección es muy corta, debe tener al menos 3 caracteres";
      isValid = false;
    }

    //Validate politics
    if (politics.value === "") {
      politicsErr.emptyPoliticsField = "Este campo es obligatorio";
      isValid = false;
    }

    //Validate security
    if (security.value === "") {
      securityErr.emptySecurityField = "Este campo es obligatorio";
      isValid = false;
    }

    //Validate norms
    if (norms.value === "") {
      normsErr.emptyNormsField = "Este campo es obligatorio";
      isValid = false;
    }

    setnameErr(nameErr);
    setDescriptionErr(descriptionErr);
    setCityErr(cityErr);
    setCategoryErr(categoryErr);
    setImagesErr(imagesErr);
    setFeatureErr(featureErr);
    setAddressErr(addressErr);
    setLatitudeErr(latitudeErr);
    setLongitudeErr(longitudeErr);
    setPoliticsErr(politicsErr);
    setSecurityErr(securityErr);
    setNormsErr(normsErr);

    return isValid;
  };

  if (!usuario) return <Redirect to="/login" />;
  else if (usuario.rol === "ROLE_CUSTOMER") return <Redirect to="/" />;

  return loading ? (
    <Loader absolute={true} />
  ) : (
    <>
      <HeaderDetailsPage producto="" title="Administración" />
      <div
        className={`${formStyles.form} ${stylesNewProduct.form} ${stylesSelect.form}`}
      >
        <h2>Crear propiedad</h2>
        <form
          method="post"
          onSubmit={handleSubmit}
          // autoComplete="off"
          noValidate
        >
          <div
            className={`${formStyles.formGroup} ${formStyles.inputDesktopGroup} ${stylesNewProduct.formGroup}`}
          >
            <div
              className={`${formStyles.formSubGroup} ${stylesNewProduct.formSubGroup} ${stylesNewProduct.first4}`}
            >
              <div className={stylesNewProduct.group}>
                <label>
                  <p>Nombre de la propiedad</p>
                </label>
                <input
                  {...name}
                  className={`
                              ${formStyles.formControl} ${
                    stylesNewProduct.formControl
                  }
                              ${
                                Object.keys(nameErr).length !== 0
                                  ? formStyles.inputError
                                  : ""
                              }
                          `}
                  type="text"
                  name="name"
                  id="name"
                  placeholder="Nombre"
                ></input>
                {Object.keys(nameErr).map((key) => {
                  return (
                    <div key={key} className={formStyles.errorMsg}>
                      {nameErr[key]}
                    </div>
                  );
                })}
              </div>

              <div className={stylesNewProduct.group}>
                <label>
                  <p>Categoría</p>
                </label>
                <div className={`${stylesNewProduct.boxFormCity}`}>
                  <SelectBuscador
                    recurso={"categorias"}
                    hayError={
                      Object.keys(categoryErr).length === 0 ? false : true
                    }
                    onChange={(id) => {
                      setSelectedCategory(id);
                      setCategoryErr({});
                    }}
                  />
                  <i></i>
                </div>
                {Object.keys(categoryErr).map((key) => {
                  return (
                    <div key={key} className={formStyles.errorMsg}>
                      {categoryErr[key]}
                    </div>
                  );
                })}
              </div>

              <div className={stylesNewProduct.group}>
                <label>
                  <p>Dirección</p>
                </label>
                <input
                  {...address}
                  className={`
                              ${formStyles.formControl} ${
                    stylesNewProduct.formControl
                  }
                              ${
                                Object.keys(addressErr).length !== 0
                                  ? formStyles.inputError
                                  : ""
                              }
                          `}
                  type="text"
                  name="address"
                  id="address"
                  placeholder="Dirección"
                ></input>
                {Object.keys(addressErr).map((key) => {
                  return (
                    <div key={key} className={formStyles.errorMsg}>
                      {addressErr[key]}
                    </div>
                  );
                })}
              </div>

              <div className={`${stylesNewProduct.group}`}>
                <label>
                  <p>Ciudad</p>
                </label>
                <div className={`${stylesNewProduct.boxFormCity}`}>
                  <SelectBuscador
                    recurso={"ciudades"}
                    hayError={Object.keys(cityErr).length === 0 ? false : true}
                    onChange={(id) => {
                      setSelectedCity(id);
                      setCityErr({});
                    }}
                  />
                  <i></i>
                </div>
                {Object.keys(cityErr).map((key) => {
                  return (
                    <div key={key} className={formStyles.errorMsg}>
                      {cityErr[key]}
                    </div>
                  );
                })}
              </div>

              <div className={stylesNewProduct.group}>
                <label>
                  <p>Latitud</p>
                </label>
                <input
                  {...latitude}
                  className={`
                              ${formStyles.formControl} ${
                    stylesNewProduct.formControl
                  }
                              ${
                                Object.keys(latitudeErr).length !== 0
                                  ? formStyles.inputError
                                  : ""
                              }
                          `}
                  type="text"
                  name="latitude"
                  id="latitude"
                  placeholder="Latitud"
                ></input>
                {Object.keys(latitudeErr).map((key) => {
                  return (
                    <div key={key} className={formStyles.errorMsg}>
                      {latitudeErr[key]}
                    </div>
                  );
                })}
              </div>
              <div className={stylesNewProduct.group}>
                <label>
                  <p>Longitud</p>
                </label>
                <input
                  {...longitude}
                  className={`
                              ${formStyles.formControl} ${
                    stylesNewProduct.formControl
                  }
                              ${
                                Object.keys(longitudeErr).length !== 0
                                  ? formStyles.inputError
                                  : ""
                              }
                          `}
                  type="text"
                  name="longitude"
                  id="longitude"
                  placeholder="Longitud"
                ></input>
                {Object.keys(longitudeErr).map((key) => {
                  return (
                    <div key={key} className={formStyles.errorMsg}>
                      {longitudeErr[key]}
                    </div>
                  );
                })}
              </div>
            </div>
            <label>
              <p>Descripción</p>
            </label>
            <textarea
              {...description}
              className={`
                          ${formStyles.formControl} ${
                stylesNewProduct.formControl
              }
                          ${
                            Object.keys(descriptionErr).length !== 0
                              ? formStyles.inputError
                              : ""
                          }
                      `}
              type="text"
              name="description"
              id="description"
              placeholder="Describa aquí su establecimiento..."
            ></textarea>
            {Object.keys(descriptionErr).map((key) => {
              return (
                <div key={key} className={formStyles.errorMsg}>
                  {descriptionErr[key]}
                </div>
              );
            })}
          </div>
          <div
            id="boxFeature"
            className={`${formStyles.formGroup} ${formStyles.inputDesktopGroup} ${stylesNewProduct.formGroup}`}
          >
            <h3>Agregar atributos</h3>
            <div
              className={`
                ${formStyles.formSubGroup}
                ${stylesNewProduct.formSubGroup}
                ${stylesNewProduct.boxAllCheckbox}
                ${
                  Object.keys(featureErr).length !== 0
                    ? stylesNewProduct.boxAllCheckboxError
                    : ""
                }
              `}
            >
              {listFeatures.map((item) => (
                <div
                  key={`icon-${item.id}`}
                  className={stylesNewProduct.boxCheckbox}
                >
                  <label id="c1" className={stylesNewProduct.labelCheckbox}>
                    <input
                      className={stylesNewProduct.input}
                      type="checkbox"
                      name="feature"
                      id={`${item.id}`}
                      value={item.id}
                      onChange={handleCheckboxClick}
                    />
                    <span className={stylesNewProduct.checkMark}></span>
                    <span className="fa-li">
                      <i
                        className={`fas ${item.icono} ${stylesNewProduct.icon}`}
                      ></i>
                    </span>
                    <p>{item.nombre}</p>
                  </label>
                </div>
              ))}
            </div>
            {Object.keys(featureErr).map((key) => {
              return (
                <div key={key} className={formStyles.errorMsg}>
                  {featureErr[key]}
                </div>
              );
            })}
          </div>
          <div
            className={`${formStyles.formGroup} ${formStyles.inputDesktopGroup} ${stylesNewProduct.formGroup}`}
          >
            <h3>Políticas del producto</h3>
            <div
              className={`${formStyles.formSubGroup} ${stylesNewProduct.formSubGroup} ${stylesNewProduct.politicGroup}`}
            >
              <div className={stylesNewProduct.politic}>
                <h4>Normas de la casa</h4>
                <label>
                  <p>Descripción</p>
                </label>
                <textarea
                  {...norms}
                  className={`
                              ${formStyles.formControl} ${
                    stylesNewProduct.formControl
                  }
                              ${
                                Object.keys(normsErr).length !== 0
                                  ? formStyles.inputError
                                  : ""
                              }
                          `}
                  type="text"
                  name="norms"
                  id="norms"
                  placeholder="Escriba aquí..."
                ></textarea>
                {Object.keys(normsErr).map((key) => {
                  return (
                    <div key={key} className={formStyles.errorMsg}>
                      {normsErr[key]}
                    </div>
                  );
                })}
              </div>
              <div className={stylesNewProduct.politic}>
                <h4>Salud y seguridad</h4>
                <label>
                  <p>Descripción</p>
                </label>
                <textarea
                  {...security}
                  className={`
                              ${formStyles.formControl} ${
                    stylesNewProduct.formControl
                  }
                              ${
                                Object.keys(securityErr).length !== 0
                                  ? formStyles.inputError
                                  : ""
                              }
                          `}
                  type="text"
                  name="security"
                  id="security"
                  placeholder="Escriba aquí..."
                ></textarea>
                {Object.keys(securityErr).map((key) => {
                  return (
                    <div key={key} className={formStyles.errorMsg}>
                      {securityErr[key]}
                    </div>
                  );
                })}
              </div>
              <div className={stylesNewProduct.politic}>
                <h4>Políticas de cancelación</h4>
                <label>
                  <p>Descripción</p>
                </label>
                <textarea
                  {...politics}
                  className={`
                              ${formStyles.formControl} ${
                    stylesNewProduct.formControl
                  }
                              ${
                                Object.keys(politicsErr).length !== 0
                                  ? formStyles.inputError
                                  : ""
                              }
                          `}
                  type="text"
                  name="politics"
                  id="politics"
                  placeholder="Escriba aquí..."
                ></textarea>
                {Object.keys(politicsErr).map((key) => {
                  return (
                    <div key={key} className={formStyles.errorMsg}>
                      {politicsErr[key]}
                    </div>
                  );
                })}
              </div>
            </div>
          </div>
          <div
            className={`${formStyles.formGroup} ${formStyles.inputDesktopGroup} ${stylesNewProduct.formGroup}`}
          >
            <h3>Cargar imágenes</h3>
            {arrayImages.map((item, i) => (
              <div
                key={i}
                className={`${formStyles.formSubGroup} ${stylesNewProduct.formSubGroup} ${stylesNewProduct.image}`}
              >
                <input
                  type="text"
                  name={"imagen" + i}
                  id={i}
                  value={item}
                  readOnly
                  className={`
                          ${formStyles.formControl} ${stylesNewProduct.formControlMostrar}`}
                />
                <button
                  type="button"
                  onClick={handleClickEliminarImagen}
                  id={i}
                  className={stylesNewProduct.delete}
                />
              </div>
            ))}
            <div
              className={`${formStyles.formSubGroup} ${stylesNewProduct.formSubGroup} ${stylesNewProduct.image}`}
            >
              <input
                type="text"
                name="nuevaImagen"
                id="nuevaImagen"
                placeholder="Insertar URL..."
                value={nuevaImagen}
                onChange={(e) => setNuevaImagen(e.target.value)}
                className={`
                            ${formStyles.formControl} ${
                  stylesNewProduct.formControl
                }
                            ${
                              Object.keys(imagesErr).length !== 0
                                ? formStyles.inputError
                                : ""
                            }
                        `}
              />
              <button
                type="button"
                onClick={handleClickAgregarImagen}
                id="btnImages"
                className={stylesNewProduct.plus}
              />
            </div>
            {Object.keys(imagesErr).map((key) => {
              return (
                <div key={key} className={formStyles.errorMsg}>
                  {imagesErr[key]}
                </div>
              );
            })}
          </div>
          <div className={`${formStyles.errorBookingMsg}`}>{errorMSG}</div>
          <div className={formStyles.btnContainer}>
            <button
              id="btnCrear"
              className={`${styles.button} ${formStyles.buttonForm} ${stylesNewProduct.button}`}
            >
              Crear
            </button>
          </div>
        </form>
      </div>
    </>
  );
};

export default NewProductPage;
