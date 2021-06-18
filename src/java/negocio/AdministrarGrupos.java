/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import java.util.List;

/**
 *
 * @author Manuel
 */
public class AdministrarGrupos {

    public AdministrarGrupos() {
    }

    public List<dto.Docente> obtenerDocentes(dto.Programa programa) {
        return programa.getDepartamentoId().getDocenteList();
    }

}
