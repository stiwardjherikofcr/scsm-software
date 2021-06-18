/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dto.EquivalenciaMateria;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.Materia;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Manuel
 */
public class EquivalenciaMateriaJpaController implements Serializable {

    public EquivalenciaMateriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EquivalenciaMateria equivalenciaMateria) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materia materia = equivalenciaMateria.getMateria();
            if (materia != null) {
                materia = em.getReference(materia.getClass(), materia.getMateriaPK());
                equivalenciaMateria.setMateria(materia);
            }
            em.persist(equivalenciaMateria);
            if (materia != null) {
                materia.getEquivalenciaMateriaList().add(equivalenciaMateria);
                materia = em.merge(materia);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EquivalenciaMateria equivalenciaMateria) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EquivalenciaMateria persistentEquivalenciaMateria = em.find(EquivalenciaMateria.class, equivalenciaMateria.getId());
            Materia materiaOld = persistentEquivalenciaMateria.getMateria();
            Materia materiaNew = equivalenciaMateria.getMateria();
            if (materiaNew != null) {
                materiaNew = em.getReference(materiaNew.getClass(), materiaNew.getMateriaPK());
                equivalenciaMateria.setMateria(materiaNew);
            }
            equivalenciaMateria = em.merge(equivalenciaMateria);
            if (materiaOld != null && !materiaOld.equals(materiaNew)) {
                materiaOld.getEquivalenciaMateriaList().remove(equivalenciaMateria);
                materiaOld = em.merge(materiaOld);
            }
            if (materiaNew != null && !materiaNew.equals(materiaOld)) {
                materiaNew.getEquivalenciaMateriaList().add(equivalenciaMateria);
                materiaNew = em.merge(materiaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = equivalenciaMateria.getId();
                if (findEquivalenciaMateria(id) == null) {
                    throw new NonexistentEntityException("The equivalenciaMateria with id " + id + " no longer exists.");
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
            EquivalenciaMateria equivalenciaMateria;
            try {
                equivalenciaMateria = em.getReference(EquivalenciaMateria.class, id);
                equivalenciaMateria.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The equivalenciaMateria with id " + id + " no longer exists.", enfe);
            }
            Materia materia = equivalenciaMateria.getMateria();
            if (materia != null) {
                materia.getEquivalenciaMateriaList().remove(equivalenciaMateria);
                materia = em.merge(materia);
            }
            em.remove(equivalenciaMateria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EquivalenciaMateria> findEquivalenciaMateriaEntities() {
        return findEquivalenciaMateriaEntities(true, -1, -1);
    }

    public List<EquivalenciaMateria> findEquivalenciaMateriaEntities(int maxResults, int firstResult) {
        return findEquivalenciaMateriaEntities(false, maxResults, firstResult);
    }

    private List<EquivalenciaMateria> findEquivalenciaMateriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EquivalenciaMateria.class));
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

    public EquivalenciaMateria findEquivalenciaMateria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EquivalenciaMateria.class, id);
        } finally {
            em.close();
        }
    }

    public int getEquivalenciaMateriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EquivalenciaMateria> rt = cq.from(EquivalenciaMateria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
