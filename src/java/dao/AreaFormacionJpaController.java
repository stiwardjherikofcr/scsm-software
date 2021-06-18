/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dto.AreaFormacion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.Microcurriculo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Manuel
 */
public class AreaFormacionJpaController implements Serializable {

    public AreaFormacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AreaFormacion areaFormacion) {
        if (areaFormacion.getMicrocurriculoList() == null) {
            areaFormacion.setMicrocurriculoList(new ArrayList<Microcurriculo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Microcurriculo> attachedMicrocurriculoList = new ArrayList<Microcurriculo>();
            for (Microcurriculo microcurriculoListMicrocurriculoToAttach : areaFormacion.getMicrocurriculoList()) {
                microcurriculoListMicrocurriculoToAttach = em.getReference(microcurriculoListMicrocurriculoToAttach.getClass(), microcurriculoListMicrocurriculoToAttach.getMicrocurriculoPK());
                attachedMicrocurriculoList.add(microcurriculoListMicrocurriculoToAttach);
            }
            areaFormacion.setMicrocurriculoList(attachedMicrocurriculoList);
            em.persist(areaFormacion);
            for (Microcurriculo microcurriculoListMicrocurriculo : areaFormacion.getMicrocurriculoList()) {
                AreaFormacion oldAreaDeFormacionIdOfMicrocurriculoListMicrocurriculo = microcurriculoListMicrocurriculo.getAreaDeFormacionId();
                microcurriculoListMicrocurriculo.setAreaDeFormacionId(areaFormacion);
                microcurriculoListMicrocurriculo = em.merge(microcurriculoListMicrocurriculo);
                if (oldAreaDeFormacionIdOfMicrocurriculoListMicrocurriculo != null) {
                    oldAreaDeFormacionIdOfMicrocurriculoListMicrocurriculo.getMicrocurriculoList().remove(microcurriculoListMicrocurriculo);
                    oldAreaDeFormacionIdOfMicrocurriculoListMicrocurriculo = em.merge(oldAreaDeFormacionIdOfMicrocurriculoListMicrocurriculo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AreaFormacion areaFormacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AreaFormacion persistentAreaFormacion = em.find(AreaFormacion.class, areaFormacion.getId());
            List<Microcurriculo> microcurriculoListOld = persistentAreaFormacion.getMicrocurriculoList();
            List<Microcurriculo> microcurriculoListNew = areaFormacion.getMicrocurriculoList();
            List<Microcurriculo> attachedMicrocurriculoListNew = new ArrayList<Microcurriculo>();
            for (Microcurriculo microcurriculoListNewMicrocurriculoToAttach : microcurriculoListNew) {
                microcurriculoListNewMicrocurriculoToAttach = em.getReference(microcurriculoListNewMicrocurriculoToAttach.getClass(), microcurriculoListNewMicrocurriculoToAttach.getMicrocurriculoPK());
                attachedMicrocurriculoListNew.add(microcurriculoListNewMicrocurriculoToAttach);
            }
            microcurriculoListNew = attachedMicrocurriculoListNew;
            areaFormacion.setMicrocurriculoList(microcurriculoListNew);
            areaFormacion = em.merge(areaFormacion);
            for (Microcurriculo microcurriculoListOldMicrocurriculo : microcurriculoListOld) {
                if (!microcurriculoListNew.contains(microcurriculoListOldMicrocurriculo)) {
                    microcurriculoListOldMicrocurriculo.setAreaDeFormacionId(null);
                    microcurriculoListOldMicrocurriculo = em.merge(microcurriculoListOldMicrocurriculo);
                }
            }
            for (Microcurriculo microcurriculoListNewMicrocurriculo : microcurriculoListNew) {
                if (!microcurriculoListOld.contains(microcurriculoListNewMicrocurriculo)) {
                    AreaFormacion oldAreaDeFormacionIdOfMicrocurriculoListNewMicrocurriculo = microcurriculoListNewMicrocurriculo.getAreaDeFormacionId();
                    microcurriculoListNewMicrocurriculo.setAreaDeFormacionId(areaFormacion);
                    microcurriculoListNewMicrocurriculo = em.merge(microcurriculoListNewMicrocurriculo);
                    if (oldAreaDeFormacionIdOfMicrocurriculoListNewMicrocurriculo != null && !oldAreaDeFormacionIdOfMicrocurriculoListNewMicrocurriculo.equals(areaFormacion)) {
                        oldAreaDeFormacionIdOfMicrocurriculoListNewMicrocurriculo.getMicrocurriculoList().remove(microcurriculoListNewMicrocurriculo);
                        oldAreaDeFormacionIdOfMicrocurriculoListNewMicrocurriculo = em.merge(oldAreaDeFormacionIdOfMicrocurriculoListNewMicrocurriculo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = areaFormacion.getId();
                if (findAreaFormacion(id) == null) {
                    throw new NonexistentEntityException("The areaFormacion with id " + id + " no longer exists.");
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
            AreaFormacion areaFormacion;
            try {
                areaFormacion = em.getReference(AreaFormacion.class, id);
                areaFormacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The areaFormacion with id " + id + " no longer exists.", enfe);
            }
            List<Microcurriculo> microcurriculoList = areaFormacion.getMicrocurriculoList();
            for (Microcurriculo microcurriculoListMicrocurriculo : microcurriculoList) {
                microcurriculoListMicrocurriculo.setAreaDeFormacionId(null);
                microcurriculoListMicrocurriculo = em.merge(microcurriculoListMicrocurriculo);
            }
            em.remove(areaFormacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AreaFormacion> findAreaFormacionEntities() {
        return findAreaFormacionEntities(true, -1, -1);
    }

    public List<AreaFormacion> findAreaFormacionEntities(int maxResults, int firstResult) {
        return findAreaFormacionEntities(false, maxResults, firstResult);
    }

    private List<AreaFormacion> findAreaFormacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AreaFormacion.class));
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

    public AreaFormacion findAreaFormacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AreaFormacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getAreaFormacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AreaFormacion> rt = cq.from(AreaFormacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
