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
import dto.Cambio;
import dto.Estado;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Manuel
 */
public class EstadoJpaController implements Serializable {

    public EstadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estado estado) {
        if (estado.getCambioList() == null) {
            estado.setCambioList(new ArrayList<Cambio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Cambio> attachedCambioList = new ArrayList<Cambio>();
            for (Cambio cambioListCambioToAttach : estado.getCambioList()) {
                cambioListCambioToAttach = em.getReference(cambioListCambioToAttach.getClass(), cambioListCambioToAttach.getId());
                attachedCambioList.add(cambioListCambioToAttach);
            }
            estado.setCambioList(attachedCambioList);
            em.persist(estado);
            for (Cambio cambioListCambio : estado.getCambioList()) {
                Estado oldEstadoIdOfCambioListCambio = cambioListCambio.getEstadoId();
                cambioListCambio.setEstadoId(estado);
                cambioListCambio = em.merge(cambioListCambio);
                if (oldEstadoIdOfCambioListCambio != null) {
                    oldEstadoIdOfCambioListCambio.getCambioList().remove(cambioListCambio);
                    oldEstadoIdOfCambioListCambio = em.merge(oldEstadoIdOfCambioListCambio);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estado estado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estado persistentEstado = em.find(Estado.class, estado.getId());
            List<Cambio> cambioListOld = persistentEstado.getCambioList();
            List<Cambio> cambioListNew = estado.getCambioList();
            List<String> illegalOrphanMessages = null;
            for (Cambio cambioListOldCambio : cambioListOld) {
                if (!cambioListNew.contains(cambioListOldCambio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cambio " + cambioListOldCambio + " since its estadoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Cambio> attachedCambioListNew = new ArrayList<Cambio>();
            for (Cambio cambioListNewCambioToAttach : cambioListNew) {
                cambioListNewCambioToAttach = em.getReference(cambioListNewCambioToAttach.getClass(), cambioListNewCambioToAttach.getId());
                attachedCambioListNew.add(cambioListNewCambioToAttach);
            }
            cambioListNew = attachedCambioListNew;
            estado.setCambioList(cambioListNew);
            estado = em.merge(estado);
            for (Cambio cambioListNewCambio : cambioListNew) {
                if (!cambioListOld.contains(cambioListNewCambio)) {
                    Estado oldEstadoIdOfCambioListNewCambio = cambioListNewCambio.getEstadoId();
                    cambioListNewCambio.setEstadoId(estado);
                    cambioListNewCambio = em.merge(cambioListNewCambio);
                    if (oldEstadoIdOfCambioListNewCambio != null && !oldEstadoIdOfCambioListNewCambio.equals(estado)) {
                        oldEstadoIdOfCambioListNewCambio.getCambioList().remove(cambioListNewCambio);
                        oldEstadoIdOfCambioListNewCambio = em.merge(oldEstadoIdOfCambioListNewCambio);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = estado.getId();
                if (findEstado(id) == null) {
                    throw new NonexistentEntityException("The estado with id " + id + " no longer exists.");
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
            Estado estado;
            try {
                estado = em.getReference(Estado.class, id);
                estado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Cambio> cambioListOrphanCheck = estado.getCambioList();
            for (Cambio cambioListOrphanCheckCambio : cambioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estado (" + estado + ") cannot be destroyed since the Cambio " + cambioListOrphanCheckCambio + " in its cambioList field has a non-nullable estadoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Estado> findEstadoEntities() {
        return findEstadoEntities(true, -1, -1);
    }

    public List<Estado> findEstadoEntities(int maxResults, int firstResult) {
        return findEstadoEntities(false, maxResults, firstResult);
    }

    private List<Estado> findEstadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estado.class));
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

    public Estado findEstado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estado> rt = cq.from(Estado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
