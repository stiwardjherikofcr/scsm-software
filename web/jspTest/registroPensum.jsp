<%-- 
    Document   : registroPensum
    Created on : 5/06/2021, 02:30:48 PM
    Author     : dunke
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>A</title>
        <script src="../js/JQuery.js"></script>
        <script src="../js/index.js"></script>
    </head>
    <body>
        <h2>Cargar pensum</h2>
        <form action="../ControladorPensum?accion=registrar" method="POST" enctype="multipart/form-data">
            <input type="text" name="programa">
            <input type="file" name="pensum">
            <input type="submit">
        </form>
    </body>
</html>
