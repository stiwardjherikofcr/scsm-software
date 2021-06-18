/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao_alternativo;

import dto.EquivalenciaMateria;
import dto.Materia;
import dto.Pensum;
import dto.PrerrequisitoMateria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author dunke
 */
public class MateriaJpaAlternativo {

    private Connection connection;

    public MateriaJpaAlternativo(Connection connection) {
        this.connection = connection;
    }

    public void create(Pensum pensum) throws SQLException {
        PreparedStatement ps = this.connection.prepareStatement("INSERT INTO materia(codigo_materia, nombre, creditos, semestre, pensum_codigo, "
                + "pensum_programa_codigo, ht, hp, hti, cr, tipo_asignatura_id) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
        for (Materia m : pensum.getMateriaList()) {
            ps.setInt(1, m.getMateriaPK().getCodigoMateria());
            ps.setString(2, m.getNombre());
            ps.setInt(3, m.getCreditos());
            ps.setInt(4, m.getSemestre());
            ps.setInt(5, pensum.getPensumPK().getCodigo());
            ps.setInt(6, pensum.getPensumPK().getProgramaCodigo());
            ps.setInt(7, m.getHt());
            ps.setInt(8, m.getHp());
            ps.setInt(9, m.getHti());
            ps.setInt(10, m.getCr());
            ps.setInt(11, m.getTipoAsignaturaId().getId());
            ps.execute();
        }

        ps = this.connection.prepareStatement("INSERT INTO prerrequisito_materia(materia_codigo_materia, materia_pensum_codigo, materia_codigo_prerrequisito, materia_pensum_prerrequisito) VALUES (?,?,?,?)");
        for (Materia m : pensum.getMateriaList()) {
            for (PrerrequisitoMateria m_r : m.getPrerrequisitoMateriaList()) {
                ps.setInt(1, m_r.getMateria().getMateriaPK().getCodigoMateria());
                ps.setInt(2, pensum.getPensumPK().getCodigo());
                ps.setInt(3, m_r.getMateria1().getMateriaPK().getCodigoMateria());
                ps.setInt(4, pensum.getPensumPK().getCodigo());
                ps.execute();
            }
        }

        ps = this.connection.prepareStatement("INSERT INTO equivalencia_materia(equivalencia_materia, nombre, materia_codigo_materia, materia_pensum_codigo) VALUES (?,?,?,?)");
        for (Materia m : pensum.getMateriaList()) {
            for (EquivalenciaMateria m_p : m.getEquivalenciaMateriaList()) {
                ps.setInt(1, m_p.getEquivalenciaMateria());
                ps.setString(2, m_p.getNombre());
                ps.setInt(3, m_p.getMateria().getMateriaPK().getCodigoMateria());
                ps.setInt(4, pensum.getPensumPK().getCodigo());
                ps.execute();
            }
        }

        this.connection.close();
    }
}
