/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import dao.exceptions.NonexistentEntityException;
import dto.Contenido;
import dto.Materia;
import dto.MateriaPK;
import dto.Microcurriculo;
import dto.Pensum;
import dto.SeccionMicrocurriculo;
import dto.TablaMicrocurriculoInfo;
import java.util.ArrayList;
import java.util.List;
import util.Conexion;

/**
 *
 * @author Manuel
 */
public class AdministrarMicrocurriculo {

    public AdministrarMicrocurriculo() {
    }

    public ArrayList<dto.Microcurriculo> obtenerMicrocurriculosPensum(int codigo, int programaCodigo) {
        AdministrarPensum administrarPensum = new AdministrarPensum();
        dto.Pensum pensum = administrarPensum.obtenerPensum(codigo, programaCodigo);
        ArrayList<dto.Microcurriculo> microcurriculos = new ArrayList<>();
        for (Materia materia : pensum.getMateriaList()) {
            microcurriculos.add(materia.getMicrocurriculoList().get(0));
        }
        return microcurriculos;
    }

    public dto.Microcurriculo obtenerMicrocurriculo(int codigoMateria, int codigoPensum) {
        Conexion con = Conexion.getConexion();
        dao.MateriaJpaController materiaDao = new dao.MateriaJpaController(con.getBd());
        dto.Microcurriculo microcurriculo = materiaDao.findMateria(new MateriaPK(codigoMateria, codigoPensum)).getMicrocurriculoList().get(0);
        return microcurriculo;
    }

    public static List<String[][]> ordenarTablaInfo(dto.Microcurriculo microcurriculo) {
        List<String[][]> tablas = new ArrayList<>();
        List<dto.SeccionMicrocurriculo> sm = microcurriculo.getSeccionMicrocurriculoList();
        for (SeccionMicrocurriculo seccionMicrocurriculo : sm) {
            if (seccionMicrocurriculo.getSeccionId().getTipoSeccionId().getId() == 2) {
                String tablaMatriz[][] = new String[seccionMicrocurriculo.getTablaMicrocurriculoList().get(0).getCantidadFilas()][seccionMicrocurriculo.getTablaMicrocurriculoList().get(0).getCantidadColumnas()];
                System.out.println("Filas" + tablaMatriz.length);
                int con = 0;
                for (int i = 0; i < tablaMatriz.length; i++) {
                    System.out.println("Columnas" + tablaMatriz[i].length);
                    for (int j = 0; j < tablaMatriz[i].length; j++) {
                        System.out.println("LISTA" + seccionMicrocurriculo.getTablaMicrocurriculoList().get(0).getTablaMicrocurriculoInfoList());
                        if (!seccionMicrocurriculo.getTablaMicrocurriculoList().get(0).getTablaMicrocurriculoInfoList().isEmpty()) {
                            tablaMatriz[seccionMicrocurriculo.getTablaMicrocurriculoList().get(0).getTablaMicrocurriculoInfoList().get(con).getTablaMicrocurriculoInfoPK().getIdFila()][seccionMicrocurriculo.getTablaMicrocurriculoList().get(0).getTablaMicrocurriculoInfoList().get(con).getTablaMicrocurriculoInfoPK().getIdColumna()] = seccionMicrocurriculo.getTablaMicrocurriculoList().get(0).getTablaMicrocurriculoInfoList().get(con).getContenidoId().getTexto();
                            con++;
                        }
                    }
                }
                tablas.add(tablaMatriz);
            }
        }
        return tablas;
    }

    public dto.Microcurriculo obtenerMicrocurriculo(int idMicrocurriculo, int codigoMateria, int codigoPensum) {
        Conexion con = Conexion.getConexion();
        dao.MicrocurriculoJpaController daoMicrocurriculo = new dao.MicrocurriculoJpaController(con.getBd());
        dto.MicrocurriculoPK microcurriculoPK = new dto.MicrocurriculoPK(idMicrocurriculo, codigoMateria, codigoPensum);
        return daoMicrocurriculo.findMicrocurriculo(microcurriculoPK);
    }

    /*
    public ArrayList<dto.Microcurriculo> obtenerTodosMicrocurriculos(int idPrograma) {

        Conexion con = Conexion.getConexion();
        dao.ProgramaJpaController daoPrograma = new dao.ProgramaJpaController(con.getBd());
        dto.Programa programa = daoPrograma.findPrograma(idPrograma);
        List<dto.Pensum> pensums = programa.getPensumList();
        ArrayList<dto.Microcurriculo> microcurriculos = new ArrayList<>();
        for (Pensum pensum : pensums) {
            List<dto.Materia> materias = pensum.getMateriaList();
            for (Materia materia : materias) {

                try {
                    microcurriculos.add(materia.getMicrocurriculoList().get(0));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
        }

        return microcurriculos;
    }
     */
    public List<dto.Materia> obtenerTodasMateria(int idPrograma) {

        Conexion con = Conexion.getConexion();
        dao.ProgramaJpaController daoPrograma = new dao.ProgramaJpaController(con.getBd());
        dto.Programa programa = daoPrograma.findPrograma(idPrograma);
        List<dto.Pensum> pensums = programa.getPensumList();
        List<dto.Materia> listaMateria = new ArrayList<>();
        for (Pensum pensum : pensums) {
            List<dto.Materia> listaMaterias = pensum.getMateriaList();
            for (Materia listaMateria1 : listaMaterias) {
                listaMateria.add(listaMateria1);
            }
        }
        return listaMateria;
    }

