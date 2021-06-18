/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.Contenido;
import dto.TablaMicrocurriculo;
import dto.TablaMicrocurriculoInfo;
import dto.TablaMicrocurriculoInfoPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Manuel
 */
public class TablaMicrocurriculoInfoJpaController implements Serializable {

    public TablaMicrocurriculoInfoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TablaMicrocurriculoInfo tablaMicrocurriculoInfo) throws PreexistingEntityException, Exception {
        if (tablaMicrocurriculoInfo.getTablaMicrocurriculoInfoPK() == null) {
            tablaMicrocurriculoInfo.setTablaMicrocurriculoInfoPK(new TablaMicrocurriculoInfoPK());
        }
        tablaMicrocurriculoInfo.getTablaMicrocurriculoInfoPK().setTablaMicrocurriculoSeccionMicrocurriculoId(tablaMicrocurriculoInfo.getTablaMicrocurriculo().getTablaMicrocurriculoPK().getSeccionMicrocurriculoId());
        tablaMicrocurriculoInfo.getTablaMicrocurriculoInfoPK().setTablaMicrocurriculoId(tablaMicrocurriculoInfo.getTablaMicrocurriculo().getTablaMicrocurriculoPK().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contenido contenidoId = tablaMicrocurriculoInfo.getContenidoId();
            if (contenidoId != null) {
                contenidoId = em.getReference(contenidoId.getClass(), contenidoId.getId());
                tablaMicrocurriculoInfo.setContenidoId(contenidoId);
            }
            TablaMicrocurriculo tablaMicrocurriculo = tablaMicrocurriculoInfo.getTablaMicrocurriculo();
            if (tablaMicrocurriculo != null) {
                tablaMicrocurriculo = em.getReference(tablaMicrocurriculo.getClass(), tablaMicrocurriculo.getTablaMicrocurriculoPK());
                tablaMicrocurriculoInfo.setTablaMicrocurriculo(tablaMicrocurriculo);
            }
            em.persist(tablaMicrocurriculoInfo);
            if (contenidoId != null) {
                contenidoId.getTablaMicrocurriculoInfoList().add(tablaMicrocurriculoInfo);
                contenidoId = em.merge(contenidoId);
            }
            if (tablaMicrocurriculo != null) {
                tablaMicrocurriculo.getTablaMicrocurriculoInfoList().add(tablaMicrocurriculoInfo);
                tablaMicrocurriculo = em.merge(tablaMicrocurriculo);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTablaMicrocurriculoInfo(tablaMicrocurriculoInfo.getTablaMicrocurriculoInfoPK()) != null) {
                throw new PreexistingEntityException("TablaMicrocurriculoInfo " + tablaMicrocurriculoInfo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TablaMicrocurriculoInfo tablaMicrocurriculoInfo) throws NonexistentEntityException, Exception {
        tablaMicrocurriculoInfo.getTablaMicrocurriculoInfoPK().setTablaMicrocurriculoSeccionMicrocurriculoId(tablaMicrocurriculoInfo.getTablaMicrocurriculo().getTablaMicrocurriculoPK().getSeccionMicrocurriculoId());
        tablaMicrocurriculoInfo.getTablaMicrocurriculoInfoPK().setTablaMicrocurriculoId(tablaMicrocurriculoInfo.getTablaMicrocurriculo().getTablaMicrocurriculoPK().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TablaMicrocurriculoInfo persistentTablaMicrocurriculoInfo = em.find(TablaMicrocurriculoInfo.class, tablaMicrocurriculoInfo.getTablaMicrocurriculoInfoPK());
            Contenido contenidoIdOld = persistentTablaMicrocurriculoInfo.getContenidoId();
            Contenido contenidoIdNew = tablaMicrocurriculoInfo.getContenidoId();
            TablaMicrocurriculo tablaMicrocurriculoOld = persistentTablaMicrocurriculoInfo.getTablaMicrocurriculo();
            TablaMicrocurriculo tablaMicrocurriculoNew = tablaMicrocurriculoInfo.getTablaMicrocurriculo();
            if (contenidoIdNew != null) {
                contenidoIdNew = em.getReference(contenidoIdNew.getClass(), contenidoIdNew.getId());
                tablaMicrocurriculoInfo.setContenidoId(contenidoIdNew);
            }
            if (tablaMicrocurriculoNew != null) {
                tablaMicrocurriculoNew = em.getReference(tablaMicrocurriculoNew.getClass(), tablaMicrocurriculoNew.getTablaMicrocurriculoPK());
                tablaMicrocurriculoInfo.setTablaMicrocurriculo(tablaMicrocurriculoNew);
            }
            tablaMicrocurriculoInfo = em.merge(tablaMicrocurriculoInfo);
            if (contenidoIdOld != null && !contenidoIdOld.equals(contenidoIdNew)) {
                contenidoIdOld.getTablaMicrocurriculoInfoList().remove(tablaMicrocurriculoInfo);
                contenidoIdOld = em.merge(contenidoIdOld);
            }
            if (contenidoIdNew != null && !contenidoIdNew.equals(contenidoIdOld)) {
                contenidoIdNew.getTablaMicrocurriculoInfoList().add(tablaMicrocurriculoInfo);
                contenidoIdNew = em.merge(contenidoIdNew);
            }
            if (tablaMicrocurriculoOld != null && !tablaMicrocurriculoOld.equals(tablaMicrocurriculoNew)) {
                tablaMicrocurriculoOld.getTablaMicrocurriculoInfoList().remove(tablaMicrocurriculoInfo);
                tablaMicrocurriculoOld = em.merge(tablaMicrocurriculoOld);
            }
            if (tablaMicrocurriculoNew != null && !tablaMicrocurriculoNew.equals(tablaMicrocurriculoOld)) {
                tablaMicrocurriculoNew.getTablaMicrocurriculoInfoList().add(tablaMicrocurriculoInfo);
                tablaMicrocurriculoNew = em.merge(tablaMicrocurriculoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                TablaMicrocurriculoInfoPK id = tablaMicrocurriculoInfo.getTablaMicrocurriculoInfoPK();
                if (findTablaMicrocurriculoInfo(id) == null) {
                    throw new NonexistentEntityException("The tablaMicrocurriculoInfo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(TablaMicrocurriculoInfoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TablaMicrocurriculoInfo tablaMicrocurriculoInfo;
            try {
                tablaMicrocurriculoInfo = em.getReference(TablaMicrocurriculoInfo.class, id);
                tablaMicrocurriculoInfo.getTablaMicrocurriculoInfoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tablaMicrocurriculoInfo with id " + id + " no longer exists.", enfe);
            }
            Contenido contenidoId = tablaMicrocurriculoInfo.getContenidoId();
            if (contenidoId != null) {
                contenidoId.getTablaMicrocurriculoInfoList().remove(tablaMicrocurriculoInfo);
                contenidoId = em.merge(contenidoId);
            }
            TablaMicrocurriculo tablaMicrocurriculo = tablaMicrocurriculoInfo.getTablaMicrocurriculo();
            if (tablaMicrocurriculo != null) {
                tablaMicrocurriculo.getTablaMicrocurriculoInfoList().remove(tablaMicrocurriculoInfo);
                tablaMicrocurriculo = em.merge(tablaMicrocurriculo);
            }
            em.remove(tablaMicrocurriculoInfo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TablaMicrocurriculoInfo> findTablaMicrocurriculoInfoEntities() {
        return findTablaMicrocurriculoInfoEntities(true, -1, -1);
    }

    public List<TablaMicrocurriculoInfo> findTablaMicrocurriculoInfoEntities(int maxResults, int firstResult) {
        return findTablaMicrocurriculoInfoEntities(false, maxResults, firstResult);
    }

    private List<TablaMicrocurriculoInfo> findTablaMicrocurriculoInfoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TablaMicrocurriculoInfo.class));
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

    public TablaMicrocurriculoInfo findTablaMicrocurriculoInfo(TablaMicrocurriculoInfoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TablaMicrocurriculoInfo.class, id);
        } finally {
            em.close();
        }
    }

    public int getTablaMicrocurriculoInfoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TablaMicrocurriculoInfo> rt = cq.from(TablaMicrocurriculoInfo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
