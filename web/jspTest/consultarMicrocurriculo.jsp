    <%-- 
    Document   : consultarMicrocurriculo
    Created on : 08-jun-2021, 3:59:40
    Author     : Manuel
--%>

<%@page import="java.util.ArrayList"%>
<%-- 
    Document   : registrarMicrocurriculo
    Created on : 05-jun-2021, 11:51:37
    Author     : Manuel
--%>

<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>JSP Page</title>
        <script src="../js/JQuery.js"></script>
        <script src="../js/microcurriculo.js"></script>
    </head>
    <body>
        <h1>Microcurriculo</h1>
        <%
            dto.Microcurriculo microcurriculo = (dto.Microcurriculo) request.getSession().getAttribute("microcurriculo");
            List<dto.AreaFormacion> areasFormacion = (List<dto.AreaFormacion>) request.getSession().getAttribute("areasFormacion");
            List<dto.TipoAsignatura> tiposAsignatura = (List<dto.TipoAsignatura>) request.getSession().getAttribute("tipoAsignatura");
        %>
     
            <input type="hidden"  name="microcurriculoId"  value=<%=microcurriculo.getMicrocurriculoPK().getId()%>>
            <table border="1">
                <thead>
                    <tr>
                        <th>Asignatura</th>
                        <th><%=microcurriculo.getMateria().getNombre()%></th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Codigo</td>
                        <td><%=microcurriculo.getMateria().getMateriaPK().getCodigoMateria()%></td>
                    </tr>
                    <tr>
                        <td>Area de Formacion</td>
                        <td>
                            <%=microcurriculo.getAreaDeFormacionId().getNombre()%>
                        </td>
                    </tr>
                    <tr>
                        <td>Tipo Asignatura</td>
                        <td>
                            <%=microcurriculo.getMateria().getTipoAsignaturaId().getTipo()%>
                        </td>
                    </tr>
                    <tr>
                        <td> Numero de creditos : </td>
                        <td> <%=microcurriculo.getMateria().getCreditos()%></td>
                    </tr>
                    <tr>
                        <td>Prerrequisitos</td>
                        <td><%
                            for (dto.PrerrequisitoMateria prerrequisito : microcurriculo.getMateria().getPrerrequisitoMateriaList()) {

                            %>

                            <%=prerrequisito.getMateria1().getNombre()%>  <br><%}%>
                        </td>

                    </tr>

                    <tr>
                        <td>Correquisitos</td>
                        <td></td>
                    </tr>

                </tbody>
            </table>

 <%
                List<String[][]> tablas = (List<String[][]>) request.getSession().getAttribute("tablas");
                List<dto.SeccionMicrocurriculo> secciones = microcurriculo.getSeccionMicrocurriculoList();
                for (dto.SeccionMicrocurriculo seccion : secciones) {
             %>

            <%=seccion.getSeccionId().getNombre()%>  <br>
           
            <% int tipo = seccion.getSeccionId().getTipoSeccionId().getId();
                if (tipo == 1) {%>
         
          <p ><%=seccion.getContenidoList().get(0).getTexto()%></p>

            <%   } else {
                int canColum = seccion.getTablaMicrocurriculoList().get(0).getCantidadColumnas();
            %>
            <table  border="1" id="tabla<%=seccion.getSeccionId().getId()%>"   style="width: 100%; border-collapse: collapse">
                <thead><tr>
                        <% for (int i = 0; i < canColum; i++) {%>
                        <th><%=seccion.getTablaMicrocurriculoList().get(0).getEncabezadoTablaList().get(i).getEncabezadoId().getNombre()%></th>
                            <% }%>
                    </tr>
       
                </thead>
                <tbody>

                    <%  List<dto.TablaMicrocurriculoInfo> tablainfo = seccion.getTablaMicrocurriculoList().get(0).getTablaMicrocurriculoInfoList();
                        int con = 0;
                        for (int i = 0; i < seccion.getTablaMicrocurriculoList().get(0).getCantidadFilas(); i++) {
                    %><tr><%
                        for (int j = 0; j < seccion.getTablaMicrocurriculoList().get(0).getCantidadColumnas(); j++) {%>

                        <td> <%=tablainfo.size() == 0 ? "" : tablas.get(seccion.getTablaMicrocurriculoList().get(0).getTablaMicrocurriculoPK().getId() - 1)[i][j]%></td>

                        <% con++;
                            }
                        %>
                    </tr>
                    <% }} }  %>





                </tbody>

            </table>




    </body>

</html>
