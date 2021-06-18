<%-- 
    Document   : board
    Created on : 06-jun-2021, 15:00:45
    Author     : Manuel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
  
        
        <%
        request.getSession().removeAttribute("materias");
        %>
        <a href="../ControladorMicrocurriculo?accion=listarTodos"><button type="button">Microcurriculo</button></a><br><br>
        <a href="registroPensum.jsp"><button type="button">Pensum</button></a><br><br>
        
        <a href="registroDocente.jsp"><button type="button">Docentes</button></a><br>
        <a href="../ControladorGrupos?accion=listar"><button type="button">Grupos</button></a><br>
  
    </body>
</html>
