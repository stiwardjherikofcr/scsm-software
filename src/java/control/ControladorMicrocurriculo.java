/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import dto.Pensum;
import dto.Seccion;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import negocio.AdministrarMicrocurriculo;
import negocio.MicrocurriculoPDF;
import negocio.RegistroMicrocurriculoBackground;

/**
 *
 * @author Manuel
 */
@WebServlet(name = "ControladorMicrocurriculo", urlPatterns = {"/ControladorMicrocurriculo"})
public class ControladorMicrocurriculo extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    public static void cargarMicrocurriculo() {
    }

    public static void listar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().removeAttribute("materias");
        AdministrarMicrocurriculo adminMicrocurriculo = new AdministrarMicrocurriculo();
        dto.Usuario usuario = (dto.Usuario) request.getSession().getAttribute("usuario");
        List<dto.Materia> materias = adminMicrocurriculo.obtenerTodasMateria(usuario.getDocente().getProgramaList().get(0).getCodigo());
        request.getSession().setAttribute("areasFormacion", adminMicrocurriculo.obtenerAreasFormacion());
        request.getSession().setAttribute("tipoAsignatura", adminMicrocurriculo.obtenerTiposAisgnatura());
        request.getSession().setAttribute("materias", materias);
        response.sendRedirect("CSM_Software/CSM/director/dashboard/microcurriculo/consultar-microcurriculo.jsp");
    }

    public void editar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("idMicrocurriculo"));
        int codigoPensum = Integer.parseInt(request.getParameter("codigoPensum"));
        int codigoMateria = Integer.parseInt(request.getParameter("codigoMateria"));
        negocio.AdministrarMicrocurriculo adminMicrocurriculo = new negocio.AdministrarMicrocurriculo();
        dto.Microcurriculo microcurriculo = adminMicrocurriculo.obtenerMicrocurriculo(id, codigoMateria, codigoPensum);
        request.getSession().setAttribute("tablas", negocio.AdministrarMicrocurriculo.ordenarTablaInfo(microcurriculo));
        request.getSession().setAttribute("microcurriculo", microcurriculo);
        response.sendRedirect("CSM_Software/CSM/director/dashboard/microcurriculo/editar-microcurriculo.jsp");
    }

    public void consultar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("idMicrocurriculo"));
        int codigoPensum = Integer.parseInt(request.getParameter("codigoPensum"));
        int codigoMateria = Integer.parseInt(request.getParameter("codigoMateria"));
        negocio.AdministrarMicrocurriculo adminMicrocurriculo = new negocio.AdministrarMicrocurriculo();
        dto.Microcurriculo microcurriculo = adminMicrocurriculo.obtenerMicrocurriculo(id, codigoMateria, codigoPensum);
        request.getSession().setAttribute("tablas", negocio.AdministrarMicrocurriculo.ordenarTablaInfo(microcurriculo));
        request.getSession().setAttribute("microcurriculo", microcurriculo);
        response.sendRedirect("CSM_Software/CSM/director/dashboard/microcurriculo/ver-microcurriculo.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion.equalsIgnoreCase("listarTodos")) {
            listar(request, response);
        }

        if (accion.equalsIgnoreCase("editar")) {
            editar(request, response);
        }

        if (accion.equalsIgnoreCase("Consultar")) {
            consultar(request, response);
        }

        if (accion.equalsIgnoreCase("registrar")) {
            try {
                crearMicrocurriculo(request, response);
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        }
        if (accion.equalsIgnoreCase("PDF")) {
            try {
                generarPDF(request, response);
            } catch (DocumentException ex) {
                Logger.getLogger(ControladorMicrocurriculo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void generarPDF(HttpServletRequest request, HttpServletResponse response) throws DocumentException, BadElementException, IOException {
        int codigoPensum = Integer.parseInt(request.getParameter("codigoPensum"));
        int codigoMateria = Integer.parseInt(request.getParameter("codigoMateria"));
        negocio.AdministrarMicrocurriculo admin = new AdministrarMicrocurriculo();
        dto.Microcurriculo microcurriculo = admin.obtenerMicrocurriculo(codigoMateria, codigoPensum);
        negocio.MicrocurriculoPDF pdf = new MicrocurriculoPDF(request.getServletContext().getRealPath("/"), microcurriculo);
        pdf.createPDF();
        response.sendRedirect("CSM_Software/CSM/director/dashboard/microcurriculo/consultar-microcurriculo.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        System.out.println(accion);
        if (accion.equalsIgnoreCase("Registrar")) {
            try {
                registrar(request, response);
            } catch (Exception ex) {
                Logger.getLogger(ControladorMicrocurriculo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void registrarInformacionTablas(HttpServletRequest request, HttpServletResponse response, AdministrarMicrocurriculo adminM) throws Exception {
        dto.Microcurriculo microcurriculo = (dto.Microcurriculo) request.getSession().getAttribute("microcurriculo");
        microcurriculo = adminM.obtenerMicrocurriculoId(microcurriculo.getMicrocurriculoPK().getId());
        for (dto.SeccionMicrocurriculo secciones : microcurriculo.getSeccionMicrocurriculoList()) {
            if (secciones.getSeccionId().getTipoSeccionId().getId() == 2) {
                int cantidadFilas = Integer.parseInt(request.getParameter("nfilas-" + secciones.getId()));
                secciones.getTablaMicrocurriculoList().get(0).setCantidadFilas(cantidadFilas);
                adminM.actualizarFilasTabla(secciones.getTablaMicrocurriculoList().get(0));
                String contenido[][] = new String[cantidadFilas][secciones.getTablaMicrocurriculoList().get(0).getCantidadColumnas()];
                System.out.println("Cantidad Filas=" + cantidadFilas);
                System.out.println("Cantidad Columnas=" + secciones.getTablaMicrocurriculoList().get(0).getCantidadColumnas());
                for (int i = 0; i < contenido.length; i++) {
                    for (int j = 0; j < contenido[i].length; j++) {
                        contenido[i][j] = (String) request.getParameter("contenido-" + secciones.getSeccionId().getId() + "-" + (i) + "-" + j);
                    }
                }
                adminM.registrarContenidoTablas(contenido, secciones);
            }
        }
    }

    public void registrarSecciones(HttpServletRequest request, HttpServletResponse response, AdministrarMicrocurriculo adminM) throws Exception {
        List<dto.Seccion> secciones = adminM.obtenerSecciones();
        response.setContentType("text/html");
        for (Seccion seccione : secciones) {
            if (seccione.getTipoSeccionId().getId() != 2) {
                String informacion = request.getParameter("seccion-" + seccione.getId());
                System.out.println("SECCION : " + "seccion-" + seccione.getId());
                int idSeccionMicrocurriculo = Integer.parseInt(request.getParameter("seccionId-" + seccione.getId()));
                System.out.println("informacion asdasd:" + informacion);
                adminM.ingresarContenidoSecciones(informacion, idSeccionMicrocurriculo);
            }
        }
    }

    private void registrar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        AdministrarMicrocurriculo adminMicrocurriculo = new AdministrarMicrocurriculo();
        int areaFormacion = Integer.parseInt(request.getParameter("areasFormacion"));
        dto.Microcurriculo microcurriculo = (dto.Microcurriculo) request.getSession().getAttribute("microcurriculo");
        adminMicrocurriculo.actualizarAreaFormacionMicrocurriculo(microcurriculo, areaFormacion);
        registrarInformacionTablas(request, response, adminMicrocurriculo);
        registrarSecciones(request, response, adminMicrocurriculo);
        response.sendRedirect("CSM_Software/CSM/director/dashboard/microcurriculo/consultar-microcurriculo.jsp");
    }

    private void crearMicrocurriculo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        new RegistroMicrocurriculoBackground(((Pensum) request.getSession().getAttribute("pensum"))).start();
        response.sendRedirect("ControladorPensum?accion=listarPensum");
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
