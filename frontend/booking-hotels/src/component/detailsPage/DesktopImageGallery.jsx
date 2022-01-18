import React, { useState } from "react";

// Librería
import Gallery, { LeftNav } from "react-image-gallery";
import "react-image-gallery/styles/css/image-gallery.css";

// Librería Fontawesome, uso de íconos:
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTimes } from "@fortawesome/free-solid-svg-icons";

// CSS
import globalStyles from "../../styles/global.module.css";
import "../../styles/detailsPage/libraryImageGallery.css";
import styles from "../../styles/detailsPage/desktopImageGallery.module.css";


const DesktopImageGallery = ({ producto }) => {

  const [showGallery, setShowGallery] = useState(false);

  const listOfUrl = [...new Set(producto.imagenes?.map((it) => it.url))];

  let productoCopy = JSON.parse(JSON.stringify(producto));

  if (Object.keys(producto).length !== 0) {
    for (let imagen of productoCopy.imagenes) {
      delete Object.assign(imagen, { ["original"]: imagen["url"] })["url"];
    }
  }

  const closeGallery = () => {
    setShowGallery(false);
    document.body.classList.remove(globalStyles["no-scroll"]);
  };

  const openGallery = () => {
    setShowGallery(true);
    document.body.classList.add(globalStyles["no-scroll"]);
  };


  return (
    <div className={styles.galleryContainer}>
      <div className={styles.imagesContainer}>
        <div className={styles.imgLarge}>
          <img src={listOfUrl[0]} alt="" />
        </div>
        <div className={styles.imgSmall1}>
          <img src={listOfUrl[1]} alt="" />
        </div>
        <div className={styles.imgSmall2}>
          <img src={listOfUrl[2]} alt="" />
        </div>
        <div className={styles.imgSmall3}>
          <img src={listOfUrl[3]} alt="" />
        </div>
        <div className={styles.imgSmall4}>
          <img src={listOfUrl[4]} alt="" />
        </div>
        <button
          type="button"
          className={`${styles.button}`}
          onClick={() => openGallery()}
        >
          <p>Ver más</p>
        </button>
      </div>
      <div>
        <div className={styles.galleryContainerShow}>
          {showGallery ? (
            <div className={styles.atenuarFondoShow}>
              <Gallery
                items={productoCopy.imagenes.map((img)=>{
                  return{
                    id:img.id,
                    original:img.original,
                    thumbnail:img.original,
                  }
                })}
                showThumbnails={true}
                additionalClass="imgSize"
                showPlayButton={false}
                showFullscreenButton={false}
                showIndex={true}
                autoPlay={false}
                slideDuration={300}
              />

              <FontAwesomeIcon
                data-test="fontAwesomeIcon"
                className={styles.closeIcon}
                icon={faTimes}
                onClick={() => closeGallery()}
              />
            </div>
          ) : (
            " "
          )}
        </div>
      </div>
    </div>
  );
};


export default DesktopImageGallery;