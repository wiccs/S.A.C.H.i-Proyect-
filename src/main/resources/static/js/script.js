/*document.getElementById("activarSensor").addEventListener("click", function(event) {
    event.preventDefault(); // Evita que se recargue la página si el botón está en un formulario

    let boton = this; // Guardamos referencia al botón
    let spinner = document.getElementById("spinner");

    // Ocultar botón y mostrar animación
    boton.style.display = "none";
    spinner.style.display = "flex";
});
*/



/*document.getElementById("activarSensor").addEventListener("click", function(event) {
    event.preventDefault();

    let boton = this;
    let spinner = document.getElementById("spinner");
    let successCheck = document.getElementById("successCheck");

    // Ocultar botón y mostrar animación de carga
    boton.style.display = "none";
    spinner.style.display = "flex";

    // Enviar solicitud al ESP32
    fetch("http://192.168.170.205/R503?estado=Encendido")  //  Usa la IP real del ESP32
        .then(response => response.text())
        .then(data => {
            console.log("Respuesta del ESP32:", data);  //  Verifica la respuesta real

            if (data.includes("Huella Registrda!")) { //  Verifica la respuesta correcta
                spinner.style.display = "none";
                successCheck.style.display = "block";
            } else {
                alert("No se detectó la huella. Intenta de nuevo.");
                boton.style.display = "block";  // Mostrar el botón nuevamente
                spinner.style.display = "none";
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("Error al comunicarse con el ESP32");
            boton.style.display = "block";  // Mostrar el botón nuevamente
            spinner.style.display = "none";
        });
});
*/

/*document.getElementById("activarSensor").addEventListener("click", function(event) {
    event.preventDefault();

    let boton = this;
    let spinner = document.getElementById("spinner");
    let successCheck = document.getElementById("successCheck");

    // Ocultar botón y mostrar animación de carga
    boton.style.display = "none";
    spinner.style.display = "flex";

    // 1. Activamos el ESP32
    fetch("http://192.168.170.205/R503?estado=Encendido")
        .then(response => response.text())
        .then(data => {
            console.log("Respuesta del ESP32:", data);


        })
        .catch(error => {
            console.error("Error:", error);
            alert("Error al comunicarse con el ESP32");
            boton.style.display = "block";
            spinner.style.display = "none";
        });
});*/

document.getElementById("activarSensor").addEventListener("click", function(event) {
    event.preventDefault();

    let boton = this;
    let spinner = document.getElementById("spinner");
    let successCheck = document.getElementById("successCheck");

    // Ocultar botón y mostrar animación de carga
    boton.style.display = "none";
    spinner.style.display = "flex";
    });







