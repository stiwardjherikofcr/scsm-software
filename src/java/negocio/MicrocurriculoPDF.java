/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dao.AreaFormacionJpaController;
import dao.TipoAsignaturaJpaController;
import dto.AreaFormacion;
import dto.EncabezadoTabla;
import dto.Materia;
import dto.Microcurriculo;
import dto.PrerrequisitoMateria;
import dto.SeccionMicrocurriculo;
import dto.TablaMicrocurriculo;
import dto.TablaMicrocurriculoInfo;
import dto.TipoAsignatura;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import util.Conexion;

/**
 *
 * @author dunke
 */
public class MicrocurriculoPDF {

    private BaseFont bf;
    private Microcurriculo m;
    private List<AreaFormacion> areas;
    private List<TipoAsignatura> ta;
    private Document doc;
    private String path;

    public MicrocurriculoPDF(String path, Microcurriculo m) throws FileNotFoundException, DocumentException, BadElementException, IOException {
        EntityManagerFactory em = Conexion.getConexion().getBd();
        this.m = m;
        this.path = path;
        this.path = new File(this.path).getParentFile().getParentFile().getAbsolutePath();
        this.ta = new TipoAsignaturaJpaController(em).findTipoAsignaturaEntities();
        this.areas = new AreaFormacionJpaController(em).findAreaFormacionEntities();
        this.bf = BaseFont.createFont(this.path + "\\fonts\\Roboto-Medium.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        TableHeader th = new TableHeader(this.getTablaEnca());
        this.doc = new Document(PageSize.A4, 36, 36, 20 + th.getTableHeight(), 36);
        PdfWriter.getInstance(this.doc, new FileOutputStream(this.path + "\\temp\\MICROCURRICULO_" + m.getMateria().getNombre() + "_" + m.getMateria().getMateriaPK().getCodigoMateria() + ".pdf")).setPageEvent(th);
    }

    public void createPDF() throws DocumentException, BadElementException, IOException {
        this.doc.open();
        this.doc.add(this.getParapgraph("Microcurriculo", 10, Paragraph.ALIGN_CENTER));
        this.createBlank();
        this.doc.add(this.getTablaInfo());
        this.createBlank();
        for (SeccionMicrocurriculo sm : this.m.getSeccionMicrocurriculoList()) {
            this.doc.add(getParapgraph(sm.getSeccionId().getNombre(), 11, Paragraph.ALIGN_CENTER));
            this.createBlank();
            if (sm.getSeccionId().getTipoSeccionId().getId() == 2) {
                this.doc.add(this.getTabla(sm.getTablaMicrocurriculoList().get(0)));
            } else {
                this.doc.add(getParapgraph(sm.getContenidoList().get(0).getTexto(), 9, Paragraph.ALIGN_JUSTIFIED));
            }
            this.createBlank();
        }
        this.doc.close();
    }

    private PdfPTable getTabla(TablaMicrocurriculo tm) throws DocumentException {
        PdfPTable tab = new PdfPTable(tm.getCantidadColumnas());
        if (tm.getTablaMicrocurriculoPK().getId() == 1) {
            tab.setWidths(new int[]{1, 3, 1, 1, 1});
        }
        tab.setWidthPercentage(100);
        this.configEnca(tab, tm);
        String[][] infoOrder = this.getOrder(tm.getTablaMicrocurriculoInfoList(), tm.getCantidadFilas(), tm.getCantidadColumnas());
        for (String x[] : infoOrder) {
            for (String y : x) {
                tab.addCell(getParapgraph(y, 9, Paragraph.ALIGN_CENTER));
            }
        }
        return tab;
    }

    private String[][] getOrder(List<TablaMicrocurriculoInfo> tmis, int row, int col) {
        String[][] info = new String[row][col];
        for (TablaMicrocurriculoInfo tmi : tmis) {
            info[tmi.getTablaMicrocurriculoInfoPK().getIdFila()][tmi.getTablaMicrocurriculoInfoPK().getIdColumna()] = tmi.getContenidoId().getTexto();
        }
        return info;
    }

    private void configEnca(PdfPTable tab, TablaMicrocurriculo tm) {
        int i = 0;
        for (EncabezadoTabla et : tm.getEncabezadoTablaList()) {
            if (i++ == 2 && tm.getTablaMicrocurriculoPK().getId() == 1) {
                tab.getDefaultCell().setColspan(2);
                tab.addCell(this.getParapgraph("Dedicacion del estudiante (Horas)", 9, Paragraph.ALIGN_CENTER));
                tab.getDefaultCell().setColspan(1);
                tab.getDefaultCell().setRowspan(2);
                tab.addCell(this.getParapgraph(tm.getEncabezadoTablaList().get(++i).getEncabezadoId().getNombre(), 9, Paragraph.ALIGN_CENTER));
                tab.getDefaultCell().setRowspan(1);

                tab.addCell(this.getParapgraph("a)Trabajo Presencial", 9, Paragraph.ALIGN_CENTER));
                tab.addCell(this.getParapgraph("b)Trabajo Independiente", 9, Paragraph.ALIGN_CENTER));
                break;
            } else {
                if (tm.getTablaMicrocurriculoPK().getId() == 1) {
                    tab.getDefaultCell().setRowspan(2);
                }
                tab.addCell(this.getParapgraph(et.getEncabezadoId().getNombre(), 9, Paragraph.ALIGN_CENTER));
                tab.getDefaultCell().setRowspan(1);
            }
        }
    }

    public PdfPTable getTablaEnca() throws DocumentException, BadElementException, IOException {
        PdfPTable tab = new PdfPTable(3);
        tab.setWidths(new int[]{1, 4, 1});
        tab.getDefaultCell().setRowspan(3);
        tab.addCell(Image.getInstance(this.path + "\\imgs\\logoufps.png"));
        tab.getDefaultCell().setRowspan(2);
        PdfPCell c = new PdfPCell(this.getParapgraph(
                "UNIVERSIDAD FRANCISCO DE PAULA SANTANDER "
                + "FACULTAD DE " + this.m.getMateria().getPensum().getPrograma().getDepartamentoId().getFacultadId().getNombre().toUpperCase() + " "
                + "PROGRAMA " + this.m.getMateria().getPensum().getPrograma().getNombrePrograma().toUpperCase(),
                11, Paragraph.ALIGN_CENTER));
        c.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        c.setVerticalAlignment(Paragraph.ALIGN_MIDDLE);
        c.setFixedHeight(70);
        tab.addCell(c);
        tab.getDefaultCell().setRowspan(3);
        tab.addCell(Image.getInstance(this.m.getMateria().getPensum().getPrograma().getImgPrograma()));
        tab.getDefaultCell().setRowspan(1);
        PdfPCell f = new PdfPCell(this.getParapgraph("Formato Syllabus", 11, Paragraph.ALIGN_CENTER));
        f.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        f.setVerticalAlignment(Paragraph.ALIGN_BOTTOM);
        tab.addCell(f);
        return tab;
    }

    public PdfPTable getTablaInfo() throws DocumentException, BadElementException, IOException {
        Materia ma = this.m.getMateria();
        PdfPTable tab = new PdfPTable(5);
        tab.setWidths(new int[]{1, 1, 1, 1, 1});
        tab.addCell(this.getParapgraph("Asignatura", 10, Paragraph.ALIGN_LEFT));
        tab.getDefaultCell().setColspan(4);
        tab.addCell(this.getParapgraph(ma.getNombre(), 10, Paragraph.ALIGN_LEFT));
        tab.getDefaultCell().setColspan(1);
        tab.addCell(this.getParapgraph("Código", 10, Paragraph.ALIGN_LEFT));
        tab.getDefaultCell().setColspan(4);
        tab.addCell(this.getParapgraph("" + ma.getMateriaPK().getCodigoMateria(), 10, Paragraph.ALIGN_LEFT));
        tab.getDefaultCell().setColspan(1);
        tab.addCell(this.getParapgraph("Área de formación:", 10, Paragraph.ALIGN_LEFT));
        for (AreaFormacion a : this.areas) {
            Phrase p = new Phrase(a.getNombre());
            if (this.m.getAreaDeFormacionId().getId().equals(a.getId())) {
                p.add(this.getParapgraph(": X", 10, Paragraph.ALIGN_LEFT));
            }
            tab.addCell(p);
        }
        tab.addCell(this.getParapgraph("Tipo de asignatura:", 10, Paragraph.ALIGN_LEFT));
        tab.getDefaultCell().setColspan(2);
        for (TipoAsignatura t : this.ta) {
            Phrase p = new Phrase(t.getTipo());
            if (this.m.getAreaDeFormacionId().getId().equals(t.getId())) {
                p.add(this.getParapgraph(": X", 10, Paragraph.ALIGN_LEFT));
            }
            tab.addCell(p);
        }
        tab.getDefaultCell().setColspan(1);
        tab.addCell(this.getParapgraph("Número de Créditos", 10, Paragraph.ALIGN_LEFT));
        tab.getDefaultCell().setColspan(4);
        tab.addCell(this.getParapgraph("" + ma.getCreditos(), 10, Paragraph.ALIGN_LEFT));
        tab.getDefaultCell().setColspan(1);
        tab.addCell(this.getParapgraph("Prerrequisitos", 10, Paragraph.ALIGN_LEFT));
        tab.getDefaultCell().setColspan(4);
        String pr = "";
        for (PrerrequisitoMateria pm : ma.getPrerrequisitoMateriaList()) {
            pr += pm.getMateria1().getMateriaPK().getCodigoMateria() + " - " + pm.getMateria1().getNombre();
        }
        tab.addCell(this.getParapgraph(pr, 10, Paragraph.ALIGN_LEFT));
        tab.setWidthPercentage(100);
        return tab;
    }

    public Paragraph getParapgraph(String e, int tam, int orien) {
        Paragraph p = new Paragraph(e, new Font(this.bf, tam));
        p.setAlignment(orien);
        return p;
    }

    public void createBlank() throws DocumentException {
        this.doc.add(getParapgraph("\n", 9, Paragraph.ALIGN_CENTER));
    }
}
