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
import dto.Materia;
import dto.TipoAsignatura;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Manuel
 */
public class TipoAsignaturaJpaController implements Serializable {

    public TipoAsignaturaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoAsignatura tipoAsignatura) {
        if (tipoAsignatura.getMateriaList() == null) {
            tipoAsignatura.setMateriaList(new ArrayList<Materia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Materia> attachedMateriaList = new ArrayList<Materia>();
            for (Materia materiaListMateriaToAttach : tipoAsignatura.getMateriaList()) {
                materiaListMateriaToAttach = em.getReference(materiaListMateriaToAttach.getClass(), materiaListMateriaToAttach.getMateriaPK());
                attachedMateriaList.add(materiaListMateriaToAttach);
            }
            tipoAsignatura.setMateriaList(attachedMateriaList);
            em.persist(tipoAsignatura);
            for (Materia materiaListMateria : tipoAsignatura.getMateriaList()) {
                TipoAsignatura oldTipoAsignaturaIdOfMateriaListMateria = materiaListMateria.getTipoAsignaturaId();
                materiaListMateria.setTipoAsignaturaId(tipoAsignatura);
                materiaListMateria = em.merge(materiaListMateria);
                if (oldTipoAsignaturaIdOfMateriaListMateria != null) {
                    oldTipoAsignaturaIdOfMateriaListMateria.getMateriaList().remove(materiaListMateria);
                    oldTipoAsignaturaIdOfMateriaListMateria = em.merge(oldTipoAsignaturaIdOfMateriaListMateria);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoAsignatura tipoAsignatura) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoAsignatura persistentTipoAsignatura = em.find(TipoAsignatura.class, tipoAsignatura.getId());
            List<Materia> materiaListOld = persistentTipoAsignatura.getMateriaList();
            List<Materia> materiaListNew = tipoAsignatura.getMateriaList();
            List<String> illegalOrphanMessages = null;
            for (Materia materiaListOldMateria : materiaListOld) {
                if (!materiaListNew.contains(materiaListOldMateria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Materia " + materiaListOldMateria + " since its tipoAsignaturaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Materia> attachedMateriaListNew = new ArrayList<Materia>();
            for (Materia materiaListNewMateriaToAttach : materiaListNew) {
                materiaListNewMateriaToAttach = em.getReference(materiaListNewMateriaToAttach.getClass(), materiaListNewMateriaToAttach.getMateriaPK());
                attachedMateriaListNew.add(materiaListNewMateriaToAttach);
            }
            materiaListNew = attachedMateriaListNew;
            tipoAsignatura.setMateriaList(materiaListNew);
            tipoAsignatura = em.merge(tipoAsignatura);
            for (Materia materiaListNewMateria : materiaListNew) {
                if (!materiaListOld.contains(materiaListNewMateria)) {
                    TipoAsignatura oldTipoAsignaturaIdOfMateriaListNewMateria = materiaListNewMateria.getTipoAsignaturaId();
                    materiaListNewMateria.setTipoAsignaturaId(tipoAsignatura);
                    materiaListNewMateria = em.merge(materiaListNewMateria);
                    if (oldTipoAsignaturaIdOfMateriaListNewMateria != null && !oldTipoAsignaturaIdOfMateriaListNewMateria.equals(tipoAsignatura)) {
                        oldTipoAsignaturaIdOfMateriaListNewMateria.getMateriaList().remove(materiaListNewMateria);
                        oldTipoAsignaturaIdOfMateriaListNewMateria = em.merge(oldTipoAsignaturaIdOfMateriaListNewMateria);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoAsignatura.getId();
                if (findTipoAsignatura(id) == null) {
                    throw new NonexistentEntityException("The tipoAsignatura with id " + id + " no longer exists.");
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
            TipoAsignatura tipoAsignatura;
            try {
                tipoAsignatura = em.getReference(TipoAsignatura.class, id);
                tipoAsignatura.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoAsignatura with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Materia> materiaListOrphanCheck = tipoAsignatura.getMateriaList();
            for (Materia materiaListOrphanCheckMateria : materiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoAsignatura (" + tipoAsignatura + ") cannot be destroyed since the Materia " + materiaListOrphanCheckMateria + " in its materiaList field has a non-nullable tipoAsignaturaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoAsignatura);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoAsignatura> findTipoAsignaturaEntities() {
        return findTipoAsignaturaEntities(true, -1, -1);
    }

    public List<TipoAsignatura> findTipoAsignaturaEntities(int maxResults, int firstResult) {
        return findTipoAsignaturaEntities(false, maxResults, firstResult);
    }

    private List<TipoAsignatura> findTipoAsignaturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoAsignatura.class));
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

    public TipoAsignatura findTipoAsignatura(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoAsignatura.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoAsignaturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoAsignatura> rt = cq.from(TipoAsignatura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
