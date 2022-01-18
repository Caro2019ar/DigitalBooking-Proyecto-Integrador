import jwt from "jsonwebtoken";

function calcularVencimientoTokenEnMilisegundos(token)
{
  const decodedToken = jwt.decode(token, { complete: true });

  // Obtengo el momento actual y el del vencimiento del token en milisegundos desde 01-01-1970
  const milisegundosActuales = Date.now();
  const milisegundosVencimientoToken = decodedToken.payload.exp * 1000;

  // console.log(milisegundosVencimientoToken)
  // console.log(milisegundosActuales)

  return milisegundosVencimientoToken - milisegundosActuales;
}

function isTokenExpired(token)
{
  const decodedToken = jwt.decode(token, { complete: true });

  const milisegundosActuales = Date.now();
  const milisegundosVencimientoToken = decodedToken.payload.exp * 1000;

  return milisegundosActuales >= milisegundosVencimientoToken;
}

export function actualizarEstadoSesion(handleAlert, handleCerrarSesion)
{
  const token = localStorage.getItem("token");

  if (token)
  {
    const tokenParsed = JSON.parse(token);
    if (isTokenExpired(tokenParsed))
      handleCerrarSesion();
    else
    {
      const tiempoRestante = calcularVencimientoTokenEnMilisegundos(tokenParsed);
      const id1 = setTimeout( () => handleAlert(tiempoRestante), tiempoRestante - 60000);
      const id2 = setTimeout(handleCerrarSesion, tiempoRestante);
      return [id1, id2];
    }
  }
}


export default { actualizarEstadoSesion };