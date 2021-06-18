/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.Programa;
import dto.Materia;
import dto.Pensum;
import dto.PensumPK;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Manuel
 */
public class PensumJpaController implements Serializable {

    public PensumJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pensum pensum) throws PreexistingEntityException, Exception {
        if (pensum.getPensumPK() == null) {
            pensum.setPensumPK(new PensumPK());
        }
        if (pensum.getMateriaList() == null) {
            pensum.setMateriaList(new ArrayList<Materia>());
        }
        pensum.getPensumPK().setProgramaCodigo(pensum.getPrograma().getCodigo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Programa programa = pensum.getPrograma();
            if (programa != null) {
                programa = em.getReference(programa.getClass(), programa.getCodigo());
                pensum.setPrograma(programa);
            }
            List<Materia> attachedMateriaList = new ArrayList<Materia>();
            for (Materia materiaListMateriaToAttach : pensum.getMateriaList()) {
                materiaListMateriaToAttach = em.getReference(materiaListMateriaToAttach.getClass(), materiaListMateriaToAttach.getMateriaPK());
                attachedMateriaList.add(materiaListMateriaToAttach);
            }
            pensum.setMateriaList(attachedMateriaList);
            em.persist(pensum);
            if (programa != null) {
                programa.getPensumList().add(pensum);
                programa = em.merge(programa);
            }
            for (Materia materiaListMateria : pensum.getMateriaList()) {
                Pensum oldPensumOfMateriaListMateria = materiaListMateria.getPensum();
                materiaListMateria.setPensum(pensum);
                materiaListMateria = em.merge(materiaListMateria);
                if (oldPensumOfMateriaListMateria != null) {
                    oldPensumOfMateriaListMateria.getMateriaList().remove(materiaListMateria);
                    oldPensumOfMateriaListMateria = em.merge(oldPensumOfMateriaListMateria);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPensum(pensum.getPensumPK()) != null) {
                throw new PreexistingEntityException("Pensum " + pensum + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pensum pensum) throws IllegalOrphanException, NonexistentEntityException, Exception {
        pensum.getPensumPK().setProgramaCodigo(pensum.getPrograma().getCodigo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pensum persistentPensum = em.find(Pensum.class, pensum.getPensumPK());
            Programa programaOld = persistentPensum.getPrograma();
            Programa programaNew = pensum.getPrograma();
            List<Materia> materiaListOld = persistentPensum.getMateriaList();
            List<Materia> materiaListNew = pensum.getMateriaList();
            List<String> illegalOrphanMessages = null;
            for (Materia materiaListOldMateria : materiaListOld) {
                if (!materiaListNew.contains(materiaListOldMateria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Materia " + materiaListOldMateria + " since its pensum field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (programaNew != null) {
                programaNew = em.getReference(programaNew.getClass(), programaNew.getCodigo());
                pensum.setPrograma(programaNew);
            }
            List<Materia> attachedMateriaListNew = new ArrayList<Materia>();
            for (Materia materiaListNewMateriaToAttach : materiaListNew) {
                materiaListNewMateriaToAttach = em.getReference(materiaListNewMateriaToAttach.getClass(), materiaListNewMateriaToAttach.getMateriaPK());
                attachedMateriaListNew.add(materiaListNewMateriaToAttach);
            }
            materiaListNew = attachedMateriaListNew;
            pensum.setMateriaList(materiaListNew);
            pensum = em.merge(pensum);
            if (programaOld != null && !programaOld.equals(programaNew)) {
                programaOld.getPensumList().remove(pensum);
                programaOld = em.merge(programaOld);
            }
            if (programaNew != null && !programaNew.equals(programaOld)) {
                programaNew.getPensumList().add(pensum);
                programaNew = em.merge(programaNew);
            }
            for (Materia materiaListNewMateria : materiaListNew) {
                if (!materiaListOld.contains(materiaListNewMateria)) {
                    Pensum oldPensumOfMateriaListNewMateria = materiaListNewMateria.getPensum();
                    materiaListNewMateria.setPensum(pensum);
                    materiaListNewMateria = em.merge(materiaListNewMateria);
                    if (oldPensumOfMateriaListNewMateria != null && !oldPensumOfMateriaListNewMateria.equals(pensum)) {
                        oldPensumOfMateriaListNewMateria.getMateriaList().remove(materiaListNewMateria);
                        oldPensumOfMateriaListNewMateria = em.merge(oldPensumOfMateriaListNewMateria);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                PensumPK id = pensum.getPensumPK();
                if (findPensum(id) == null) {
                    throw new NonexistentEntityException("The pensum with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(PensumPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pensum pensum;
            try {
                pensum = em.getReference(Pensum.class, id);
                pensum.getPensumPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pensum with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Materia> materiaListOrphanCheck = pensum.getMateriaList();
            for (Materia materiaListOrphanCheckMateria : materiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pensum (" + pensum + ") cannot be destroyed since the Materia " + materiaListOrphanCheckMateria + " in its materiaList field has a non-nullable pensum field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Programa programa = pensum.getPrograma();
            if (programa != null) {
                programa.getPensumList().remove(pensum);
                programa = em.merge(programa);
            }
            em.remove(pensum);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pensum> findPensumEntities() {
        return findPensumEntities(true, -1, -1);
    }

    public List<Pensum> findPensumEntities(int maxResults, int firstResult) {
        return findPensumEntities(false, maxResults, firstResult);
    }

    private List<Pensum> findPensumEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pensum.class));
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

    public Pensum findPensum(PensumPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pensum.class, id);
        } finally {
            em.close();
        }
    }

    public int getPensumCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pensum> rt = cq.from(Pensum.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
