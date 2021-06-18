/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dto.Cambio;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.Estado;
import dto.SeccionCambio;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Manuel
 */
public class CambioJpaController implements Serializable {

    public CambioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cambio cambio) {
        if (cambio.getSeccionCambioList() == null) {
            cambio.setSeccionCambioList(new ArrayList<SeccionCambio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estado estadoId = cambio.getEstadoId();
            if (estadoId != null) {
                estadoId = em.getReference(estadoId.getClass(), estadoId.getId());
                cambio.setEstadoId(estadoId);
            }
            List<SeccionCambio> attachedSeccionCambioList = new ArrayList<SeccionCambio>();
            for (SeccionCambio seccionCambioListSeccionCambioToAttach : cambio.getSeccionCambioList()) {
                seccionCambioListSeccionCambioToAttach = em.getReference(seccionCambioListSeccionCambioToAttach.getClass(), seccionCambioListSeccionCambioToAttach.getId());
                attachedSeccionCambioList.add(seccionCambioListSeccionCambioToAttach);
            }
            cambio.setSeccionCambioList(attachedSeccionCambioList);
            em.persist(cambio);
            if (estadoId != null) {
                estadoId.getCambioList().add(cambio);
                estadoId = em.merge(estadoId);
            }
            for (SeccionCambio seccionCambioListSeccionCambio : cambio.getSeccionCambioList()) {
                Cambio oldCambioIdOfSeccionCambioListSeccionCambio = seccionCambioListSeccionCambio.getCambioId();
                seccionCambioListSeccionCambio.setCambioId(cambio);
                seccionCambioListSeccionCambio = em.merge(seccionCambioListSeccionCambio);
                if (oldCambioIdOfSeccionCambioListSeccionCambio != null) {
                    oldCambioIdOfSeccionCambioListSeccionCambio.getSeccionCambioList().remove(seccionCambioListSeccionCambio);
                    oldCambioIdOfSeccionCambioListSeccionCambio = em.merge(oldCambioIdOfSeccionCambioListSeccionCambio);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cambio cambio) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cambio persistentCambio = em.find(Cambio.class, cambio.getId());
            Estado estadoIdOld = persistentCambio.getEstadoId();
            Estado estadoIdNew = cambio.getEstadoId();
            List<SeccionCambio> seccionCambioListOld = persistentCambio.getSeccionCambioList();
            List<SeccionCambio> seccionCambioListNew = cambio.getSeccionCambioList();
            List<String> illegalOrphanMessages = null;
            for (SeccionCambio seccionCambioListOldSeccionCambio : seccionCambioListOld) {
                if (!seccionCambioListNew.contains(seccionCambioListOldSeccionCambio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SeccionCambio " + seccionCambioListOldSeccionCambio + " since its cambioId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (estadoIdNew != null) {
                estadoIdNew = em.getReference(estadoIdNew.getClass(), estadoIdNew.getId());
                cambio.setEstadoId(estadoIdNew);
            }
            List<SeccionCambio> attachedSeccionCambioListNew = new ArrayList<SeccionCambio>();
            for (SeccionCambio seccionCambioListNewSeccionCambioToAttach : seccionCambioListNew) {
                seccionCambioListNewSeccionCambioToAttach = em.getReference(seccionCambioListNewSeccionCambioToAttach.getClass(), seccionCambioListNewSeccionCambioToAttach.getId());
                attachedSeccionCambioListNew.add(seccionCambioListNewSeccionCambioToAttach);
            }
            seccionCambioListNew = attachedSeccionCambioListNew;
            cambio.setSeccionCambioList(seccionCambioListNew);
            cambio = em.merge(cambio);
            if (estadoIdOld != null && !estadoIdOld.equals(estadoIdNew)) {
                estadoIdOld.getCambioList().remove(cambio);
                estadoIdOld = em.merge(estadoIdOld);
            }
            if (estadoIdNew != null && !estadoIdNew.equals(estadoIdOld)) {
                estadoIdNew.getCambioList().add(cambio);
                estadoIdNew = em.merge(estadoIdNew);
            }
            for (SeccionCambio seccionCambioListNewSeccionCambio : seccionCambioListNew) {
                if (!seccionCambioListOld.contains(seccionCambioListNewSeccionCambio)) {
                    Cambio oldCambioIdOfSeccionCambioListNewSeccionCambio = seccionCambioListNewSeccionCambio.getCambioId();
                    seccionCambioListNewSeccionCambio.setCambioId(cambio);
                    seccionCambioListNewSeccionCambio = em.merge(seccionCambioListNewSeccionCambio);
                    if (oldCambioIdOfSeccionCambioListNewSeccionCambio != null && !oldCambioIdOfSeccionCambioListNewSeccionCambio.equals(cambio)) {
                        oldCambioIdOfSeccionCambioListNewSeccionCambio.getSeccionCambioList().remove(seccionCambioListNewSeccionCambio);
                        oldCambioIdOfSeccionCambioListNewSeccionCambio = em.merge(oldCambioIdOfSeccionCambioListNewSeccionCambio);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cambio.getId();
                if (findCambio(id) == null) {
                    throw new NonexistentEntityException("The cambio with id " + id + " no longer exists.");
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
            Cambio cambio;
            try {
                cambio = em.getReference(Cambio.class, id);
                cambio.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cambio with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<SeccionCambio> seccionCambioListOrphanCheck = cambio.getSeccionCambioList();
            for (SeccionCambio seccionCambioListOrphanCheckSeccionCambio : seccionCambioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cambio (" + cambio + ") cannot be destroyed since the SeccionCambio " + seccionCambioListOrphanCheckSeccionCambio + " in its seccionCambioList field has a non-nullable cambioId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Estado estadoId = cambio.getEstadoId();
            if (estadoId != null) {
                estadoId.getCambioList().remove(cambio);
                estadoId = em.merge(estadoId);
            }
            em.remove(cambio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cambio> findCambioEntities() {
        return findCambioEntities(true, -1, -1);
    }

    public List<Cambio> findCambioEntities(int maxResults, int firstResult) {
        return findCambioEntities(false, maxResults, firstResult);
    }

    private List<Cambio> findCambioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cambio.class));
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

    public Cambio findCambio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cambio.class, id);
        } finally {
            em.close();
        }
    }

    public int getCambioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cambio> rt = cq.from(Cambio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
