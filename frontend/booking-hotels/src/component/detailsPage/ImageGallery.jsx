import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import { ProductoService } from "../../Service/ProductoService";
import { ClienteService } from "../../Service/ClienteService";

// Librería Galería
import Gallery from "react-image-gallery";

// Librería Share
import { EmailShareButton, EmailIcon, FacebookShareButton, FacebookIcon, TwitterShareButton, TwitterIcon, WhatsappShareButton, WhatsappIcon } from "react-share";

// CSS
import "react-image-gallery/styles/css/image-gallery.css";
import "../../styles/detailsPage/libraryImageGallery.css";
import imageShareDefault from "../../image/share-alt-solid.svg";
import imageShareOnClick from "../../image/share-alt.svg";
import stylesGallery from "../../styles/detailsPage/imageGallery.module.css";


const ImageGallery = ({ producto, usuario }) => {

  let productoCopy = JSON.parse(JSON.stringify(producto));

  const [shareClicked, setShareClicked] = useState(false);
  const [esFavorito, setEsFavorito] = useState(null);
  // Para manejo de la animación del like
  const [productJustLiked, setProductJustLiked] = useState(false);

  if (Object.keys(producto).length !== 0) {
    for (let imagen of productoCopy.imagenes) {
      delete Object.assign(imagen, { ["original"]: imagen["url"] })["url"];
    }
  }

  useEffect(() => {

    if (usuario && usuario.rol === "ROLE_CUSTOMER")
    {
      const productoService = new ProductoService();

      productoService.isFavoriteByUser(producto.id, usuario.id)
      .then( (data) => setEsFavorito(data.message === "true") )
      .catch( (e) => {
        if (e.response) console.log(e.response.data.error);
        else console.log("Ha ocurrido un error. Por favor intente más tarde.");
      });
    }

  }, [usuario]);

  const handleShareClick = () => setShareClicked( (shareClicked => !shareClicked ));

  function handleFavoriteIconClick()
  {
    const clienteService = new ClienteService();

    if (esFavorito)
    {
      clienteService.eliminarFavorito(usuario.id, producto.id)
      .then( data => {
        setEsFavorito(false);
        setProductJustLiked(false);
      })
      .catch( e => {
        if (e.response) console.log(e.response.data.error);
        else console.log("Ha ocurrido un error. Por favor intente más tarde.");
      });
    }
    else
    {
      clienteService.agregarFavorito(usuario.id, producto.id)
      .then( data => {
        setEsFavorito(true);
        setProductJustLiked(true);
      })
      .catch( e => {
        if (e.response) console.log(e.response.data.error);
        else console.log("Ha ocurrido un error. Por favor intente más tarde.");
      });
    }
  }

  //console.log(productoCopy.imagenes);
  

  return (
    <>
      <div className={stylesGallery.galleryContainer}>
        <div className={stylesGallery.iconsContainer}>

          <div className={stylesGallery.shareAndNetworkIcons}>
            <div className={stylesGallery.shareIconContainer} onClick={handleShareClick}>
              <img src={shareClicked ? imageShareOnClick : imageShareDefault}/>
            </div>

            <div className={`
                ${stylesGallery.networkIconsContainerHide}
                ${shareClicked ? stylesGallery.networkIconsContainerShow : ''}
              `}>
              <div className={stylesGallery.network}>
                <EmailShareButton
                  url={window.location.href}
                  subject={'Mira este increíble ' + producto.categoria.titulo + '!!'}
                  body={'Se llama ' + producto.nombre + ' y se encuentra en ' + producto.ciudad.nombre + ', ' + producto.ciudad.pais + '.'}
                  className={stylesGallery.networkButton}
                >
                  <EmailIcon size={28} round />
                </EmailShareButton>
              </div>
              <div className={stylesGallery.network}>
                <FacebookShareButton
                  url={window.location.href}
                  quote={'Mira este increíble ' + producto.categoria.titulo + '!!'}
                  className={stylesGallery.networkButton}
                >
                  <FacebookIcon size={28} round />
                </FacebookShareButton>
              </div>
              <div className={stylesGallery.network}>
                <TwitterShareButton
                  url={window.location.href}
                  title={'Mira este increíble ' + producto.categoria.titulo + '!!'}
                  className={stylesGallery.networkButton}
                >
                  <TwitterIcon size={28} round />
                </TwitterShareButton>
              </div>
              <div className={stylesGallery.network}>
                <WhatsappShareButton
                  url={window.location.href}
                  title={'Mira este increíble ' + producto.categoria.titulo + '!!'}
                  className={stylesGallery.networkButton}
                >
                  <WhatsappIcon size={28} round />
                </WhatsappShareButton>
              </div>
            </div>
          </div>

          {(usuario && usuario.rol === "ROLE_CUSTOMER") ? (
          <span className={`
                  ${esFavorito ? stylesGallery.favoriteIconYes : stylesGallery.favoriteIconNo}
                  ${productJustLiked ? stylesGallery.favoriteIconYesOnClick : ''}
                `}
                onClick={() => handleFavoriteIconClick()}>
            <i className={esFavorito ? "fas fa-heart" : "far fa-heart"}></i>
          </span>
          ) : null}
        </div>
        <div className={stylesGallery.galleryLibraryContainer}>
          <Gallery
            items={productoCopy.imagenes}
            className={stylesGallery}
            additionalClass="anaru"
            showPlayButton={false}
            showFullscreenButton={false}
            showIndex={true}
            showNav={false}
            autoPlay={true}
            slideDuration={300}
          />
        </div>
      </div>
    </>
  );
};

export default ImageGallery;
