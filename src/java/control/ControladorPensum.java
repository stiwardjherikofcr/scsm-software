/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import dto.Pensum;
import dto.Usuario;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import negocio.AdministrarPensum;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author dunke
 */
@WebServlet(name = "ControladorPensum", urlPatterns = {"/ControladorPensum"})
public class ControladorPensum extends HttpServlet {

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
        switch (request.getParameter("accion")) {
            case "listarPensum":
                this.listarPensum2(request, response);
        }
    }

    public void listarPensum2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AdministrarPensum admin = new AdministrarPensum();
        Usuario u = (Usuario) request.getSession().getAttribute("usuario");
        List<Pensum> pensum = admin.obtenerPensum(u.getDocente().getProgramaList().get(0));
        request.getSession().setAttribute("listaPensum2", pensum);
        response.sendRedirect("CSM_Software/CSM/director/dashboard/pensum.jsp");
    }

    private void registrar(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Integer id_programa = 0;
            InputStream pensumFile = null;
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            for (FileItem item : items) {
                if (item.isFormField()) {
                    switch (item.getFieldName()) {
                        case "programa":
                            id_programa = Integer.parseInt(item.getString());
                    }
                } else {
                    pensumFile = item.getInputStream();
                }
            }
            AdministrarPensum ap = new AdministrarPensum(request.getServletContext().getRealPath("/"));
            request.getSession().setAttribute("pensum", ap.registrar(id_programa, pensumFile));
            response.sendRedirect("ControladorMicrocurriculo?accion=registrar");
        } catch (FileUploadException e) {
            throw new ServletException("Cannot parse multipart request.", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        switch (request.getParameter("accion")) {
            case "registrar":
                this.registrar(request, response);
        }
        if (request.getParameter("accion").equalsIgnoreCase("obtenerPensums")) {
            listarPensums(request, response);
        }
        if (request.getParameter("accion").equalsIgnoreCase("listarMaterias")) {
            listarMaterias(request, response);
        }
    }

    private void listarMaterias(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pw = new PrintWriter(response.getOutputStream());
        negocio.AdministrarPensum admin = new AdministrarPensum();
        dto.Programa programa = (dto.Programa) request.getSession().getAttribute("programaSesion");
        int pensumCodigo = Integer.parseInt(request.getParameter("pensumCodigo"));
        System.out.println("DIGITO " + pensumCodigo);
        List<dto.Materia> materias = admin.obtenerMateriasPensum(pensumCodigo, programa.getCodigo());
        for (dto.Materia m : materias) {
            pw.println("<option value=" + m.getMateriaPK().getCodigoMateria() + ">" + m.getMateriaPK().getCodigoMateria() + "-" + m.getNombre() + "</option>");
        }
        pw.flush();
    }

    private void listarPensums(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pw = new PrintWriter(response.getOutputStream());
        negocio.AdministrarPensum admin = new AdministrarPensum();
        dto.Programa programa = (dto.Programa) request.getSession().getAttribute("programaSesion");
        List<dto.Pensum> pensums = admin.obtenerPensum(programa);
        for (dto.Pensum p : pensums) {
            pw.println("<option value=" + p.getPensumPK().getCodigo() + ">" + p.getPrograma().getCodigo() + "-" + p.getPensumPK().getCodigo() + "</option>");
        }
        pw.flush();
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
