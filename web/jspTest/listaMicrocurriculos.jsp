<%-- 
    Document   : listaMicrocurriculo
    Created on : 05-jun-2021, 18:38:01
    Author     : Manuel
--%>

<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

    </head>
    <body>

        <a href="../ControladorMicrocurriculo?accion=listarTodos"><button type="button">Actualizar</button></a>
        <table border="1">
            <thead>
                <tr>
                    <th>Pensum</th>
                    <th>Codigo Materia</th>
                    <th>Nombre Materia</th>
                    <th>Creditos</th>
                    <th>Semestre</th>
                    <th>Accion</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<dto.Materia> materias = (List<dto.Materia>) request.getSession().getAttribute("materias");
                    for (dto.Materia elem : materias) {


                %>
                <tr>
                    <td><%=elem.getPensum().getPensumPK().getProgramaCodigo()%> - <%=elem.getPensum().getPensumPK().getCodigo()%></td>
                    <td><%=elem.getMateriaPK().getCodigoMateria()%></td>
                    <td><%=elem.getNombre()%></td>
                    <td><%=elem.getCreditos()%></td>
                    <td><%=elem.getSemestre()%></td>
                    <%
                        if (elem.getMicrocurriculoList().size() > 0) {
                    %>
                    <td> <a href="../ControladorMicrocurriculo?accion=Editar&idMicrocurriculo=<%=elem.getMicrocurriculoList().get(0).getMicrocurriculoPK().getId()%>&codigoMateria=<%=elem.getMicrocurriculoList().get(0).getMateria().getMateriaPK().getCodigoMateria()%>&codigoPensum=<%=elem.getPensum().getPensumPK().getCodigo()%>"><button type="button">Editar </button></a>  
                        <a href="../ControladorMicrocurriculo?accion=Consultar&idMicrocurriculo=<%=elem.getMicrocurriculoList().get(0).getMicrocurriculoPK().getId()%>&codigoMateria=<%=elem.getMicrocurriculoList().get(0).getMateria().getMateriaPK().getCodigoMateria()%>&codigoPensum=<%=elem.getPensum().getPensumPK().getCodigo()%>"><button type="button">Consultar </button></a> 
                        <a href="../ControladorMicrocurriculo?accion=PDF&idMicrocurriculo=<%=elem.getMicrocurriculoList().get(0).getMicrocurriculoPK().getId()%>&codigoMateria=<%=elem.getMicrocurriculoList().get(0).getMateria().getMateriaPK().getCodigoMateria()%>&codigoPensum=<%=elem.getPensum().getPensumPK().getCodigo()%>"><button type="button">PDF</button></a> 

                    </td>
                    <%}%>       
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>

    </body>
</html>
