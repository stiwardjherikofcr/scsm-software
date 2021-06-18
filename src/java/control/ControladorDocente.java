/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import dao.DocenteJpaController;
import dao.UsuarioJpaController;
import dto.Departamento;
import dto.Docente;
import dto.Rol;
import dto.Usuario;
import dto.UsuarioPK;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.PasswordAuthentication;
import util.Conexion;

/**
 *
 * @author jhoser
 */
@WebServlet(name = "ControladorDocente", urlPatterns = {"/ControladorDocente"})
public class ControladorDocente extends HttpServlet {

    EntityManagerFactory em = Conexion.getConexion().getBd();
    DocenteJpaController docenteDao = new DocenteJpaController(em);
    UsuarioJpaController usuarioDao = new UsuarioJpaController(em);

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ControladorDocente</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ControladorDocente at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        try {
            switch (request.getParameter("action")) {
                case "editarDocente":
                    this.editarDocente(request, response);
                    break;
                case "listarDocente":
                    this.listarDocente(request, response);
                    break;
            }
            pw.println("<h1>Hizo algo</h1>");
        } catch (Exception e) {
            System.out.println("estoy editando");
            pw.println("<h1>Error</h1>");
            e.printStackTrace();
            System.err.println(e);
        }
        pw.flush();
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        try {
            switch (request.getParameter("action")) {
                case "registrarDocente":
                    this.guardarDocente(request, response);
                    break;
                case "listarDocente":
                    this.listarDocente(request, response);
                    break;
                case "activarDocente":
                    this.activarDocente(request, response);
                    break;
            }
            pw.println("<h1>Hizo algo</h1>");

        } catch (Exception e) {
            System.out.println("estoy editando");
            pw.println("<h1>Error</h1>");
            e.printStackTrace();
            System.err.println(e);
        }
        pw.flush();
    }

    public void activarDocente(HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        System.out.println("ACTIVAR DOCENTE");
        String[] inactivos = request.getParameterValues("activarDocente");
        for (String s : inactivos) {
            String[] spliteado = s.split("-");
            Docente d = docenteDao.findDocente(Integer.parseInt(spliteado[1]));
            short in;
            in = (short) ((d.getEstado() == 1) ? 0 : 1);
            d.setEstado(in);
            docenteDao.edit(d);
        }
        listarDocente(request, response);

    }

    public void guardarDocente(HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        Docente docente = new Docente();
        PasswordAuthentication encriptarPass = new PasswordAuthentication();
        //lectura de datos
        int codigo = Integer.parseInt(request.getParameter("txtCodigo"));
        String nombre = request.getParameter("txtNombre");
        String apellido = request.getParameter("txtApellido");
        int departamento = Integer.parseInt(request.getParameter("optionDepartamento"));
        short estado = 1;
        String password = request.getParameter("txtPassword");
        //Creacion del docente
        docente.setNombre(nombre);
        docente.setApellido(apellido);
        docente.setDepartamentoId(new Departamento(departamento));
        docente.setCodigoDocente(codigo);
        docente.setEstado(estado);
        //Creacion del usuario
        password = encriptarPass.hash(password.toCharArray()); //encriptando password
        UsuarioPK upk = new UsuarioPK(0, codigo);
        Usuario usuario = new Usuario(upk, password);
        usuario.setRol(new Rol(2));
        usuario.setDocente(docente);
        upk.setDocenteCodigo(codigo);
        docenteDao.create(docente); //docente creado 
        usuarioDao.create(usuario); //usuario creado
        response.sendRedirect("jspTest/registroDocente.jsp");
    }

    public void listarDocente(HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        System.out.println("Listando docentes");
        List<Docente> docentes = (List<Docente>) docenteDao.findDocenteEntities();
        request.getSession().setAttribute("listaDocentes", docentes);
        response.sendRedirect("CSM_Software/CSM/director/dashboard/docentes.jsp");
    }

    public void editarDocente(HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        System.out.println("Estoy en editar");
        PrintWriter pw = response.getWriter();
        pw.println("<h1>Error</h1>");
        response.sendRedirect("jspTest/listaDocente.jsp");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
