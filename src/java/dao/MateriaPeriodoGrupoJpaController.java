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
import dto.Docente;
import dto.MateriaPeriodo;
import dto.MateriaPeriodoGrupo;
import dto.MateriaPeriodoGrupoPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Manuel
 */
public class MateriaPeriodoGrupoJpaController implements Serializable {

    public MateriaPeriodoGrupoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MateriaPeriodoGrupo materiaPeriodoGrupo) throws PreexistingEntityException, Exception {
        if (materiaPeriodoGrupo.getMateriaPeriodoGrupoPK() == null) {
            materiaPeriodoGrupo.setMateriaPeriodoGrupoPK(new MateriaPeriodoGrupoPK());
        }
        materiaPeriodoGrupo.getMateriaPeriodoGrupoPK().setMateriaPeriodoSemestreAnio(materiaPeriodoGrupo.getMateriaPeriodo().getMateriaPeriodoPK().getSemestreAnio());
        materiaPeriodoGrupo.getMateriaPeriodoGrupoPK().setMateriaPeriodoMateriaPensumCodigo(materiaPeriodoGrupo.getMateriaPeriodo().getMateriaPeriodoPK().getMateriaPensumCodigo());
        materiaPeriodoGrupo.getMateriaPeriodoGrupoPK().setDocenteCodigo(materiaPeriodoGrupo.getDocente().getCodigoDocente());
        materiaPeriodoGrupo.getMateriaPeriodoGrupoPK().setMateriaPeriodoMateriaCodigoMateria(materiaPeriodoGrupo.getMateriaPeriodo().getMateriaPeriodoPK().getMateriaCodigoMateria());
        materiaPeriodoGrupo.getMateriaPeriodoGrupoPK().setMateriaPeriodoAnio(materiaPeriodoGrupo.getMateriaPeriodo().getMateriaPeriodoPK().getAnio());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Docente docente = materiaPeriodoGrupo.getDocente();
            if (docente != null) {
                docente = em.getReference(docente.getClass(), docente.getCodigoDocente());
                materiaPeriodoGrupo.setDocente(docente);
            }
            MateriaPeriodo materiaPeriodo = materiaPeriodoGrupo.getMateriaPeriodo();
            if (materiaPeriodo != null) {
                materiaPeriodo = em.getReference(materiaPeriodo.getClass(), materiaPeriodo.getMateriaPeriodoPK());
                materiaPeriodoGrupo.setMateriaPeriodo(materiaPeriodo);
            }
            em.persist(materiaPeriodoGrupo);
            if (docente != null) {
                docente.getMateriaPeriodoGrupoList().add(materiaPeriodoGrupo);
                docente = em.merge(docente);
            }
            if (materiaPeriodo != null) {
                materiaPeriodo.getMateriaPeriodoGrupoList().add(materiaPeriodoGrupo);
                materiaPeriodo = em.merge(materiaPeriodo);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMateriaPeriodoGrupo(materiaPeriodoGrupo.getMateriaPeriodoGrupoPK()) != null) {
                throw new PreexistingEntityException("MateriaPeriodoGrupo " + materiaPeriodoGrupo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MateriaPeriodoGrupo materiaPeriodoGrupo) throws NonexistentEntityException, Exception {
        materiaPeriodoGrupo.getMateriaPeriodoGrupoPK().setMateriaPeriodoSemestreAnio(materiaPeriodoGrupo.getMateriaPeriodo().getMateriaPeriodoPK().getSemestreAnio());
        materiaPeriodoGrupo.getMateriaPeriodoGrupoPK().setMateriaPeriodoMateriaPensumCodigo(materiaPeriodoGrupo.getMateriaPeriodo().getMateriaPeriodoPK().getMateriaPensumCodigo());
        materiaPeriodoGrupo.getMateriaPeriodoGrupoPK().setDocenteCodigo(materiaPeriodoGrupo.getDocente().getCodigoDocente());
        materiaPeriodoGrupo.getMateriaPeriodoGrupoPK().setMateriaPeriodoMateriaCodigoMateria(materiaPeriodoGrupo.getMateriaPeriodo().getMateriaPeriodoPK().getMateriaCodigoMateria());
        materiaPeriodoGrupo.getMateriaPeriodoGrupoPK().setMateriaPeriodoAnio(materiaPeriodoGrupo.getMateriaPeriodo().getMateriaPeriodoPK().getAnio());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MateriaPeriodoGrupo persistentMateriaPeriodoGrupo = em.find(MateriaPeriodoGrupo.class, materiaPeriodoGrupo.getMateriaPeriodoGrupoPK());
            Docente docenteOld = persistentMateriaPeriodoGrupo.getDocente();
            Docente docenteNew = materiaPeriodoGrupo.getDocente();
            MateriaPeriodo materiaPeriodoOld = persistentMateriaPeriodoGrupo.getMateriaPeriodo();
            MateriaPeriodo materiaPeriodoNew = materiaPeriodoGrupo.getMateriaPeriodo();
            if (docenteNew != null) {
                docenteNew = em.getReference(docenteNew.getClass(), docenteNew.getCodigoDocente());
                materiaPeriodoGrupo.setDocente(docenteNew);
            }
            if (materiaPeriodoNew != null) {
                materiaPeriodoNew = em.getReference(materiaPeriodoNew.getClass(), materiaPeriodoNew.getMateriaPeriodoPK());
                materiaPeriodoGrupo.setMateriaPeriodo(materiaPeriodoNew);
            }
            materiaPeriodoGrupo = em.merge(materiaPeriodoGrupo);
            if (docenteOld != null && !docenteOld.equals(docenteNew)) {
                docenteOld.getMateriaPeriodoGrupoList().remove(materiaPeriodoGrupo);
                docenteOld = em.merge(docenteOld);
            }
            if (docenteNew != null && !docenteNew.equals(docenteOld)) {
                docenteNew.getMateriaPeriodoGrupoList().add(materiaPeriodoGrupo);
                docenteNew = em.merge(docenteNew);
            }
            if (materiaPeriodoOld != null && !materiaPeriodoOld.equals(materiaPeriodoNew)) {
                materiaPeriodoOld.getMateriaPeriodoGrupoList().remove(materiaPeriodoGrupo);
                materiaPeriodoOld = em.merge(materiaPeriodoOld);
            }
            if (materiaPeriodoNew != null && !materiaPeriodoNew.equals(materiaPeriodoOld)) {
                materiaPeriodoNew.getMateriaPeriodoGrupoList().add(materiaPeriodoGrupo);
                materiaPeriodoNew = em.merge(materiaPeriodoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                MateriaPeriodoGrupoPK id = materiaPeriodoGrupo.getMateriaPeriodoGrupoPK();
                if (findMateriaPeriodoGrupo(id) == null) {
                    throw new NonexistentEntityException("The materiaPeriodoGrupo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(MateriaPeriodoGrupoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MateriaPeriodoGrupo materiaPeriodoGrupo;
            try {
                materiaPeriodoGrupo = em.getReference(MateriaPeriodoGrupo.class, id);
                materiaPeriodoGrupo.getMateriaPeriodoGrupoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The materiaPeriodoGrupo with id " + id + " no longer exists.", enfe);
            }
            Docente docente = materiaPeriodoGrupo.getDocente();
            if (docente != null) {
                docente.getMateriaPeriodoGrupoList().remove(materiaPeriodoGrupo);
                docente = em.merge(docente);
            }
            MateriaPeriodo materiaPeriodo = materiaPeriodoGrupo.getMateriaPeriodo();
            if (materiaPeriodo != null) {
                materiaPeriodo.getMateriaPeriodoGrupoList().remove(materiaPeriodoGrupo);
                materiaPeriodo = em.merge(materiaPeriodo);
            }
            em.remove(materiaPeriodoGrupo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MateriaPeriodoGrupo> findMateriaPeriodoGrupoEntities() {
        return findMateriaPeriodoGrupoEntities(true, -1, -1);
    }

    public List<MateriaPeriodoGrupo> findMateriaPeriodoGrupoEntities(int maxResults, int firstResult) {
        return findMateriaPeriodoGrupoEntities(false, maxResults, firstResult);
    }

    private List<MateriaPeriodoGrupo> findMateriaPeriodoGrupoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MateriaPeriodoGrupo.class));
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

    public MateriaPeriodoGrupo findMateriaPeriodoGrupo(MateriaPeriodoGrupoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MateriaPeriodoGrupo.class, id);
        } finally {
            em.close();
        }
    }

    public int getMateriaPeriodoGrupoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MateriaPeriodoGrupo> rt = cq.from(MateriaPeriodoGrupo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
