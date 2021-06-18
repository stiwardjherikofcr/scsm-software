<%-- 
    Document   : registroDocente
    Created on : 5/06/2021, 10:44:35 PM
    Author     : jhoser
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registrar docente</title>      
        <script src="../js/JQuery.js"></script>
        <script src="../js/index.js"></script>
    </head>
    <body>
        <h1>REGISTRO DOCENTE</h1>
        <!--  nombre, apellido, codigo, correo, contraseña, confirmar contraseña, facultad[], departamento[], tipo[] BUTON iniciar sesion, registrar -->
        <form method="post" action="../ControladorDocente">
            <table>
                <tr> 
                    <td><input type="text" name="txtNombre" required>Nombre</td>
                    <td><input type="text" name="txtApellido" required>Apellido</td>
                </tr>
                <tr>
                    <td><input type="number" name="txtCodigo" required>Codigo</td>
                    <td><input type="email" name="txtCorreo" required>Correo</td>
                </tr>
                <tr>
                    <td><input type="password" name="txtPassword" required>Contraseña</td>
                    <td><input type="password" name="txtPassword" required>Confirmar contraseña</td>
                </tr>
                <tr>
                    <td>    
                        <select name="optionFacultad" id="optionFacultad" onchange ="searchDepartamento()" required>
                        </select>
                    </td>
                    <td>
                        <select name="optionDepartamento" id="optionDepartamento" required>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input type="submit"name="action" value="registrarDocente">Registrarse     
                      
                    </td>
                </tr>
            </table> 
        </form>
           <form method="post" action="../ControladorDocente">
             <input type="submit"name="action" value="listarDocente" > Docentes adscritos
            
           </form>
    
    </body>
</html>
