/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import dao.ContenidoJpaController;
import dao.EncabezadoTablaJpaController;
import dao.MicrocurriculoJpaController;
import dao.SeccionJpaController;
import dao.SeccionMicrocurriculoJpaController;
import dao.TablaMicrocurriculoJpaController;
import dao.exceptions.NonexistentEntityException;
import dto.AreaFormacion;
import dto.Contenido;
import dto.EncabezadoTabla;
import dto.Materia;
import dto.Microcurriculo;
import dto.Pensum;
import dto.Seccion;
import dto.SeccionMicrocurriculo;
import dto.TablaMicrocurriculo;
import dto.TablaMicrocurriculoPK;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManagerFactory;
import util.Conexion;

/**
 *
 * @author Manuel
 */
public class RegistroMicrocurriculoBackground extends Thread {

    private Pensum pensum;

    public RegistroMicrocurriculoBackground(Pensum pensum) {
        this.pensum = pensum;
    }

    @Override
    public void run() {
        try {
            this.registrarMicrocurriculos();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private void registrarMicrocurriculos() throws Exception {
        EntityManagerFactory em = Conexion.getConexion().getBd();
        SeccionJpaController tjpa = new SeccionJpaController(em);
        SeccionMicrocurriculoJpaController sjpa = new SeccionMicrocurriculoJpaController(em);
        ContenidoJpaController cjpa = new ContenidoJpaController(em);
        TablaMicrocurriculoJpaController tmjpa = new TablaMicrocurriculoJpaController(em);
        EncabezadoTablaJpaController etjpa = new EncabezadoTablaJpaController(em);
        List<Seccion> secciones = tjpa.findSeccionEntities();
        List<Materia> materias = pensum.getMateriaList();
        MicrocurriculoJpaController mjpa = new MicrocurriculoJpaController(em);
        int id = 1;
        for (Materia m : materias) {
            Microcurriculo micro = new Microcurriculo(id++, m.getMateriaPK().getCodigoMateria(), m.getMateriaPK().getPensumCodigo());
            micro.setAreaDeFormacionId(new AreaFormacion(1));
            micro.setMateria(m);
            mjpa.create(micro);
            getDefaultSecciones(micro, secciones, sjpa, cjpa, tmjpa, etjpa);
        }
        tjpa.getEntityManager().close();
        sjpa.getEntityManager().close();
        cjpa.getEntityManager().close();
        tmjpa.getEntityManager().close();
        mjpa.getEntityManager().close();
    }

    private void getDefaultSecciones(Microcurriculo micro, List<Seccion> secciones, SeccionMicrocurriculoJpaController sjpa, ContenidoJpaController cjpa, TablaMicrocurriculoJpaController tmjpa, EncabezadoTablaJpaController etjpa) throws NonexistentEntityException, Exception {
        List<SeccionMicrocurriculo> seccionesDefault = new ArrayList<>();
        Conexion con = Conexion.getConexion();
        dao.EncabezadoJpaController encabezadoDao = new dao.EncabezadoJpaController(con.getBd());
        int id = 1;
        for (Seccion t : secciones) {
            SeccionMicrocurriculo s = new SeccionMicrocurriculo();
            short a = 0;
            s.setEditable(a);
            s.setMicrocurriculo(micro);
            if (t.getTipoSeccionId().getId() == 1) {
                s.setSeccionId(t);
                sjpa.create(s);
                Contenido c = new Contenido();
                c.setCantidadItemsLista(0);
                c.setSeccionMicrocurriculoId(s);
                c.setTexto("Empty text");
                cjpa.create(c);
            } else {
                s.setSeccionId(t);
                sjpa.create(s);
                TablaMicrocurriculo tm = new TablaMicrocurriculo();
                tm.setCantidadFilas(a);
                tm.setSeccionMicrocurriculo(s);
                tm.setTablaMicrocurriculoPK(new TablaMicrocurriculoPK(id++, s.getId()));
                tm.setCantidadColumnas(tm.getTablaMicrocurriculoPK().getId() == 1 ? 5 : 3);
                tmjpa.create(tm);
                List<EncabezadoTabla> ets = new ArrayList<>();
                for (int i = (tm.getCantidadColumnas() == 5 ? 1 : 6); i < (tm.getCantidadColumnas() == 5 ? 6 : 9); i++) {
                    EncabezadoTabla et = new EncabezadoTabla();
                    et.setEncabezadoId(encabezadoDao.findEncabezado(i));
                    et.setTablaMicrocurriculo(tm);
                    ets.add(et);
                    etjpa.create(et);
                }
                tm.setEncabezadoTablaList(ets);
            }
            seccionesDefault.add(s);
        }
        micro.setSeccionMicrocurriculoList(seccionesDefault);
    }

    public String[] obtenerContenidos(String unidad) {
        formatearContenidos(unidad);
        return null;
    }

    public String[] formatearContenidos(String unidad) {
        System.out.println(unidad);
        String[] text = unidad.split("-");
        System.out.println("Formateo :");
        for (String string : text) {
            System.out.println(string);
        }
        return null;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String cadena = "";
        while (sc.hasNext()) {
            cadena += sc.next();
        }
        System.out.println("ORIGINAL");
        System.out.println(cadena);
        RegistroMicrocurriculoBackground rm = new RegistroMicrocurriculoBackground(null);
        rm.obtenerContenidos(cadena);
    }
}
