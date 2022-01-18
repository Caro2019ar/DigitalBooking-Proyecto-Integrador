pm.test("El método es de tipo DELETE", () => {
    pm.expect(request.method).eql("DELETE");
});

pm.test("La URL de la petición es la esperada", () => {
    pm.expect(request.url).to.have.string("http://localhost:8080/productos/");
});

pm.test("Existe la propiedad Content-Type en el encabezado de la respuesta y es igual a 'application/json'", () => {
    pm.response.to.have.header("Content-Type");
    pm.expect(pm.response.headers.get('Content-Type')).to.eql('application/json');
});

pm.test("Tiempo de respuesta menor a 200 ms", () => {
    pm.expect(pm.response.responseTime).to.be.below(200);
});

pm.test("Status igual a '200 OK' o '404 Not Found'", function () {
    pm.expect(pm.response.code).to.be.oneOf([200, 400, 404]);
    if (pm.response.code == 200)
        pm.response.to.be.ok;
    else if (pm.response.code == 404)
        pm.response.to.have.status("Not Found");
    else if (pm.response.code == 400)
        pm.response.to.have.status("Bad Request");
});

pm.test("Body de la petición vacío", function () {
    pm.expect(request.data).to.be.empty;
});

pm.test("La respuesta es correcta/errónea, tiene body y es un JSON", function () {
    if (pm.response.code == 200)
        pm.response.to.not.be.error;
    else if (pm.response.code == 404 || pm.response.code == 400)
        pm.response.to.be.error;
        pm.response.to.be.withBody;
        pm.response.to.be.json;
});

const dataRes = pm.response.json();

pm.test("Body de la respuesta no vacío", function () {
    pm.expect(dataRes).to.not.be.empty;
});

pm.test("Respuesta con los atributos esperados", function () {
    if (pm.response.code == 200)
        pm.expect(dataRes).to.have.all.keys("message");
    else if (pm.response.code == 404 || pm.response.code == 400)
        pm.expect(dataRes).to.have.all.keys(["timestamp", "status", "error"]);
});

pm.test("Tipos de datos de la respuesta", () => {
    pm.expect(dataRes).to.be.an("object");
    if (pm.response.code == 200) {
        pm.expect(dataRes.message).to.be.a("string");
    }
    else if (pm.response.code == 404 || pm.response.code == 400) {
        pm.expect(dataRes.timestamp).to.be.a("string");
        pm.expect(dataRes.status).to.be.a("number");
        pm.expect(dataRes.error).to.be.a("string");
    }
});

pm.test("Mensaje con texto 'eliminada correctamente'", function () {
    if (pm.response.code == 200)
        pm.expect(dataRes.message).to.have.string('eliminado correctamente');
});

pm.test("Status con código 404 o 400 en caso de error", function () {
    if (pm.response.code == 404)
        pm.expect(dataRes.status).to.eql(404);
    if (pm.response.code == 400)
        pm.expect(dataRes.status).to.eql(400);
});

pm.test("Error con texto 'no existe' o 'productos asociados'", function () {
    if (pm.response.code == 404)
        pm.expect(dataRes.error).to.have.string('no existe');
    if (pm.response.code == 400)
        pm.expect(dataRes.error).to.have.string('productos asociados');
});