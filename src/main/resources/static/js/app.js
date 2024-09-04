const url = "http://localhost:8080/odontologos/listar"

const botonListar = document.querySelector("#listar-odontologos")

function listarOdontologos(){

    fetch(`${url}`)
    .then(response => {
    console.log(response)
    return response.json();
    })
    .then(data =>{
    console.log(data)

    renderizarOdontologos(data)
    })
    .catch(error => console.log(error))
}

function renderizarOdontologos(listado){
    const listadoOdontologos = document.querySelector("#odontologos") /
    listadoOdontologos.innerHTML = ""
    let odontologo;
    listado.forEach(function(odontologo){
    console.log(odontologo)
    odontologo = `<li><p>${odontologo.nombre} ${odontologo.apellido} </p></li>`
    listadoOdontologos.innerHTML += odontologo;
})
}

botonListar.addEventListener("click", listarOdontologos)