

$(document).ready(function () {
    searchPensums()
    searchDocente();
    searchMateria();
  
});

function searchDocente() {
    console.log("Estoy en searchDocente");
    $.post('../ControladorGrupos?action=optionDocente', {}, function (response) {
        $('#optionDocente').html(response);
    });
}

function searchPensums() {
    $.post('../ControladorPensum?accion=obtenerPensums', {}, function (response) {
        $('#optionPensum').html(response);
      $.post('../ControladorPensum?accion=listarMaterias', {pensumCodigo :$("#optionPensum").val() }, function (response) {
        $('#optionMateria').html(response);
    });
    
    });
    
}

function searchMateria() {
    console.log("Estoy en searchMateria");
    var p = $("#optionPensum").val();
    console.log(p); 
    $.post('../ControladorPensum?accion=listarMaterias', {pensumCodigo : p}, function (response) {
        $('#optionMateria').html(response);
    });
}

