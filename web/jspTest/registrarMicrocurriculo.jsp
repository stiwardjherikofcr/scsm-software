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
        <script src="../js/micro.js"></script>
    </head>
    <body>

        <h1>Microcurriculo</h1>
        <%
            dto.Microcurriculo microcurriculo = (dto.Microcurriculo) request.getSession().getAttribute("microcurriculo");
            List<dto.AreaFormacion> areasFormacion = (List<dto.AreaFormacion>) request.getSession().getAttribute("areasFormacion");
            List<dto.TipoAsignatura> tiposAsignatura = (List<dto.TipoAsignatura>) request.getSession().getAttribute("tipoAsignatura");
        %>

        <form action="../ControladorMicrocurriculo" method="POST" accept-charset="ISO-8859-1">
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
                            <%
                                System.out.println(areasFormacion.size());
                                for (dto.AreaFormacion areas : areasFormacion) {


                            %>
                            <div> <%=areas.getNombre()%>
                                <%
                                    System.out.println(microcurriculo.getAreaDeFormacionId().getId() + " == " + areas.getId());
                                    if (microcurriculo.getAreaDeFormacionId().getId().equals(areas.getId())) {
                                %>
                                <input type="radio" name="areasFormacion"  value="<%=areas.getId()%>" checked>
                                <%System.out.println("Entro");
                                } else {
                                %>
                                <input type="radio" name="areasFormacion" value="<%=areas.getId()%>">
                                <%}%> 
                            </div>

                            <%}%>
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
                <a href="consultarMicrocurriculo.jsp"></a>
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

            <input  type="hidden"  name="seccionId-<%=seccion.getSeccionId().getId()%>" value="<%=seccion.getId()%>">  
            <textarea  name="seccion-<%= seccion.getSeccionId().getId()%>" rows="10" cols="50" value="info"><%=seccion.getContenidoList().get(0).getTexto()%></textarea>

            <%   } else {
                int canColum = seccion.getTablaMicrocurriculoList().get(0).getCantidadColumnas();
            %>
            <table  border="1" id="tabla<%=seccion.getSeccionId().getId()%>"   style="width: 100%; border-collapse: collapse">
                <thead>
                    <tr>
                        <% for (int i = 0; i < canColum; i++) {%>
                        <th><%=seccion.getTablaMicrocurriculoList().get(0).getEncabezadoTablaList().get(i).getEncabezadoId().getNombre()%></th>
                            <% }%>
                    </tr>
                <input  type="hidden" name="nfilas-<%=seccion.getId()%>" id="nfilas-<%=seccion.getSeccionId().getId()%>" value="<%=seccion.getTablaMicrocurriculoList().get(0).getCantidadFilas()%>">

                </thead>
                <tbody>

                    <%  List<dto.TablaMicrocurriculoInfo> tablainfo = seccion.getTablaMicrocurriculoList().get(0).getTablaMicrocurriculoInfoList();
                        for (int i = 0; i < seccion.getTablaMicrocurriculoList().get(0).getCantidadFilas(); i++) {
                    %><tr><%
                        for (int j = 0; j < seccion.getTablaMicrocurriculoList().get(0).getCantidadColumnas(); j++) {%>

                        <td> <textarea name="contenido-<%=seccion.getSeccionId().getId()%>-<%=i%>-<%=j%>"><%=tablainfo.size() == 0 ? "" : tablas.get(seccion.getTablaMicrocurriculoList().get(0).getTablaMicrocurriculoPK().getId() - 1)[i][j]%></textarea></td>

                        <%}%>
                    </tr>
                    <% }%>





                </tbody>

            </table>


            <button type="button"  onclick="agregarFila(<%=seccion.getSeccionId().getId()%>)">Agregar Fila</button>
            <button type="button"  onclick="eliminarFila(<%=seccion.getSeccionId().getId()%>)">Eliminar Fila</button>

            <%                }
                }
            %>


            <input type="submit" name="accion" value="Registrar">
        </form>

        <script>




        </script>
    </body>

</html>
