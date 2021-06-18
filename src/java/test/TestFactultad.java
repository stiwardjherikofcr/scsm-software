/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import util.Conexion;
import dao.*;
import dto.Departamento;
import dto.Facultad;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Manuel
 */
public class TestFactultad {
    
    public static void main(String[] args) throws ClassNotFoundException {
        Conexion con = Conexion.getConexion();
        FacultadJpaController facultadDao = new dao.FacultadJpaController(con.getBd());
      
   
//        for (Facultad findFacultadEntity : facultadDao.findFacultadEntities()) {
//            
//        
//        dto.Facultad facultad = findFacultadEntity;
//            System.out.println(facultad.getNombre());
//            
//            for (Departamento departamento : facultad.getDepartamentoList()) {
//                System.out.println(departamento.getNombreDepartamento());
//            }
//        }


    }
        
    
    
}
