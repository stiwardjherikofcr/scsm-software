/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function (){
    searchFacultad();
});

function searchFacultad(){
    $.post('../ControladorFacultad?accion=listar', {}, function(response){
        $('#optionFacultad').html(response);
        $('#optionFacultad').val(1);
        searchDepartamento();
    });
}

function searchDepartamento(){
    f = $('#optionFacultad').val();
    $.post('../ControladorDepartamento?accion=listar', {query:f}, function(response){
        $('#optionDepartamento').html(response);
    });
}

//Generalizacion fallida xd
function search(controlador, params, target_component){
    $.post(controlador+'?accion=listar', params, function(response){
        $(target_component).html(response);
    });
}