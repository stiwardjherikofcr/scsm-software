<%-- 
    Document   : inicioSesion
    Created on : 05-jun-2021, 21:18:08
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
        <form action="../ControladorLogin" method="POST">
            <input type="number" name="codigo" placeholder="Codigo">
            <input type="password" name="clave" placeholder="Contrasena">
            Director
            <input type="radio" name="rol" value="1">
            Docente
            <input type="radio"  name="rol" value="2">
           <input type="submit" name="accion" value="iniciarSesion">  
        </form>
    </body>
</html>
