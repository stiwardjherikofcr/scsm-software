/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dto.Seccion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.TipoSeccion;
import dto.SeccionMicrocurriculo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Manuel
 */
public class SeccionJpaController implements Serializable {

    public SeccionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Seccion seccion) {
        if (seccion.getSeccionMicrocurriculoList() == null) {
            seccion.setSeccionMicrocurriculoList(new ArrayList<SeccionMicrocurriculo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoSeccion tipoSeccionId = seccion.getTipoSeccionId();
            if (tipoSeccionId != null) {
                tipoSeccionId = em.getReference(tipoSeccionId.getClass(), tipoSeccionId.getId());
                seccion.setTipoSeccionId(tipoSeccionId);
            }
            List<SeccionMicrocurriculo> attachedSeccionMicrocurriculoList = new ArrayList<SeccionMicrocurriculo>();
            for (SeccionMicrocurriculo seccionMicrocurriculoListSeccionMicrocurriculoToAttach : seccion.getSeccionMicrocurriculoList()) {
                seccionMicrocurriculoListSeccionMicrocurriculoToAttach = em.getReference(seccionMicrocurriculoListSeccionMicrocurriculoToAttach.getClass(), seccionMicrocurriculoListSeccionMicrocurriculoToAttach.getId());
                attachedSeccionMicrocurriculoList.add(seccionMicrocurriculoListSeccionMicrocurriculoToAttach);
            }
            seccion.setSeccionMicrocurriculoList(attachedSeccionMicrocurriculoList);
            em.persist(seccion);
            if (tipoSeccionId != null) {
                tipoSeccionId.getSeccionList().add(seccion);
                tipoSeccionId = em.merge(tipoSeccionId);
            }
            for (SeccionMicrocurriculo seccionMicrocurriculoListSeccionMicrocurriculo : seccion.getSeccionMicrocurriculoList()) {
                Seccion oldSeccionIdOfSeccionMicrocurriculoListSeccionMicrocurriculo = seccionMicrocurriculoListSeccionMicrocurriculo.getSeccionId();
                seccionMicrocurriculoListSeccionMicrocurriculo.setSeccionId(seccion);
                seccionMicrocurriculoListSeccionMicrocurriculo = em.merge(seccionMicrocurriculoListSeccionMicrocurriculo);
                if (oldSeccionIdOfSeccionMicrocurriculoListSeccionMicrocurriculo != null) {
                    oldSeccionIdOfSeccionMicrocurriculoListSeccionMicrocurriculo.getSeccionMicrocurriculoList().remove(seccionMicrocurriculoListSeccionMicrocurriculo);
                    oldSeccionIdOfSeccionMicrocurriculoListSeccionMicrocurriculo = em.merge(oldSeccionIdOfSeccionMicrocurriculoListSeccionMicrocurriculo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Seccion seccion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Seccion persistentSeccion = em.find(Seccion.class, seccion.getId());
            TipoSeccion tipoSeccionIdOld = persistentSeccion.getTipoSeccionId();
            TipoSeccion tipoSeccionIdNew = seccion.getTipoSeccionId();
            List<SeccionMicrocurriculo> seccionMicrocurriculoListOld = persistentSeccion.getSeccionMicrocurriculoList();
            List<SeccionMicrocurriculo> seccionMicrocurriculoListNew = seccion.getSeccionMicrocurriculoList();
            List<String> illegalOrphanMessages = null;
            for (SeccionMicrocurriculo seccionMicrocurriculoListOldSeccionMicrocurriculo : seccionMicrocurriculoListOld) {
                if (!seccionMicrocurriculoListNew.contains(seccionMicrocurriculoListOldSeccionMicrocurriculo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SeccionMicrocurriculo " + seccionMicrocurriculoListOldSeccionMicrocurriculo + " since its seccionId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tipoSeccionIdNew != null) {
                tipoSeccionIdNew = em.getReference(tipoSeccionIdNew.getClass(), tipoSeccionIdNew.getId());
                seccion.setTipoSeccionId(tipoSeccionIdNew);
            }
            List<SeccionMicrocurriculo> attachedSeccionMicrocurriculoListNew = new ArrayList<SeccionMicrocurriculo>();
            for (SeccionMicrocurriculo seccionMicrocurriculoListNewSeccionMicrocurriculoToAttach : seccionMicrocurriculoListNew) {
                seccionMicrocurriculoListNewSeccionMicrocurriculoToAttach = em.getReference(seccionMicrocurriculoListNewSeccionMicrocurriculoToAttach.getClass(), seccionMicrocurriculoListNewSeccionMicrocurriculoToAttach.getId());
                attachedSeccionMicrocurriculoListNew.add(seccionMicrocurriculoListNewSeccionMicrocurriculoToAttach);
            }
            seccionMicrocurriculoListNew = attachedSeccionMicrocurriculoListNew;
            seccion.setSeccionMicrocurriculoList(seccionMicrocurriculoListNew);
            seccion = em.merge(seccion);
            if (tipoSeccionIdOld != null && !tipoSeccionIdOld.equals(tipoSeccionIdNew)) {
                tipoSeccionIdOld.getSeccionList().remove(seccion);
                tipoSeccionIdOld = em.merge(tipoSeccionIdOld);
            }
            if (tipoSeccionIdNew != null && !tipoSeccionIdNew.equals(tipoSeccionIdOld)) {
                tipoSeccionIdNew.getSeccionList().add(seccion);
                tipoSeccionIdNew = em.merge(tipoSeccionIdNew);
            }
            for (SeccionMicrocurriculo seccionMicrocurriculoListNewSeccionMicrocurriculo : seccionMicrocurriculoListNew) {
                if (!seccionMicrocurriculoListOld.contains(seccionMicrocurriculoListNewSeccionMicrocurriculo)) {
                    Seccion oldSeccionIdOfSeccionMicrocurriculoListNewSeccionMicrocurriculo = seccionMicrocurriculoListNewSeccionMicrocurriculo.getSeccionId();
                    seccionMicrocurriculoListNewSeccionMicrocurriculo.setSeccionId(seccion);
                    seccionMicrocurriculoListNewSeccionMicrocurriculo = em.merge(seccionMicrocurriculoListNewSeccionMicrocurriculo);
                    if (oldSeccionIdOfSeccionMicrocurriculoListNewSeccionMicrocurriculo != null && !oldSeccionIdOfSeccionMicrocurriculoListNewSeccionMicrocurriculo.equals(seccion)) {
                        oldSeccionIdOfSeccionMicrocurriculoListNewSeccionMicrocurriculo.getSeccionMicrocurriculoList().remove(seccionMicrocurriculoListNewSeccionMicrocurriculo);
                        oldSeccionIdOfSeccionMicrocurriculoListNewSeccionMicrocurriculo = em.merge(oldSeccionIdOfSeccionMicrocurriculoListNewSeccionMicrocurriculo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = seccion.getId();
                if (findSeccion(id) == null) {
                    throw new NonexistentEntityException("The seccion with id " + id + " no longer exists.");
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
            Seccion seccion;
            try {
                seccion = em.getReference(Seccion.class, id);
                seccion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The seccion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<SeccionMicrocurriculo> seccionMicrocurriculoListOrphanCheck = seccion.getSeccionMicrocurriculoList();
            for (SeccionMicrocurriculo seccionMicrocurriculoListOrphanCheckSeccionMicrocurriculo : seccionMicrocurriculoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Seccion (" + seccion + ") cannot be destroyed since the SeccionMicrocurriculo " + seccionMicrocurriculoListOrphanCheckSeccionMicrocurriculo + " in its seccionMicrocurriculoList field has a non-nullable seccionId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TipoSeccion tipoSeccionId = seccion.getTipoSeccionId();
            if (tipoSeccionId != null) {
                tipoSeccionId.getSeccionList().remove(seccion);
                tipoSeccionId = em.merge(tipoSeccionId);
            }
            em.remove(seccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Seccion> findSeccionEntities() {
        return findSeccionEntities(true, -1, -1);
    }

    public List<Seccion> findSeccionEntities(int maxResults, int firstResult) {
        return findSeccionEntities(false, maxResults, firstResult);
    }

    private List<Seccion> findSeccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Seccion.class));
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

    public Seccion findSeccion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Seccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getSeccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Seccion> rt = cq.from(Seccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
