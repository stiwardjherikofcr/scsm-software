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
import dto.Materia;
import dto.PrerrequisitoMateria;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Manuel
 */
public class PrerrequisitoMateriaJpaController implements Serializable {

    public PrerrequisitoMateriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PrerrequisitoMateria prerrequisitoMateria) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materia materia = prerrequisitoMateria.getMateria();
            if (materia != null) {
                materia = em.getReference(materia.getClass(), materia.getMateriaPK());
                prerrequisitoMateria.setMateria(materia);
            }
            Materia materia1 = prerrequisitoMateria.getMateria1();
            if (materia1 != null) {
                materia1 = em.getReference(materia1.getClass(), materia1.getMateriaPK());
                prerrequisitoMateria.setMateria1(materia1);
            }
            em.persist(prerrequisitoMateria);
            if (materia != null) {
                materia.getPrerrequisitoMateriaList().add(prerrequisitoMateria);
                materia = em.merge(materia);
            }
            if (materia1 != null) {
                materia1.getPrerrequisitoMateriaList().add(prerrequisitoMateria);
                materia1 = em.merge(materia1);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PrerrequisitoMateria prerrequisitoMateria) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PrerrequisitoMateria persistentPrerrequisitoMateria = em.find(PrerrequisitoMateria.class, prerrequisitoMateria.getId());
            Materia materiaOld = persistentPrerrequisitoMateria.getMateria();
            Materia materiaNew = prerrequisitoMateria.getMateria();
            Materia materia1Old = persistentPrerrequisitoMateria.getMateria1();
            Materia materia1New = prerrequisitoMateria.getMateria1();
            if (materiaNew != null) {
                materiaNew = em.getReference(materiaNew.getClass(), materiaNew.getMateriaPK());
                prerrequisitoMateria.setMateria(materiaNew);
            }
            if (materia1New != null) {
                materia1New = em.getReference(materia1New.getClass(), materia1New.getMateriaPK());
                prerrequisitoMateria.setMateria1(materia1New);
            }
            prerrequisitoMateria = em.merge(prerrequisitoMateria);
            if (materiaOld != null && !materiaOld.equals(materiaNew)) {
                materiaOld.getPrerrequisitoMateriaList().remove(prerrequisitoMateria);
                materiaOld = em.merge(materiaOld);
            }
            if (materiaNew != null && !materiaNew.equals(materiaOld)) {
                materiaNew.getPrerrequisitoMateriaList().add(prerrequisitoMateria);
                materiaNew = em.merge(materiaNew);
            }
            if (materia1Old != null && !materia1Old.equals(materia1New)) {
                materia1Old.getPrerrequisitoMateriaList().remove(prerrequisitoMateria);
                materia1Old = em.merge(materia1Old);
            }
            if (materia1New != null && !materia1New.equals(materia1Old)) {
                materia1New.getPrerrequisitoMateriaList().add(prerrequisitoMateria);
                materia1New = em.merge(materia1New);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = prerrequisitoMateria.getId();
                if (findPrerrequisitoMateria(id) == null) {
                    throw new NonexistentEntityException("The prerrequisitoMateria with id " + id + " no longer exists.");
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
            PrerrequisitoMateria prerrequisitoMateria;
            try {
                prerrequisitoMateria = em.getReference(PrerrequisitoMateria.class, id);
                prerrequisitoMateria.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The prerrequisitoMateria with id " + id + " no longer exists.", enfe);
            }
            Materia materia = prerrequisitoMateria.getMateria();
            if (materia != null) {
                materia.getPrerrequisitoMateriaList().remove(prerrequisitoMateria);
                materia = em.merge(materia);
            }
            Materia materia1 = prerrequisitoMateria.getMateria1();
            if (materia1 != null) {
                materia1.getPrerrequisitoMateriaList().remove(prerrequisitoMateria);
                materia1 = em.merge(materia1);
            }
            em.remove(prerrequisitoMateria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PrerrequisitoMateria> findPrerrequisitoMateriaEntities() {
        return findPrerrequisitoMateriaEntities(true, -1, -1);
    }

    public List<PrerrequisitoMateria> findPrerrequisitoMateriaEntities(int maxResults, int firstResult) {
        return findPrerrequisitoMateriaEntities(false, maxResults, firstResult);
    }

    private List<PrerrequisitoMateria> findPrerrequisitoMateriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PrerrequisitoMateria.class));
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

    public PrerrequisitoMateria findPrerrequisitoMateria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PrerrequisitoMateria.class, id);
        } finally {
            em.close();
        }
    }

    public int getPrerrequisitoMateriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PrerrequisitoMateria> rt = cq.from(PrerrequisitoMateria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
