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
import dto.Seccion;
import dto.TipoSeccion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Manuel
 */
public class TipoSeccionJpaController implements Serializable {

    public TipoSeccionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoSeccion tipoSeccion) {
        if (tipoSeccion.getSeccionList() == null) {
            tipoSeccion.setSeccionList(new ArrayList<Seccion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Seccion> attachedSeccionList = new ArrayList<Seccion>();
            for (Seccion seccionListSeccionToAttach : tipoSeccion.getSeccionList()) {
                seccionListSeccionToAttach = em.getReference(seccionListSeccionToAttach.getClass(), seccionListSeccionToAttach.getId());
                attachedSeccionList.add(seccionListSeccionToAttach);
            }
            tipoSeccion.setSeccionList(attachedSeccionList);
            em.persist(tipoSeccion);
            for (Seccion seccionListSeccion : tipoSeccion.getSeccionList()) {
                TipoSeccion oldTipoSeccionIdOfSeccionListSeccion = seccionListSeccion.getTipoSeccionId();
                seccionListSeccion.setTipoSeccionId(tipoSeccion);
                seccionListSeccion = em.merge(seccionListSeccion);
                if (oldTipoSeccionIdOfSeccionListSeccion != null) {
                    oldTipoSeccionIdOfSeccionListSeccion.getSeccionList().remove(seccionListSeccion);
                    oldTipoSeccionIdOfSeccionListSeccion = em.merge(oldTipoSeccionIdOfSeccionListSeccion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoSeccion tipoSeccion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoSeccion persistentTipoSeccion = em.find(TipoSeccion.class, tipoSeccion.getId());
            List<Seccion> seccionListOld = persistentTipoSeccion.getSeccionList();
            List<Seccion> seccionListNew = tipoSeccion.getSeccionList();
            List<String> illegalOrphanMessages = null;
            for (Seccion seccionListOldSeccion : seccionListOld) {
                if (!seccionListNew.contains(seccionListOldSeccion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Seccion " + seccionListOldSeccion + " since its tipoSeccionId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Seccion> attachedSeccionListNew = new ArrayList<Seccion>();
            for (Seccion seccionListNewSeccionToAttach : seccionListNew) {
                seccionListNewSeccionToAttach = em.getReference(seccionListNewSeccionToAttach.getClass(), seccionListNewSeccionToAttach.getId());
                attachedSeccionListNew.add(seccionListNewSeccionToAttach);
            }
            seccionListNew = attachedSeccionListNew;
            tipoSeccion.setSeccionList(seccionListNew);
            tipoSeccion = em.merge(tipoSeccion);
            for (Seccion seccionListNewSeccion : seccionListNew) {
                if (!seccionListOld.contains(seccionListNewSeccion)) {
                    TipoSeccion oldTipoSeccionIdOfSeccionListNewSeccion = seccionListNewSeccion.getTipoSeccionId();
                    seccionListNewSeccion.setTipoSeccionId(tipoSeccion);
                    seccionListNewSeccion = em.merge(seccionListNewSeccion);
                    if (oldTipoSeccionIdOfSeccionListNewSeccion != null && !oldTipoSeccionIdOfSeccionListNewSeccion.equals(tipoSeccion)) {
                        oldTipoSeccionIdOfSeccionListNewSeccion.getSeccionList().remove(seccionListNewSeccion);
                        oldTipoSeccionIdOfSeccionListNewSeccion = em.merge(oldTipoSeccionIdOfSeccionListNewSeccion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoSeccion.getId();
                if (findTipoSeccion(id) == null) {
                    throw new NonexistentEntityException("The tipoSeccion with id " + id + " no longer exists.");
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
            TipoSeccion tipoSeccion;
            try {
                tipoSeccion = em.getReference(TipoSeccion.class, id);
                tipoSeccion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoSeccion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Seccion> seccionListOrphanCheck = tipoSeccion.getSeccionList();
            for (Seccion seccionListOrphanCheckSeccion : seccionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoSeccion (" + tipoSeccion + ") cannot be destroyed since the Seccion " + seccionListOrphanCheckSeccion + " in its seccionList field has a non-nullable tipoSeccionId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoSeccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoSeccion> findTipoSeccionEntities() {
        return findTipoSeccionEntities(true, -1, -1);
    }

    public List<TipoSeccion> findTipoSeccionEntities(int maxResults, int firstResult) {
        return findTipoSeccionEntities(false, maxResults, firstResult);
    }

    private List<TipoSeccion> findTipoSeccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoSeccion.class));
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

    public TipoSeccion findTipoSeccion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoSeccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoSeccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoSeccion> rt = cq.from(TipoSeccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
