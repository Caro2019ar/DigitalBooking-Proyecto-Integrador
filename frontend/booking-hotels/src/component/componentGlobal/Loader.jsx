import globalStyles from "../../styles/global.module.css";


function Loader({ absolute }) {
   
   return absolute ? (
      <div className={globalStyles["lds-hourglass-absolute"]}></div> )
      : ( <div className={globalStyles["lds-hourglass-relative"]}></div> )
}


export default Loader;
