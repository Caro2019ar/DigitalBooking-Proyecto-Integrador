import React from "react";

// Librería
import Gallery from "react-image-gallery";
import "react-image-gallery/styles/css/image-gallery.css";
import "../../styles/detailsPage/libraryImageGallery.css";
import imageShare from "../../image/share-alt-light.svg";
import stylesGallery from "../../styles/detailsPage/imageGallery.module.css";

const images = [
  // {
  //   original: "https://picsum.photos/id/1018/1000/600/",
  //   //   thumbnail: 'https://picsum.photos/id/1018/250/150/',
  // },
  // {
  //   original: "https://picsum.photos/id/1015/1000/600/",
  //   //   thumbnail: 'https://picsum.photos/id/1015/250/150/',
  // },
  // {
  //   original: "https://picsum.photos/id/1019/1000/600/",
  //   //   thumbnail: 'https://picsum.photos/id/1019/250/150/',
  // },
];

const ImageGallery = ({producto}) => {
  
  let productoCopy = JSON.parse(JSON.stringify(producto));

  if (Object.keys(producto).length !== 0) {
    for(let imagen of productoCopy.imagenes) {
      delete Object.assign(imagen, {["original"]: imagen["url"] })["url"];
    }
    productoCopy.imagenes.forEach((item)=>
images.push(item)
    )
  }

  return (
    <>
      <div className={stylesGallery.galleryContainer}>
        {/* // onClick={handleClick} */}
        <div className={stylesGallery.iconsContainer}>
          <img src={imageShare} />
          <span>
            <i class="far fa-heart"></i>
          </span>
        </div>
        <div className={stylesGallery.galleryLibraryContainer}>
          <Gallery
            items={images}
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
