/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.Microcurriculo;
import dto.Seccion;
import dto.Contenido;
import java.util.ArrayList;
import java.util.List;
import dto.SeccionCambio;
import dto.SeccionMicrocurriculo;
import dto.TablaMicrocurriculo;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Manuel
 */
public class SeccionMicrocurriculoJpaController implements Serializable {

    public SeccionMicrocurriculoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SeccionMicrocurriculo seccionMicrocurriculo) {
        if (seccionMicrocurriculo.getContenidoList() == null) {
            seccionMicrocurriculo.setContenidoList(new ArrayList<Contenido>());
        }
        if (seccionMicrocurriculo.getSeccionCambioList() == null) {
            seccionMicrocurriculo.setSeccionCambioList(new ArrayList<SeccionCambio>());
        }
        if (seccionMicrocurriculo.getSeccionCambioList1() == null) {
            seccionMicrocurriculo.setSeccionCambioList1(new ArrayList<SeccionCambio>());
        }
        if (seccionMicrocurriculo.getTablaMicrocurriculoList() == null) {
            seccionMicrocurriculo.setTablaMicrocurriculoList(new ArrayList<TablaMicrocurriculo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Microcurriculo microcurriculo = seccionMicrocurriculo.getMicrocurriculo();
            if (microcurriculo != null) {
                microcurriculo = em.getReference(microcurriculo.getClass(), microcurriculo.getMicrocurriculoPK());
                seccionMicrocurriculo.setMicrocurriculo(microcurriculo);
            }
            Seccion seccionId = seccionMicrocurriculo.getSeccionId();
            if (seccionId != null) {
                seccionId = em.getReference(seccionId.getClass(), seccionId.getId());
                seccionMicrocurriculo.setSeccionId(seccionId);
            }
            List<Contenido> attachedContenidoList = new ArrayList<Contenido>();
            for (Contenido contenidoListContenidoToAttach : seccionMicrocurriculo.getContenidoList()) {
                contenidoListContenidoToAttach = em.getReference(contenidoListContenidoToAttach.getClass(), contenidoListContenidoToAttach.getId());
                attachedContenidoList.add(contenidoListContenidoToAttach);
            }
            seccionMicrocurriculo.setContenidoList(attachedContenidoList);
            List<SeccionCambio> attachedSeccionCambioList = new ArrayList<SeccionCambio>();
            for (SeccionCambio seccionCambioListSeccionCambioToAttach : seccionMicrocurriculo.getSeccionCambioList()) {
                seccionCambioListSeccionCambioToAttach = em.getReference(seccionCambioListSeccionCambioToAttach.getClass(), seccionCambioListSeccionCambioToAttach.getId());
                attachedSeccionCambioList.add(seccionCambioListSeccionCambioToAttach);
            }
            seccionMicrocurriculo.setSeccionCambioList(attachedSeccionCambioList);
            List<SeccionCambio> attachedSeccionCambioList1 = new ArrayList<SeccionCambio>();
            for (SeccionCambio seccionCambioList1SeccionCambioToAttach : seccionMicrocurriculo.getSeccionCambioList1()) {
                seccionCambioList1SeccionCambioToAttach = em.getReference(seccionCambioList1SeccionCambioToAttach.getClass(), seccionCambioList1SeccionCambioToAttach.getId());
                attachedSeccionCambioList1.add(seccionCambioList1SeccionCambioToAttach);
            }
            seccionMicrocurriculo.setSeccionCambioList1(attachedSeccionCambioList1);
            List<TablaMicrocurriculo> attachedTablaMicrocurriculoList = new ArrayList<TablaMicrocurriculo>();
            for (TablaMicrocurriculo tablaMicrocurriculoListTablaMicrocurriculoToAttach : seccionMicrocurriculo.getTablaMicrocurriculoList()) {
                tablaMicrocurriculoListTablaMicrocurriculoToAttach = em.getReference(tablaMicrocurriculoListTablaMicrocurriculoToAttach.getClass(), tablaMicrocurriculoListTablaMicrocurriculoToAttach.getTablaMicrocurriculoPK());
                attachedTablaMicrocurriculoList.add(tablaMicrocurriculoListTablaMicrocurriculoToAttach);
            }
            seccionMicrocurriculo.setTablaMicrocurriculoList(attachedTablaMicrocurriculoList);
            em.persist(seccionMicrocurriculo);
            if (microcurriculo != null) {
                microcurriculo.getSeccionMicrocurriculoList().add(seccionMicrocurriculo);
                microcurriculo = em.merge(microcurriculo);
            }
            if (seccionId != null) {
                seccionId.getSeccionMicrocurriculoList().add(seccionMicrocurriculo);
                seccionId = em.merge(seccionId);
            }
            for (Contenido contenidoListContenido : seccionMicrocurriculo.getContenidoList()) {
                SeccionMicrocurriculo oldSeccionMicrocurriculoIdOfContenidoListContenido = contenidoListContenido.getSeccionMicrocurriculoId();
                contenidoListContenido.setSeccionMicrocurriculoId(seccionMicrocurriculo);
                contenidoListContenido = em.merge(contenidoListContenido);
                if (oldSeccionMicrocurriculoIdOfContenidoListContenido != null) {
                    oldSeccionMicrocurriculoIdOfContenidoListContenido.getContenidoList().remove(contenidoListContenido);
                    oldSeccionMicrocurriculoIdOfContenidoListContenido = em.merge(oldSeccionMicrocurriculoIdOfContenidoListContenido);
                }
            }
            for (SeccionCambio seccionCambioListSeccionCambio : seccionMicrocurriculo.getSeccionCambioList()) {
                SeccionMicrocurriculo oldSeccionMicrocurriculoIdNuevoOfSeccionCambioListSeccionCambio = seccionCambioListSeccionCambio.getSeccionMicrocurriculoIdNuevo();
                seccionCambioListSeccionCambio.setSeccionMicrocurriculoIdNuevo(seccionMicrocurriculo);
                seccionCambioListSeccionCambio = em.merge(seccionCambioListSeccionCambio);
                if (oldSeccionMicrocurriculoIdNuevoOfSeccionCambioListSeccionCambio != null) {
                    oldSeccionMicrocurriculoIdNuevoOfSeccionCambioListSeccionCambio.getSeccionCambioList().remove(seccionCambioListSeccionCambio);
                    oldSeccionMicrocurriculoIdNuevoOfSeccionCambioListSeccionCambio = em.merge(oldSeccionMicrocurriculoIdNuevoOfSeccionCambioListSeccionCambio);
                }
            }
            for (SeccionCambio seccionCambioList1SeccionCambio : seccionMicrocurriculo.getSeccionCambioList1()) {
                SeccionMicrocurriculo oldSeccionMicrocurriculoIdAntiguaOfSeccionCambioList1SeccionCambio = seccionCambioList1SeccionCambio.getSeccionMicrocurriculoIdAntigua();
                seccionCambioList1SeccionCambio.setSeccionMicrocurriculoIdAntigua(seccionMicrocurriculo);
                seccionCambioList1SeccionCambio = em.merge(seccionCambioList1SeccionCambio);
                if (oldSeccionMicrocurriculoIdAntiguaOfSeccionCambioList1SeccionCambio != null) {
                    oldSeccionMicrocurriculoIdAntiguaOfSeccionCambioList1SeccionCambio.getSeccionCambioList1().remove(seccionCambioList1SeccionCambio);
                    oldSeccionMicrocurriculoIdAntiguaOfSeccionCambioList1SeccionCambio = em.merge(oldSeccionMicrocurriculoIdAntiguaOfSeccionCambioList1SeccionCambio);
                }
            }
            for (TablaMicrocurriculo tablaMicrocurriculoListTablaMicrocurriculo : seccionMicrocurriculo.getTablaMicrocurriculoList()) {
                SeccionMicrocurriculo oldSeccionMicrocurriculoOfTablaMicrocurriculoListTablaMicrocurriculo = tablaMicrocurriculoListTablaMicrocurriculo.getSeccionMicrocurriculo();
                tablaMicrocurriculoListTablaMicrocurriculo.setSeccionMicrocurriculo(seccionMicrocurriculo);
                tablaMicrocurriculoListTablaMicrocurriculo = em.merge(tablaMicrocurriculoListTablaMicrocurriculo);
                if (oldSeccionMicrocurriculoOfTablaMicrocurriculoListTablaMicrocurriculo != null) {
                    oldSeccionMicrocurriculoOfTablaMicrocurriculoListTablaMicrocurriculo.getTablaMicrocurriculoList().remove(tablaMicrocurriculoListTablaMicrocurriculo);
                    oldSeccionMicrocurriculoOfTablaMicrocurriculoListTablaMicrocurriculo = em.merge(oldSeccionMicrocurriculoOfTablaMicrocurriculoListTablaMicrocurriculo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SeccionMicrocurriculo seccionMicrocurriculo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SeccionMicrocurriculo persistentSeccionMicrocurriculo = em.find(SeccionMicrocurriculo.class, seccionMicrocurriculo.getId());
            Microcurriculo microcurriculoOld = persistentSeccionMicrocurriculo.getMicrocurriculo();
            Microcurriculo microcurriculoNew = seccionMicrocurriculo.getMicrocurriculo();
            Seccion seccionIdOld = persistentSeccionMicrocurriculo.getSeccionId();
            Seccion seccionIdNew = seccionMicrocurriculo.getSeccionId();
            List<Contenido> contenidoListOld = persistentSeccionMicrocurriculo.getContenidoList();
            List<Contenido> contenidoListNew = seccionMicrocurriculo.getContenidoList();
            List<SeccionCambio> seccionCambioListOld = persistentSeccionMicrocurriculo.getSeccionCambioList();
            List<SeccionCambio> seccionCambioListNew = seccionMicrocurriculo.getSeccionCambioList();
            List<SeccionCambio> seccionCambioList1Old = persistentSeccionMicrocurriculo.getSeccionCambioList1();
            List<SeccionCambio> seccionCambioList1New = seccionMicrocurriculo.getSeccionCambioList1();
            List<TablaMicrocurriculo> tablaMicrocurriculoListOld = persistentSeccionMicrocurriculo.getTablaMicrocurriculoList();
            List<TablaMicrocurriculo> tablaMicrocurriculoListNew = seccionMicrocurriculo.getTablaMicrocurriculoList();
            List<String> illegalOrphanMessages = null;
            for (Contenido contenidoListOldContenido : contenidoListOld) {
                if (!contenidoListNew.contains(contenidoListOldContenido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Contenido " + contenidoListOldContenido + " since its seccionMicrocurriculoId field is not nullable.");
                }
            }
            for (SeccionCambio seccionCambioListOldSeccionCambio : seccionCambioListOld) {
                if (!seccionCambioListNew.contains(seccionCambioListOldSeccionCambio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SeccionCambio " + seccionCambioListOldSeccionCambio + " since its seccionMicrocurriculoIdNuevo field is not nullable.");
                }
            }
            for (SeccionCambio seccionCambioList1OldSeccionCambio : seccionCambioList1Old) {
                if (!seccionCambioList1New.contains(seccionCambioList1OldSeccionCambio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SeccionCambio " + seccionCambioList1OldSeccionCambio + " since its seccionMicrocurriculoIdAntigua field is not nullable.");
                }
            }
            for (TablaMicrocurriculo tablaMicrocurriculoListOldTablaMicrocurriculo : tablaMicrocurriculoListOld) {
                if (!tablaMicrocurriculoListNew.contains(tablaMicrocurriculoListOldTablaMicrocurriculo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TablaMicrocurriculo " + tablaMicrocurriculoListOldTablaMicrocurriculo + " since its seccionMicrocurriculo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (microcurriculoNew != null) {
                microcurriculoNew = em.getReference(microcurriculoNew.getClass(), microcurriculoNew.getMicrocurriculoPK());
                seccionMicrocurriculo.setMicrocurriculo(microcurriculoNew);
            }
            if (seccionIdNew != null) {
                seccionIdNew = em.getReference(seccionIdNew.getClass(), seccionIdNew.getId());
                seccionMicrocurriculo.setSeccionId(seccionIdNew);
            }
            List<Contenido> attachedContenidoListNew = new ArrayList<Contenido>();
            for (Contenido contenidoListNewContenidoToAttach : contenidoListNew) {
                contenidoListNewContenidoToAttach = em.getReference(contenidoListNewContenidoToAttach.getClass(), contenidoListNewContenidoToAttach.getId());
                attachedContenidoListNew.add(contenidoListNewContenidoToAttach);
            }
            contenidoListNew = attachedContenidoListNew;
            seccionMicrocurriculo.setContenidoList(contenidoListNew);
            List<SeccionCambio> attachedSeccionCambioListNew = new ArrayList<SeccionCambio>();
            for (SeccionCambio seccionCambioListNewSeccionCambioToAttach : seccionCambioListNew) {
                seccionCambioListNewSeccionCambioToAttach = em.getReference(seccionCambioListNewSeccionCambioToAttach.getClass(), seccionCambioListNewSeccionCambioToAttach.getId());
                attachedSeccionCambioListNew.add(seccionCambioListNewSeccionCambioToAttach);
            }
            seccionCambioListNew = attachedSeccionCambioListNew;
            seccionMicrocurriculo.setSeccionCambioList(seccionCambioListNew);
            List<SeccionCambio> attachedSeccionCambioList1New = new ArrayList<SeccionCambio>();
            for (SeccionCambio seccionCambioList1NewSeccionCambioToAttach : seccionCambioList1New) {
                seccionCambioList1NewSeccionCambioToAttach = em.getReference(seccionCambioList1NewSeccionCambioToAttach.getClass(), seccionCambioList1NewSeccionCambioToAttach.getId());
                attachedSeccionCambioList1New.add(seccionCambioList1NewSeccionCambioToAttach);
            }
            seccionCambioList1New = attachedSeccionCambioList1New;
            seccionMicrocurriculo.setSeccionCambioList1(seccionCambioList1New);
            List<TablaMicrocurriculo> attachedTablaMicrocurriculoListNew = new ArrayList<TablaMicrocurriculo>();
            for (TablaMicrocurriculo tablaMicrocurriculoListNewTablaMicrocurriculoToAttach : tablaMicrocurriculoListNew) {
                tablaMicrocurriculoListNewTablaMicrocurriculoToAttach = em.getReference(tablaMicrocurriculoListNewTablaMicrocurriculoToAttach.getClass(), tablaMicrocurriculoListNewTablaMicrocurriculoToAttach.getTablaMicrocurriculoPK());
                attachedTablaMicrocurriculoListNew.add(tablaMicrocurriculoListNewTablaMicrocurriculoToAttach);
            }
            tablaMicrocurriculoListNew = attachedTablaMicrocurriculoListNew;
            seccionMicrocurriculo.setTablaMicrocurriculoList(tablaMicrocurriculoListNew);
            seccionMicrocurriculo = em.merge(seccionMicrocurriculo);
            if (microcurriculoOld != null && !microcurriculoOld.equals(microcurriculoNew)) {
                microcurriculoOld.getSeccionMicrocurriculoList().remove(seccionMicrocurriculo);
                microcurriculoOld = em.merge(microcurriculoOld);
            }
            if (microcurriculoNew != null && !microcurriculoNew.equals(microcurriculoOld)) {
                microcurriculoNew.getSeccionMicrocurriculoList().add(seccionMicrocurriculo);
                microcurriculoNew = em.merge(microcurriculoNew);
            }
            if (seccionIdOld != null && !seccionIdOld.equals(seccionIdNew)) {
                seccionIdOld.getSeccionMicrocurriculoList().remove(seccionMicrocurriculo);
                seccionIdOld = em.merge(seccionIdOld);
            }
            if (seccionIdNew != null && !seccionIdNew.equals(seccionIdOld)) {
                seccionIdNew.getSeccionMicrocurriculoList().add(seccionMicrocurriculo);
                seccionIdNew = em.merge(seccionIdNew);
            }
            for (Contenido contenidoListNewContenido : contenidoListNew) {
                if (!contenidoListOld.contains(contenidoListNewContenido)) {
                    SeccionMicrocurriculo oldSeccionMicrocurriculoIdOfContenidoListNewContenido = contenidoListNewContenido.getSeccionMicrocurriculoId();
                    contenidoListNewContenido.setSeccionMicrocurriculoId(seccionMicrocurriculo);
                    contenidoListNewContenido = em.merge(contenidoListNewContenido);
                    if (oldSeccionMicrocurriculoIdOfContenidoListNewContenido != null && !oldSeccionMicrocurriculoIdOfContenidoListNewContenido.equals(seccionMicrocurriculo)) {
                        oldSeccionMicrocurriculoIdOfContenidoListNewContenido.getContenidoList().remove(contenidoListNewContenido);
                        oldSeccionMicrocurriculoIdOfContenidoListNewContenido = em.merge(oldSeccionMicrocurriculoIdOfContenidoListNewContenido);
                    }
                }
            }
            for (SeccionCambio seccionCambioListNewSeccionCambio : seccionCambioListNew) {
                if (!seccionCambioListOld.contains(seccionCambioListNewSeccionCambio)) {
                    SeccionMicrocurriculo oldSeccionMicrocurriculoIdNuevoOfSeccionCambioListNewSeccionCambio = seccionCambioListNewSeccionCambio.getSeccionMicrocurriculoIdNuevo();
                    seccionCambioListNewSeccionCambio.setSeccionMicrocurriculoIdNuevo(seccionMicrocurriculo);
                    seccionCambioListNewSeccionCambio = em.merge(seccionCambioListNewSeccionCambio);
                    if (oldSeccionMicrocurriculoIdNuevoOfSeccionCambioListNewSeccionCambio != null && !oldSeccionMicrocurriculoIdNuevoOfSeccionCambioListNewSeccionCambio.equals(seccionMicrocurriculo)) {
                        oldSeccionMicrocurriculoIdNuevoOfSeccionCambioListNewSeccionCambio.getSeccionCambioList().remove(seccionCambioListNewSeccionCambio);
                        oldSeccionMicrocurriculoIdNuevoOfSeccionCambioListNewSeccionCambio = em.merge(oldSeccionMicrocurriculoIdNuevoOfSeccionCambioListNewSeccionCambio);
                    }
                }
            }
            for (SeccionCambio seccionCambioList1NewSeccionCambio : seccionCambioList1New) {
                if (!seccionCambioList1Old.contains(seccionCambioList1NewSeccionCambio)) {
                    SeccionMicrocurriculo oldSeccionMicrocurriculoIdAntiguaOfSeccionCambioList1NewSeccionCambio = seccionCambioList1NewSeccionCambio.getSeccionMicrocurriculoIdAntigua();
                    seccionCambioList1NewSeccionCambio.setSeccionMicrocurriculoIdAntigua(seccionMicrocurriculo);
                    seccionCambioList1NewSeccionCambio = em.merge(seccionCambioList1NewSeccionCambio);
                    if (oldSeccionMicrocurriculoIdAntiguaOfSeccionCambioList1NewSeccionCambio != null && !oldSeccionMicrocurriculoIdAntiguaOfSeccionCambioList1NewSeccionCambio.equals(seccionMicrocurriculo)) {
                        oldSeccionMicrocurriculoIdAntiguaOfSeccionCambioList1NewSeccionCambio.getSeccionCambioList1().remove(seccionCambioList1NewSeccionCambio);
                        oldSeccionMicrocurriculoIdAntiguaOfSeccionCambioList1NewSeccionCambio = em.merge(oldSeccionMicrocurriculoIdAntiguaOfSeccionCambioList1NewSeccionCambio);
                    }
                }
            }
            for (TablaMicrocurriculo tablaMicrocurriculoListNewTablaMicrocurriculo : tablaMicrocurriculoListNew) {
                if (!tablaMicrocurriculoListOld.contains(tablaMicrocurriculoListNewTablaMicrocurriculo)) {
                    SeccionMicrocurriculo oldSeccionMicrocurriculoOfTablaMicrocurriculoListNewTablaMicrocurriculo = tablaMicrocurriculoListNewTablaMicrocurriculo.getSeccionMicrocurriculo();
                    tablaMicrocurriculoListNewTablaMicrocurriculo.setSeccionMicrocurriculo(seccionMicrocurriculo);
                    tablaMicrocurriculoListNewTablaMicrocurriculo = em.merge(tablaMicrocurriculoListNewTablaMicrocurriculo);
                    if (oldSeccionMicrocurriculoOfTablaMicrocurriculoListNewTablaMicrocurriculo != null && !oldSeccionMicrocurriculoOfTablaMicrocurriculoListNewTablaMicrocurriculo.equals(seccionMicrocurriculo)) {
                        oldSeccionMicrocurriculoOfTablaMicrocurriculoListNewTablaMicrocurriculo.getTablaMicrocurriculoList().remove(tablaMicrocurriculoListNewTablaMicrocurriculo);
                        oldSeccionMicrocurriculoOfTablaMicrocurriculoListNewTablaMicrocurriculo = em.merge(oldSeccionMicrocurriculoOfTablaMicrocurriculoListNewTablaMicrocurriculo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = seccionMicrocurriculo.getId();
                if (findSeccionMicrocurriculo(id) == null) {
                    throw new NonexistentEntityException("The seccionMicrocurriculo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SeccionMicrocurriculo seccionMicrocurriculo;
            try {
                seccionMicrocurriculo = em.getReference(SeccionMicrocurriculo.class, id);
                seccionMicrocurriculo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The seccionMicrocurriculo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Contenido> contenidoListOrphanCheck = seccionMicrocurriculo.getContenidoList();
            for (Contenido contenidoListOrphanCheckContenido : contenidoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SeccionMicrocurriculo (" + seccionMicrocurriculo + ") cannot be destroyed since the Contenido " + contenidoListOrphanCheckContenido + " in its contenidoList field has a non-nullable seccionMicrocurriculoId field.");
            }
            List<SeccionCambio> seccionCambioListOrphanCheck = seccionMicrocurriculo.getSeccionCambioList();
            for (SeccionCambio seccionCambioListOrphanCheckSeccionCambio : seccionCambioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SeccionMicrocurriculo (" + seccionMicrocurriculo + ") cannot be destroyed since the SeccionCambio " + seccionCambioListOrphanCheckSeccionCambio + " in its seccionCambioList field has a non-nullable seccionMicrocurriculoIdNuevo field.");
            }
            List<SeccionCambio> seccionCambioList1OrphanCheck = seccionMicrocurriculo.getSeccionCambioList1();
            for (SeccionCambio seccionCambioList1OrphanCheckSeccionCambio : seccionCambioList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SeccionMicrocurriculo (" + seccionMicrocurriculo + ") cannot be destroyed since the SeccionCambio " + seccionCambioList1OrphanCheckSeccionCambio + " in its seccionCambioList1 field has a non-nullable seccionMicrocurriculoIdAntigua field.");
            }
            List<TablaMicrocurriculo> tablaMicrocurriculoListOrphanCheck = seccionMicrocurriculo.getTablaMicrocurriculoList();
            for (TablaMicrocurriculo tablaMicrocurriculoListOrphanCheckTablaMicrocurriculo : tablaMicrocurriculoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SeccionMicrocurriculo (" + seccionMicrocurriculo + ") cannot be destroyed since the TablaMicrocurriculo " + tablaMicrocurriculoListOrphanCheckTablaMicrocurriculo + " in its tablaMicrocurriculoList field has a non-nullable seccionMicrocurriculo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Microcurriculo microcurriculo = seccionMicrocurriculo.getMicrocurriculo();
            if (microcurriculo != null) {
                microcurriculo.getSeccionMicrocurriculoList().remove(seccionMicrocurriculo);
                microcurriculo = em.merge(microcurriculo);
            }
            Seccion seccionId = seccionMicrocurriculo.getSeccionId();
            if (seccionId != null) {
                seccionId.getSeccionMicrocurriculoList().remove(seccionMicrocurriculo);
                seccionId = em.merge(seccionId);
            }
            em.remove(seccionMicrocurriculo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SeccionMicrocurriculo> findSeccionMicrocurriculoEntities() {
        return findSeccionMicrocurriculoEntities(true, -1, -1);
    }

    public List<SeccionMicrocurriculo> findSeccionMicrocurriculoEntities(int maxResults, int firstResult) {
        return findSeccionMicrocurriculoEntities(false, maxResults, firstResult);
    }

    private List<SeccionMicrocurriculo> findSeccionMicrocurriculoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SeccionMicrocurriculo.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public SeccionMicrocurriculo findSeccionMicrocurriculo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SeccionMicrocurriculo.class, id);
        } finally {
            em.close();
        }
    }

    public int getSeccionMicrocurriculoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SeccionMicrocurriculo> rt = cq.from(SeccionMicrocurriculo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
