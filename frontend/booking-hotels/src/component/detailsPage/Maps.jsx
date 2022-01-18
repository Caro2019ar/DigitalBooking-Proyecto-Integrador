import React, { useState } from "react";
import {
  GoogleMap,
  useJsApiLoader,
  Marker,
  InfoBox,
} from "@react-google-maps/api";
import credentials from "../../credentials";

//componente
import HorizontalLine from "../componentGlobal/HorizontalLine";

// CSS:
import styles from "../../styles/detailsPage/maps.module.css";
import "../../styles/detailsPage/mapsInfoBox.css";

const Maps = ({ producto }) => {
  const [map, setMap] = useState(null);
  const [coordinates, setCoordinates] = useState({
    lat: producto.latitud,
    lng: producto.longitud,
  });
  const [markerClicked, setMarkerClicked] = useState(false);

  /* map configuration */
  const containerStyle = {
    height: "450px",
  };

  const { isLoaded } = useJsApiLoader({
    id: "google-map-script",
    googleMapsApiKey: `${credentials.mapsKey}`,
  });

  const onUnmount = React.useCallback(function callback(map) {
    setMap(null);
  }, []);

  const handleMarkerClick = () =>
    setMarkerClicked((markerClicked) => !markerClicked);

  return (
    <>
      <div id="map" className={styles.maps}>
        <h2 className={styles.title}>¿Dónde vas a estar?</h2>
        <HorizontalLine />
        <div className={styles.location}>Buenos Aires, Argentina</div>
        <div className={styles.imgMap}>
          {isLoaded ? (
            <GoogleMap
              clickableIcons={true}
              mapContainerStyle={containerStyle}
              center={coordinates}
              zoom={15}
              onUnmount={onUnmount}
            >
              {/* Child components, such as markers, info windows, etc. */}
              <Marker position={coordinates} onClick={handleMarkerClick}>
                {markerClicked ? (
                  <InfoBox onCloseClick={handleMarkerClick}>
                    <div className={styles.infoBox}>{producto.direccion}</div>
                  </InfoBox>
                ) : null}
              </Marker>
            </GoogleMap>
          ) : (
            <>Loading maps failed</>
          )}
        </div>
      </div>
    </>
  );
};

export default React.memo(Maps);