    public dto.Microcurriculo obtenerMicrocurriculoId(int idMicrocurriculo) {
        Conexion con = Conexion.getConexion();
        dao.MicrocurriculoJpaController daoMicrocurriculo = new dao.MicrocurriculoJpaController(con.getBd());
        List<dto.Microcurriculo> listaM = daoMicrocurriculo.findMicrocurriculoEntities();
        dto.Microcurriculo microcurriculoRta = new dto.Microcurriculo();
        for (Microcurriculo microcurriculo : listaM) {
            if (microcurriculo.getMicrocurriculoPK().getId() == idMicrocurriculo) {
                microcurriculoRta = microcurriculo;
            }
        }
        return microcurriculoRta;
    }

    public void actualizarAreaFormacionMicrocurriculo(dto.Microcurriculo microcurriculo, int areaFormacion) throws NonexistentEntityException, Exception {
        Conexion con = Conexion.getConexion();
        dao.MicrocurriculoJpaController daoMicrocurriculo = new dao.MicrocurriculoJpaController(con.getBd());
        microcurriculo.setAreaDeFormacionId(new dto.AreaFormacion(areaFormacion));
        daoMicrocurriculo.edit(microcurriculo);

    }

    public void actualizarFilasTabla(dto.TablaMicrocurriculo tabla) throws NonexistentEntityException, Exception {
        Conexion con = Conexion.getConexion();
        dao.TablaMicrocurriculoJpaController tablaDao = new dao.TablaMicrocurriculoJpaController(con.getBd());
        tablaDao.edit(tabla);
        borrarDatosTabla(tabla);
    }

    public void borrarDatosTabla(dto.TablaMicrocurriculo tabla) throws NonexistentEntityException {
        Conexion con = Conexion.getConexion();
        dao.TablaMicrocurriculoJpaController tablaDao = new dao.TablaMicrocurriculoJpaController(con.getBd());
        dao.TablaMicrocurriculoInfoJpaController infoDao = new dao.TablaMicrocurriculoInfoJpaController(con.getBd());
        List<dto.TablaMicrocurriculoInfo> tablaInfo = tabla.getTablaMicrocurriculoInfoList();
        for (TablaMicrocurriculoInfo tablaMicrocurriculoInfo : tablaInfo) {
            infoDao.destroy(tablaMicrocurriculoInfo.getTablaMicrocurriculoInfoPK());
        }
    }

    public void registrarContenidoTablas(String[][] contenido, dto.SeccionMicrocurriculo seccion) throws Exception {
        Conexion con = Conexion.getConexion();
        dto.TablaMicrocurriculo tabla = seccion.getTablaMicrocurriculoList().get(0);
        dao.ContenidoJpaController contenidoDao = new dao.ContenidoJpaController(con.getBd());
        dao.TablaMicrocurriculoInfoJpaController tablaDao = new dao.TablaMicrocurriculoInfoJpaController(con.getBd());
        for (int i = 0; i < contenido.length; i++) {
            for (int j = 0; j < contenido[i].length; j++) {
                dto.Contenido contenido2 = new Contenido();
                contenido2.setId(0);
                contenido2.setTexto(contenido[i][j]);
                contenido2.setCantidadItemsLista(0);
                contenido2.setSeccionMicrocurriculoId(seccion);
                contenidoDao.create(contenido2);
                dto.TablaMicrocurriculoInfo tablainfo = new dto.TablaMicrocurriculoInfo(i, j, tabla.getTablaMicrocurriculoPK().getId(), seccion.getId());
                tablainfo.setContenidoId(contenido2);
                tablainfo.setTablaMicrocurriculo(tabla);
                tablaDao.create(tablainfo);
            }
        }
    }

    public void ingresarContenidoSecciones(String informacion, int idSeccionMicrocurriculo) throws NonexistentEntityException, Exception {
        Conexion con = Conexion.getConexion();
        dto.Contenido contenido = new dto.Contenido();
        dao.ContenidoJpaController daoContenido = new dao.ContenidoJpaController(con.getBd());
        dao.SeccionMicrocurriculoJpaController daoSeccionMicrocurriculo = new dao.SeccionMicrocurriculoJpaController(con.getBd());
        dto.SeccionMicrocurriculo seccionMicro = daoSeccionMicrocurriculo.findSeccionMicrocurriculo(idSeccionMicrocurriculo);
        seccionMicro.getContenidoList().get(0).setTexto(informacion);
        daoSeccionMicrocurriculo.edit(seccionMicro);
        daoContenido.destroy(seccionMicro.getContenidoList().get(0).getId());
        contenido.setTexto(informacion);
        contenido.setSeccionMicrocurriculoId(daoSeccionMicrocurriculo.findSeccionMicrocurriculo(idSeccionMicrocurriculo));
        contenido.setCantidadItemsLista(0);
        daoContenido.create(contenido);
    }

    public List<dto.Seccion> obtenerSecciones() {
        Conexion con = Conexion.getConexion();
        dao.SeccionJpaController daoSeccion = new dao.SeccionJpaController(con.getBd());
        return daoSeccion.findSeccionEntities();
    }

    public void registrarMicrocurriculos(Pensum pensum) {
        new RegistroMicrocurriculoBackground(pensum).start();
    }

    public List<dto.AreaFormacion> obtenerAreasFormacion() {
        Conexion con = Conexion.getConexion();
        dao.AreaFormacionJpaController daoAreasFormacion = new dao.AreaFormacionJpaController(con.getBd());
        return daoAreasFormacion.findAreaFormacionEntities();
    }

    public List<dto.TipoAsignatura> obtenerTiposAisgnatura() {
        Conexion con = Conexion.getConexion();
        dao.TipoAsignaturaJpaController daoTipoAsignatura = new dao.TipoAsignaturaJpaController(con.getBd());
        return daoTipoAsignatura.findTipoAsignaturaEntities();
    }

}
