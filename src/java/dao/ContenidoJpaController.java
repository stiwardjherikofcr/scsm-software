/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dto.Contenido;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.SeccionMicrocurriculo;
import dto.TablaMicrocurriculoInfo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Manuel
 */
public class ContenidoJpaController implements Serializable {

    public ContenidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Contenido contenido) {
        if (contenido.getTablaMicrocurriculoInfoList() == null) {
            contenido.setTablaMicrocurriculoInfoList(new ArrayList<TablaMicrocurriculoInfo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SeccionMicrocurriculo seccionMicrocurriculoId = contenido.getSeccionMicrocurriculoId();
            if (seccionMicrocurriculoId != null) {
                seccionMicrocurriculoId = em.getReference(seccionMicrocurriculoId.getClass(), seccionMicrocurriculoId.getId());
                contenido.setSeccionMicrocurriculoId(seccionMicrocurriculoId);
            }
            List<TablaMicrocurriculoInfo> attachedTablaMicrocurriculoInfoList = new ArrayList<TablaMicrocurriculoInfo>();
            for (TablaMicrocurriculoInfo tablaMicrocurriculoInfoListTablaMicrocurriculoInfoToAttach : contenido.getTablaMicrocurriculoInfoList()) {
                tablaMicrocurriculoInfoListTablaMicrocurriculoInfoToAttach = em.getReference(tablaMicrocurriculoInfoListTablaMicrocurriculoInfoToAttach.getClass(), tablaMicrocurriculoInfoListTablaMicrocurriculoInfoToAttach.getTablaMicrocurriculoInfoPK());
                attachedTablaMicrocurriculoInfoList.add(tablaMicrocurriculoInfoListTablaMicrocurriculoInfoToAttach);
            }
            contenido.setTablaMicrocurriculoInfoList(attachedTablaMicrocurriculoInfoList);
            em.persist(contenido);
            if (seccionMicrocurriculoId != null) {
                seccionMicrocurriculoId.getContenidoList().add(contenido);
                seccionMicrocurriculoId = em.merge(seccionMicrocurriculoId);
            }
            for (TablaMicrocurriculoInfo tablaMicrocurriculoInfoListTablaMicrocurriculoInfo : contenido.getTablaMicrocurriculoInfoList()) {
                Contenido oldContenidoIdOfTablaMicrocurriculoInfoListTablaMicrocurriculoInfo = tablaMicrocurriculoInfoListTablaMicrocurriculoInfo.getContenidoId();
                tablaMicrocurriculoInfoListTablaMicrocurriculoInfo.setContenidoId(contenido);
                tablaMicrocurriculoInfoListTablaMicrocurriculoInfo = em.merge(tablaMicrocurriculoInfoListTablaMicrocurriculoInfo);
                if (oldContenidoIdOfTablaMicrocurriculoInfoListTablaMicrocurriculoInfo != null) {
                    oldContenidoIdOfTablaMicrocurriculoInfoListTablaMicrocurriculoInfo.getTablaMicrocurriculoInfoList().remove(tablaMicrocurriculoInfoListTablaMicrocurriculoInfo);
                    oldContenidoIdOfTablaMicrocurriculoInfoListTablaMicrocurriculoInfo = em.merge(oldContenidoIdOfTablaMicrocurriculoInfoListTablaMicrocurriculoInfo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Contenido contenido) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contenido persistentContenido = em.find(Contenido.class, contenido.getId());
            SeccionMicrocurriculo seccionMicrocurriculoIdOld = persistentContenido.getSeccionMicrocurriculoId();
            SeccionMicrocurriculo seccionMicrocurriculoIdNew = contenido.getSeccionMicrocurriculoId();
            List<TablaMicrocurriculoInfo> tablaMicrocurriculoInfoListOld = persistentContenido.getTablaMicrocurriculoInfoList();
            List<TablaMicrocurriculoInfo> tablaMicrocurriculoInfoListNew = contenido.getTablaMicrocurriculoInfoList();
            List<String> illegalOrphanMessages = null;
            for (TablaMicrocurriculoInfo tablaMicrocurriculoInfoListOldTablaMicrocurriculoInfo : tablaMicrocurriculoInfoListOld) {
                if (!tablaMicrocurriculoInfoListNew.contains(tablaMicrocurriculoInfoListOldTablaMicrocurriculoInfo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TablaMicrocurriculoInfo " + tablaMicrocurriculoInfoListOldTablaMicrocurriculoInfo + " since its contenidoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (seccionMicrocurriculoIdNew != null) {
                seccionMicrocurriculoIdNew = em.getReference(seccionMicrocurriculoIdNew.getClass(), seccionMicrocurriculoIdNew.getId());
                contenido.setSeccionMicrocurriculoId(seccionMicrocurriculoIdNew);
            }
            List<TablaMicrocurriculoInfo> attachedTablaMicrocurriculoInfoListNew = new ArrayList<TablaMicrocurriculoInfo>();
            for (TablaMicrocurriculoInfo tablaMicrocurriculoInfoListNewTablaMicrocurriculoInfoToAttach : tablaMicrocurriculoInfoListNew) {
                tablaMicrocurriculoInfoListNewTablaMicrocurriculoInfoToAttach = em.getReference(tablaMicrocurriculoInfoListNewTablaMicrocurriculoInfoToAttach.getClass(), tablaMicrocurriculoInfoListNewTablaMicrocurriculoInfoToAttach.getTablaMicrocurriculoInfoPK());
                attachedTablaMicrocurriculoInfoListNew.add(tablaMicrocurriculoInfoListNewTablaMicrocurriculoInfoToAttach);
            }
            tablaMicrocurriculoInfoListNew = attachedTablaMicrocurriculoInfoListNew;
            contenido.setTablaMicrocurriculoInfoList(tablaMicrocurriculoInfoListNew);
            contenido = em.merge(contenido);
            if (seccionMicrocurriculoIdOld != null && !seccionMicrocurriculoIdOld.equals(seccionMicrocurriculoIdNew)) {
                seccionMicrocurriculoIdOld.getContenidoList().remove(contenido);
                seccionMicrocurriculoIdOld = em.merge(seccionMicrocurriculoIdOld);
            }
            if (seccionMicrocurriculoIdNew != null && !seccionMicrocurriculoIdNew.equals(seccionMicrocurriculoIdOld)) {
                seccionMicrocurriculoIdNew.getContenidoList().add(contenido);
                seccionMicrocurriculoIdNew = em.merge(seccionMicrocurriculoIdNew);
            }
            for (TablaMicrocurriculoInfo tablaMicrocurriculoInfoListNewTablaMicrocurriculoInfo : tablaMicrocurriculoInfoListNew) {
                if (!tablaMicrocurriculoInfoListOld.contains(tablaMicrocurriculoInfoListNewTablaMicrocurriculoInfo)) {
                    Contenido oldContenidoIdOfTablaMicrocurriculoInfoListNewTablaMicrocurriculoInfo = tablaMicrocurriculoInfoListNewTablaMicrocurriculoInfo.getContenidoId();
                    tablaMicrocurriculoInfoListNewTablaMicrocurriculoInfo.setContenidoId(contenido);
                    tablaMicrocurriculoInfoListNewTablaMicrocurriculoInfo = em.merge(tablaMicrocurriculoInfoListNewTablaMicrocurriculoInfo);
                    if (oldContenidoIdOfTablaMicrocurriculoInfoListNewTablaMicrocurriculoInfo != null && !oldContenidoIdOfTablaMicrocurriculoInfoListNewTablaMicrocurriculoInfo.equals(contenido)) {
                        oldContenidoIdOfTablaMicrocurriculoInfoListNewTablaMicrocurriculoInfo.getTablaMicrocurriculoInfoList().remove(tablaMicrocurriculoInfoListNewTablaMicrocurriculoInfo);
                        oldContenidoIdOfTablaMicrocurriculoInfoListNewTablaMicrocurriculoInfo = em.merge(oldContenidoIdOfTablaMicrocurriculoInfoListNewTablaMicrocurriculoInfo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = contenido.getId();
                if (findContenido(id) == null) {
                    throw new NonexistentEntityException("The contenido with id " + id + " no longer exists.");
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
            Contenido contenido;
            try {
                contenido = em.getReference(Contenido.class, id);
                contenido.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contenido with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<TablaMicrocurriculoInfo> tablaMicrocurriculoInfoListOrphanCheck = contenido.getTablaMicrocurriculoInfoList();
            for (TablaMicrocurriculoInfo tablaMicrocurriculoInfoListOrphanCheckTablaMicrocurriculoInfo : tablaMicrocurriculoInfoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Contenido (" + contenido + ") cannot be destroyed since the TablaMicrocurriculoInfo " + tablaMicrocurriculoInfoListOrphanCheckTablaMicrocurriculoInfo + " in its tablaMicrocurriculoInfoList field has a non-nullable contenidoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            SeccionMicrocurriculo seccionMicrocurriculoId = contenido.getSeccionMicrocurriculoId();
            if (seccionMicrocurriculoId != null) {
                seccionMicrocurriculoId.getContenidoList().remove(contenido);
                seccionMicrocurriculoId = em.merge(seccionMicrocurriculoId);
            }
            em.remove(contenido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Contenido> findContenidoEntities() {
        return findContenidoEntities(true, -1, -1);
    }

    public List<Contenido> findContenidoEntities(int maxResults, int firstResult) {
        return findContenidoEntities(false, maxResults, firstResult);
    }

    private List<Contenido> findContenidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Contenido.class));
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

    public Contenido findContenido(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Contenido.class, id);
        } finally {
            em.close();
        }
    }

    public int getContenidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Contenido> rt = cq.from(Contenido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
