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
import dto.Departamento;
import dto.Facultad;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Manuel
 */
public class FacultadJpaController implements Serializable {

    public FacultadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Facultad facultad) {
        if (facultad.getDepartamentoList() == null) {
            facultad.setDepartamentoList(new ArrayList<Departamento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Departamento> attachedDepartamentoList = new ArrayList<Departamento>();
            for (Departamento departamentoListDepartamentoToAttach : facultad.getDepartamentoList()) {
                departamentoListDepartamentoToAttach = em.getReference(departamentoListDepartamentoToAttach.getClass(), departamentoListDepartamentoToAttach.getId());
                attachedDepartamentoList.add(departamentoListDepartamentoToAttach);
            }
            facultad.setDepartamentoList(attachedDepartamentoList);
            em.persist(facultad);
            for (Departamento departamentoListDepartamento : facultad.getDepartamentoList()) {
                Facultad oldFacultadIdOfDepartamentoListDepartamento = departamentoListDepartamento.getFacultadId();
                departamentoListDepartamento.setFacultadId(facultad);
                departamentoListDepartamento = em.merge(departamentoListDepartamento);
                if (oldFacultadIdOfDepartamentoListDepartamento != null) {
                    oldFacultadIdOfDepartamentoListDepartamento.getDepartamentoList().remove(departamentoListDepartamento);
                    oldFacultadIdOfDepartamentoListDepartamento = em.merge(oldFacultadIdOfDepartamentoListDepartamento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Facultad facultad) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Facultad persistentFacultad = em.find(Facultad.class, facultad.getId());
            List<Departamento> departamentoListOld = persistentFacultad.getDepartamentoList();
            List<Departamento> departamentoListNew = facultad.getDepartamentoList();
            List<String> illegalOrphanMessages = null;
            for (Departamento departamentoListOldDepartamento : departamentoListOld) {
                if (!departamentoListNew.contains(departamentoListOldDepartamento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Departamento " + departamentoListOldDepartamento + " since its facultadId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Departamento> attachedDepartamentoListNew = new ArrayList<Departamento>();
            for (Departamento departamentoListNewDepartamentoToAttach : departamentoListNew) {
                departamentoListNewDepartamentoToAttach = em.getReference(departamentoListNewDepartamentoToAttach.getClass(), departamentoListNewDepartamentoToAttach.getId());
                attachedDepartamentoListNew.add(departamentoListNewDepartamentoToAttach);
            }
            departamentoListNew = attachedDepartamentoListNew;
            facultad.setDepartamentoList(departamentoListNew);
            facultad = em.merge(facultad);
            for (Departamento departamentoListNewDepartamento : departamentoListNew) {
                if (!departamentoListOld.contains(departamentoListNewDepartamento)) {
                    Facultad oldFacultadIdOfDepartamentoListNewDepartamento = departamentoListNewDepartamento.getFacultadId();
                    departamentoListNewDepartamento.setFacultadId(facultad);
                    departamentoListNewDepartamento = em.merge(departamentoListNewDepartamento);
                    if (oldFacultadIdOfDepartamentoListNewDepartamento != null && !oldFacultadIdOfDepartamentoListNewDepartamento.equals(facultad)) {
                        oldFacultadIdOfDepartamentoListNewDepartamento.getDepartamentoList().remove(departamentoListNewDepartamento);
                        oldFacultadIdOfDepartamentoListNewDepartamento = em.merge(oldFacultadIdOfDepartamentoListNewDepartamento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = facultad.getId();
                if (findFacultad(id) == null) {
                    throw new NonexistentEntityException("The facultad with id " + id + " no longer exists.");
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
            Facultad facultad;
            try {
                facultad = em.getReference(Facultad.class, id);
                facultad.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The facultad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Departamento> departamentoListOrphanCheck = facultad.getDepartamentoList();
            for (Departamento departamentoListOrphanCheckDepartamento : departamentoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Facultad (" + facultad + ") cannot be destroyed since the Departamento " + departamentoListOrphanCheckDepartamento + " in its departamentoList field has a non-nullable facultadId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(facultad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Facultad> findFacultadEntities() {
        return findFacultadEntities(true, -1, -1);
    }

    public List<Facultad> findFacultadEntities(int maxResults, int firstResult) {
        return findFacultadEntities(false, maxResults, firstResult);
    }

    private List<Facultad> findFacultadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Facultad.class));
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

    public Facultad findFacultad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Facultad.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacultadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Facultad> rt = cq.from(Facultad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
