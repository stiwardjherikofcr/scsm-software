<%-- 
    Document   : listaDocente
    Created on : 6/06/2021, 01:52:47 PM
    Author     : jhoser
--%>

<%@page import="util.Conexion"%>
<%@page import="dao.DocenteJpaController"%>
<%@page import="dto.Docente"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lista Docente</title>
        <style>/* The switch - the box around the slider */
            .switch {
                position: relative;
                display: inline-block;
                width: 30px;
                height: 14px;
            }

            /* Hide default HTML checkbox */
            .switch input {
                opacity: 0;
                width: 0;
                height: 0;
            }

            /* The slider */
            .slider {
                position: absolute;
                cursor: pointer;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background-color: #ccc;
                -webkit-transition: .4s;
                transition: .4s;
            }

            .slider:before {
                position: absolute;
                content: "";
                height: 14px;
                width: 16px;
                left: 0px;
                bottom: 0px;
                background-color: white;
                -webkit-transition: .4s;
                transition: .4s;
            }

            input:checked + .slider {
                background-color: #2196F3;
            }

            input:focus + .slider {
                box-shadow: 0 0 1px #2196F3;
            }

            input:checked + .slider:before {
                -webkit-transform: translateX(14px);
                -ms-transform: translateX(14px);
                transform: translateX(14px);
            }

            /* Rounded sliders */
            .slider.round {
                border-radius: 34px;
            }

            .slider.round:before {
                border-radius: 50%;
            }
        </style>
    </head>
    <body>
        <table>
            <thead>
                <tr>
                    <th>Codigo</th>
                    <th>Nombre</th>
                    <th>Apellido</th>
                    <th>Departamento</th>
                    <th>Estado</th>
                    <th>Accion</th>
                    <th>Activo</th>
                </tr>
            </thead>

            <%
                List<Docente> docentes = (List<Docente>) request.getSession().getAttribute("listaDocentes");
                System.out.println(docentes.size());
                for (Docente teacher : docentes) {
            %>
            <tbody>
                <tr>
                    <td ><%= teacher.getCodigoDocente()%></td>
                    <td ><%= teacher.getNombre()%></td>
                    <td ><%= teacher.getApellido()%></td>
                    <td ><%= teacher.getDepartamentoId().getNombreDepartamento()%></td>
                    <td ><%= teacher.getEstado()%></td>   
                    <td><a href="../ControladorDocente?action=editarDocente" method="post"><button type="button" value="editarDocente" name="action">Editar</button></a></td>

                    <td> 
                        <form name="fActivar" method="post" action="../ControladorDocente?action=activarDocente"> 
                            <!-- Rounded switch -->

                            <label class="switch">
                                <input type="checkbox" name="activarDocente"  value="activarDocente-<%=teacher.getCodigoDocente()%>" id="activarDocente-<%=teacher.getCodigoDocente()%>" onchange="validarCheck(<%=teacher.getCodigoDocente()%>)">
                                <span class="slider round"></span>
                            </label>
                            <%}%>  
                            <input type="submit">
                        </form>
                    </td>

                </tr>
            </tbody>

        </table>
      
    </body>
    <script>
        function validarCheck(codigo) {
            console.log(document.getElementById("activarDocente-" + codigo).checked);
        }
    </script>
</html>
