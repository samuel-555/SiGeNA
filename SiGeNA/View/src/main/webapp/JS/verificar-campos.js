document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");

    form.addEventListener("submit", function (event) {
        const obrigatorios = document.querySelectorAll(".obrigatorio");
        let valido = true;

        obrigatorios.forEach(campo => {
            let valor = campo.value;

            if (valor === null || valor === undefined || valor.trim() === "") {
                valido = false;
                campo.focus();
                return;
            }
        });

        if (!valido) {
            event.preventDefault();
            window.alert("Por favor, preencha todos os campos obrigatórios marcados com *.");
        }
    });
});
