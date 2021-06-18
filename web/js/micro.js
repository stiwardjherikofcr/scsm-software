/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



function agregarFila(seccionId) {
    var rta = "<tr>";
    var tabla = document.getElementById("tabla" + seccionId);
    var nColumnas = $("#tabla" + seccionId + " tr:last th").length;
    var nColumnas2 = $("#tabla" + seccionId + " tr:last td").length;
    var filas = tabla.rows.length;
    console.log(filas);
    console.log(seccionId);
    var max = Math.max(nColumnas, nColumnas2);
    for (var i = 0; i < max; i++) {
        rta += `<td><textarea style="border:none;" name="contenido-${seccionId }-${filas - 1}-${i}"></textarea></td>`;
    }


    rta += "</tr>"

    tabla.insertRow(-1).innerHTML = rta;
    document.getElementById("nfilas-" + seccionId).value = tabla.rows.length - 1

}

function eliminarFila(seccionId) {
    var table = document.getElementById("tabla" + seccionId);
    var rowCount = table.rows.length;
    //console.log(rowCount);
    document.getElementById("nfilas-" + seccionId).value = rowCount - 2
    if (rowCount <= 1)
        alert('No se puede eliminar el encabezado');
    else
        table.deleteRow(rowCount - 1);
}
     