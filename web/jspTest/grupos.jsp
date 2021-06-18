<%-- 
    Document   : grupos
    Created on : 10/06/2021, 07:45:23 PM
    Author     : jhoser
--%>

<%@page import="dto.MateriaPeriodoGrupo"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">      
        <script src="../js/JQuery.js"></script>
        <script src="../js/grupos.js"></script>
        <title>Grupos</title>
    </head>
    <body>
        <h1>Hello Grupos!</h1>

        <div>
            <form action="../ControladorGrupos" method="post">
                <label>Pensum</label>
                <select name="optionPensum" id="optionPensum" onchange="searchMateria()"></select>
                <%dto.Usuario usuario = (dto.Usuario) (request.getSession().getAttribute("usuario"));%>
                <br><br>


                <label>Materia  </label>
                <select name="optionMateria" id="optionMateria">
                </select><br><br>

                <label> Docente  </label>
                <select name="optionDocente" id="optionDocente">
                </select><br>    <br>         

                <label>Grupo </label>
                <input type="text" name="txtGrupo" id="txtGrupo"><br><br>
                <input type="submit" name="action" value="registrarGrupo"> Crear Grupo 
            </form><br><br>
        </div> 


        <div>
            <table>
                <thead>
                    <tr>
                        <th>Codigo Materia</th>
                        <th>Nombre Materia</th>
                        <th>Codigo Docente</th>
                        <th>Grupo</th>
                        <th>Accion</th>
                    </tr>
                </thead>

            </table>
        </div> 
    </body>
</html>
