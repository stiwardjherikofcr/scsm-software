/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import dto.Usuario;
import dto.UsuarioPK;
import util.Conexion;
import util.PasswordAuthentication;

/**
 *
 * @author Manuel
 */
public class Login {

    public Login() {
    }

    public boolean validarUsuario(int codigo, String contrasena, int rol) {
        Conexion con = Conexion.getConexion();
        dao.UsuarioJpaController daoUsuario = new dao.UsuarioJpaController(con.getBd());
        dto.UsuarioPK usuarioPk = new dto.UsuarioPK(rol, codigo);
        dto.Usuario usuario = daoUsuario.findUsuario(usuarioPk);
        System.out.println(usuario);
        if (usuario != null && autenticarContra(contrasena, codigo, rol) && usuario.getDocente().getEstado() == 1) {
            return true;
        } else {
            return false;
        }
    }

    public dto.Usuario obtenerUsuario(int codigo, int rol) {
        Conexion con = Conexion.getConexion();
        dao.UsuarioJpaController daoUsuario = new dao.UsuarioJpaController(con.getBd());
        dto.UsuarioPK usuarioPk = new dto.UsuarioPK(rol, codigo);
        dto.Usuario usuario = daoUsuario.findUsuario(usuarioPk);
        return usuario;
    }

    public dto.Docente obtenerDocente(int codigo) {
        Conexion con = Conexion.getConexion();
        dao.DocenteJpaController daoDocente = new dao.DocenteJpaController(con.getBd());
        return daoDocente.findDocente(codigo);
    }

    public boolean autenticarContra(String password, int codigo, int rol) {
        PasswordAuthentication encriptarPass = new PasswordAuthentication();
        Conexion con = Conexion.getConexion();
        dao.DocenteJpaController daoDocente = new dao.DocenteJpaController(con.getBd());
        dao.UsuarioJpaController daoUsuario = new dao.UsuarioJpaController(con.getBd());
        Usuario u = daoUsuario.findUsuario(new UsuarioPK(rol, codigo));
        return encriptarPass.authenticate(password, u.getClave());
    }
}
