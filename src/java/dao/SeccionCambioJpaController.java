/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.Cambio;
import dto.SeccionCambio;
import dto.SeccionMicrocurriculo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Manuel
 */
public class SeccionCambioJpaController implements Serializable {

    public SeccionCambioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SeccionCambio seccionCambio) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cambio cambioId = seccionCambio.getCambioId();
            if (cambioId != null) {
                cambioId = em.getReference(cambioId.getClass(), cambioId.getId());
                seccionCambio.setCambioId(cambioId);
            }
            SeccionMicrocurriculo seccionMicrocurriculoIdNuevo = seccionCambio.getSeccionMicrocurriculoIdNuevo();
            if (seccionMicrocurriculoIdNuevo != null) {
                seccionMicrocurriculoIdNuevo = em.getReference(seccionMicrocurriculoIdNuevo.getClass(), seccionMicrocurriculoIdNuevo.getId());
                seccionCambio.setSeccionMicrocurriculoIdNuevo(seccionMicrocurriculoIdNuevo);
            }
            SeccionMicrocurriculo seccionMicrocurriculoIdAntigua = seccionCambio.getSeccionMicrocurriculoIdAntigua();
            if (seccionMicrocurriculoIdAntigua != null) {
                seccionMicrocurriculoIdAntigua = em.getReference(seccionMicrocurriculoIdAntigua.getClass(), seccionMicrocurriculoIdAntigua.getId());
                seccionCambio.setSeccionMicrocurriculoIdAntigua(seccionMicrocurriculoIdAntigua);
            }
            em.persist(seccionCambio);
            if (cambioId != null) {
                cambioId.getSeccionCambioList().add(seccionCambio);
                cambioId = em.merge(cambioId);
            }
            if (seccionMicrocurriculoIdNuevo != null) {
                seccionMicrocurriculoIdNuevo.getSeccionCambioList().add(seccionCambio);
                seccionMicrocurriculoIdNuevo = em.merge(seccionMicrocurriculoIdNuevo);
            }
            if (seccionMicrocurriculoIdAntigua != null) {
                seccionMicrocurriculoIdAntigua.getSeccionCambioList().add(seccionCambio);
                seccionMicrocurriculoIdAntigua = em.merge(seccionMicrocurriculoIdAntigua);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SeccionCambio seccionCambio) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SeccionCambio persistentSeccionCambio = em.find(SeccionCambio.class, seccionCambio.getId());
            Cambio cambioIdOld = persistentSeccionCambio.getCambioId();
            Cambio cambioIdNew = seccionCambio.getCambioId();
            SeccionMicrocurriculo seccionMicrocurriculoIdNuevoOld = persistentSeccionCambio.getSeccionMicrocurriculoIdNuevo();
            SeccionMicrocurriculo seccionMicrocurriculoIdNuevoNew = seccionCambio.getSeccionMicrocurriculoIdNuevo();
            SeccionMicrocurriculo seccionMicrocurriculoIdAntiguaOld = persistentSeccionCambio.getSeccionMicrocurriculoIdAntigua();
            SeccionMicrocurriculo seccionMicrocurriculoIdAntiguaNew = seccionCambio.getSeccionMicrocurriculoIdAntigua();
            if (cambioIdNew != null) {
                cambioIdNew = em.getReference(cambioIdNew.getClass(), cambioIdNew.getId());
                seccionCambio.setCambioId(cambioIdNew);
            }
            if (seccionMicrocurriculoIdNuevoNew != null) {
                seccionMicrocurriculoIdNuevoNew = em.getReference(seccionMicrocurriculoIdNuevoNew.getClass(), seccionMicrocurriculoIdNuevoNew.getId());
                seccionCambio.setSeccionMicrocurriculoIdNuevo(seccionMicrocurriculoIdNuevoNew);
            }
            if (seccionMicrocurriculoIdAntiguaNew != null) {
                seccionMicrocurriculoIdAntiguaNew = em.getReference(seccionMicrocurriculoIdAntiguaNew.getClass(), seccionMicrocurriculoIdAntiguaNew.getId());
                seccionCambio.setSeccionMicrocurriculoIdAntigua(seccionMicrocurriculoIdAntiguaNew);
            }
            seccionCambio = em.merge(seccionCambio);
            if (cambioIdOld != null && !cambioIdOld.equals(cambioIdNew)) {
                cambioIdOld.getSeccionCambioList().remove(seccionCambio);
                cambioIdOld = em.merge(cambioIdOld);
            }
            if (cambioIdNew != null && !cambioIdNew.equals(cambioIdOld)) {
                cambioIdNew.getSeccionCambioList().add(seccionCambio);
                cambioIdNew = em.merge(cambioIdNew);
            }
            if (seccionMicrocurriculoIdNuevoOld != null && !seccionMicrocurriculoIdNuevoOld.equals(seccionMicrocurriculoIdNuevoNew)) {
                seccionMicrocurriculoIdNuevoOld.getSeccionCambioList().remove(seccionCambio);
                seccionMicrocurriculoIdNuevoOld = em.merge(seccionMicrocurriculoIdNuevoOld);
            }
            if (seccionMicrocurriculoIdNuevoNew != null && !seccionMicrocurriculoIdNuevoNew.equals(seccionMicrocurriculoIdNuevoOld)) {
                seccionMicrocurriculoIdNuevoNew.getSeccionCambioList().add(seccionCambio);
                seccionMicrocurriculoIdNuevoNew = em.merge(seccionMicrocurriculoIdNuevoNew);
            }
            if (seccionMicrocurriculoIdAntiguaOld != null && !seccionMicrocurriculoIdAntiguaOld.equals(seccionMicrocurriculoIdAntiguaNew)) {
                seccionMicrocurriculoIdAntiguaOld.getSeccionCambioList().remove(seccionCambio);
                seccionMicrocurriculoIdAntiguaOld = em.merge(seccionMicrocurriculoIdAntiguaOld);
            }
            if (seccionMicrocurriculoIdAntiguaNew != null && !seccionMicrocurriculoIdAntiguaNew.equals(seccionMicrocurriculoIdAntiguaOld)) {
                seccionMicrocurriculoIdAntiguaNew.getSeccionCambioList().add(seccionCambio);
                seccionMicrocurriculoIdAntiguaNew = em.merge(seccionMicrocurriculoIdAntiguaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = seccionCambio.getId();
                if (findSeccionCambio(id) == null) {
                    throw new NonexistentEntityException("The seccionCambio with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SeccionCambio seccionCambio;
            try {
                seccionCambio = em.getReference(SeccionCambio.class, id);
                seccionCambio.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The seccionCambio with id " + id + " no longer exists.", enfe);
            }
            Cambio cambioId = seccionCambio.getCambioId();
            if (cambioId != null) {
                cambioId.getSeccionCambioList().remove(seccionCambio);
                cambioId = em.merge(cambioId);
            }
            SeccionMicrocurriculo seccionMicrocurriculoIdNuevo = seccionCambio.getSeccionMicrocurriculoIdNuevo();
            if (seccionMicrocurriculoIdNuevo != null) {
                seccionMicrocurriculoIdNuevo.getSeccionCambioList().remove(seccionCambio);
                seccionMicrocurriculoIdNuevo = em.merge(seccionMicrocurriculoIdNuevo);
            }
            SeccionMicrocurriculo seccionMicrocurriculoIdAntigua = seccionCambio.getSeccionMicrocurriculoIdAntigua();
            if (seccionMicrocurriculoIdAntigua != null) {
                seccionMicrocurriculoIdAntigua.getSeccionCambioList().remove(seccionCambio);
                seccionMicrocurriculoIdAntigua = em.merge(seccionMicrocurriculoIdAntigua);
            }
            em.remove(seccionCambio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SeccionCambio> findSeccionCambioEntities() {
        return findSeccionCambioEntities(true, -1, -1);
    }

    public List<SeccionCambio> findSeccionCambioEntities(int maxResults, int firstResult) {
        return findSeccionCambioEntities(false, maxResults, firstResult);
    }

    private List<SeccionCambio> findSeccionCambioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SeccionCambio.class));
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

    public SeccionCambio findSeccionCambio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SeccionCambio.class, id);
        } finally {
            em.close();
        }
    }

    public int getSeccionCambioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SeccionCambio> rt = cq.from(SeccionCambio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
