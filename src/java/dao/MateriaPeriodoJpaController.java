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
import dto.Materia;
import dto.MateriaPeriodo;
import dto.MateriaPeriodoGrupo;
import dto.MateriaPeriodoPK;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Manuel
 */
public class MateriaPeriodoJpaController implements Serializable {

    public MateriaPeriodoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MateriaPeriodo materiaPeriodo) throws PreexistingEntityException, Exception {
        if (materiaPeriodo.getMateriaPeriodoPK() == null) {
            materiaPeriodo.setMateriaPeriodoPK(new MateriaPeriodoPK());
        }
        if (materiaPeriodo.getMateriaPeriodoGrupoList() == null) {
            materiaPeriodo.setMateriaPeriodoGrupoList(new ArrayList<MateriaPeriodoGrupo>());
        }
        materiaPeriodo.getMateriaPeriodoPK().setMateriaPensumCodigo(materiaPeriodo.getMateria().getMateriaPK().getPensumCodigo());
        materiaPeriodo.getMateriaPeriodoPK().setMateriaCodigoMateria(materiaPeriodo.getMateria().getMateriaPK().getCodigoMateria());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materia materia = materiaPeriodo.getMateria();
            if (materia != null) {
                materia = em.getReference(materia.getClass(), materia.getMateriaPK());
                materiaPeriodo.setMateria(materia);
            }
            List<MateriaPeriodoGrupo> attachedMateriaPeriodoGrupoList = new ArrayList<MateriaPeriodoGrupo>();
            for (MateriaPeriodoGrupo materiaPeriodoGrupoListMateriaPeriodoGrupoToAttach : materiaPeriodo.getMateriaPeriodoGrupoList()) {
                materiaPeriodoGrupoListMateriaPeriodoGrupoToAttach = em.getReference(materiaPeriodoGrupoListMateriaPeriodoGrupoToAttach.getClass(), materiaPeriodoGrupoListMateriaPeriodoGrupoToAttach.getMateriaPeriodoGrupoPK());
                attachedMateriaPeriodoGrupoList.add(materiaPeriodoGrupoListMateriaPeriodoGrupoToAttach);
            }
            materiaPeriodo.setMateriaPeriodoGrupoList(attachedMateriaPeriodoGrupoList);
            em.persist(materiaPeriodo);
            if (materia != null) {
                materia.getMateriaPeriodoList().add(materiaPeriodo);
                materia = em.merge(materia);
            }
            for (MateriaPeriodoGrupo materiaPeriodoGrupoListMateriaPeriodoGrupo : materiaPeriodo.getMateriaPeriodoGrupoList()) {
                MateriaPeriodo oldMateriaPeriodoOfMateriaPeriodoGrupoListMateriaPeriodoGrupo = materiaPeriodoGrupoListMateriaPeriodoGrupo.getMateriaPeriodo();
                materiaPeriodoGrupoListMateriaPeriodoGrupo.setMateriaPeriodo(materiaPeriodo);
                materiaPeriodoGrupoListMateriaPeriodoGrupo = em.merge(materiaPeriodoGrupoListMateriaPeriodoGrupo);
                if (oldMateriaPeriodoOfMateriaPeriodoGrupoListMateriaPeriodoGrupo != null) {
                    oldMateriaPeriodoOfMateriaPeriodoGrupoListMateriaPeriodoGrupo.getMateriaPeriodoGrupoList().remove(materiaPeriodoGrupoListMateriaPeriodoGrupo);
                    oldMateriaPeriodoOfMateriaPeriodoGrupoListMateriaPeriodoGrupo = em.merge(oldMateriaPeriodoOfMateriaPeriodoGrupoListMateriaPeriodoGrupo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMateriaPeriodo(materiaPeriodo.getMateriaPeriodoPK()) != null) {
                throw new PreexistingEntityException("MateriaPeriodo " + materiaPeriodo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MateriaPeriodo materiaPeriodo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        materiaPeriodo.getMateriaPeriodoPK().setMateriaPensumCodigo(materiaPeriodo.getMateria().getMateriaPK().getPensumCodigo());
        materiaPeriodo.getMateriaPeriodoPK().setMateriaCodigoMateria(materiaPeriodo.getMateria().getMateriaPK().getCodigoMateria());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MateriaPeriodo persistentMateriaPeriodo = em.find(MateriaPeriodo.class, materiaPeriodo.getMateriaPeriodoPK());
            Materia materiaOld = persistentMateriaPeriodo.getMateria();
            Materia materiaNew = materiaPeriodo.getMateria();
            List<MateriaPeriodoGrupo> materiaPeriodoGrupoListOld = persistentMateriaPeriodo.getMateriaPeriodoGrupoList();
            List<MateriaPeriodoGrupo> materiaPeriodoGrupoListNew = materiaPeriodo.getMateriaPeriodoGrupoList();
            List<String> illegalOrphanMessages = null;
            for (MateriaPeriodoGrupo materiaPeriodoGrupoListOldMateriaPeriodoGrupo : materiaPeriodoGrupoListOld) {
                if (!materiaPeriodoGrupoListNew.contains(materiaPeriodoGrupoListOldMateriaPeriodoGrupo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MateriaPeriodoGrupo " + materiaPeriodoGrupoListOldMateriaPeriodoGrupo + " since its materiaPeriodo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (materiaNew != null) {
                materiaNew = em.getReference(materiaNew.getClass(), materiaNew.getMateriaPK());
                materiaPeriodo.setMateria(materiaNew);
            }
            List<MateriaPeriodoGrupo> attachedMateriaPeriodoGrupoListNew = new ArrayList<MateriaPeriodoGrupo>();
            for (MateriaPeriodoGrupo materiaPeriodoGrupoListNewMateriaPeriodoGrupoToAttach : materiaPeriodoGrupoListNew) {
                materiaPeriodoGrupoListNewMateriaPeriodoGrupoToAttach = em.getReference(materiaPeriodoGrupoListNewMateriaPeriodoGrupoToAttach.getClass(), materiaPeriodoGrupoListNewMateriaPeriodoGrupoToAttach.getMateriaPeriodoGrupoPK());
                attachedMateriaPeriodoGrupoListNew.add(materiaPeriodoGrupoListNewMateriaPeriodoGrupoToAttach);
            }
            materiaPeriodoGrupoListNew = attachedMateriaPeriodoGrupoListNew;
            materiaPeriodo.setMateriaPeriodoGrupoList(materiaPeriodoGrupoListNew);
            materiaPeriodo = em.merge(materiaPeriodo);
            if (materiaOld != null && !materiaOld.equals(materiaNew)) {
                materiaOld.getMateriaPeriodoList().remove(materiaPeriodo);
                materiaOld = em.merge(materiaOld);
            }
            if (materiaNew != null && !materiaNew.equals(materiaOld)) {
                materiaNew.getMateriaPeriodoList().add(materiaPeriodo);
                materiaNew = em.merge(materiaNew);
            }
            for (MateriaPeriodoGrupo materiaPeriodoGrupoListNewMateriaPeriodoGrupo : materiaPeriodoGrupoListNew) {
                if (!materiaPeriodoGrupoListOld.contains(materiaPeriodoGrupoListNewMateriaPeriodoGrupo)) {
                    MateriaPeriodo oldMateriaPeriodoOfMateriaPeriodoGrupoListNewMateriaPeriodoGrupo = materiaPeriodoGrupoListNewMateriaPeriodoGrupo.getMateriaPeriodo();
                    materiaPeriodoGrupoListNewMateriaPeriodoGrupo.setMateriaPeriodo(materiaPeriodo);
                    materiaPeriodoGrupoListNewMateriaPeriodoGrupo = em.merge(materiaPeriodoGrupoListNewMateriaPeriodoGrupo);
                    if (oldMateriaPeriodoOfMateriaPeriodoGrupoListNewMateriaPeriodoGrupo != null && !oldMateriaPeriodoOfMateriaPeriodoGrupoListNewMateriaPeriodoGrupo.equals(materiaPeriodo)) {
                        oldMateriaPeriodoOfMateriaPeriodoGrupoListNewMateriaPeriodoGrupo.getMateriaPeriodoGrupoList().remove(materiaPeriodoGrupoListNewMateriaPeriodoGrupo);
                        oldMateriaPeriodoOfMateriaPeriodoGrupoListNewMateriaPeriodoGrupo = em.merge(oldMateriaPeriodoOfMateriaPeriodoGrupoListNewMateriaPeriodoGrupo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                MateriaPeriodoPK id = materiaPeriodo.getMateriaPeriodoPK();
                if (findMateriaPeriodo(id) == null) {
                    throw new NonexistentEntityException("The materiaPeriodo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(MateriaPeriodoPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MateriaPeriodo materiaPeriodo;
            try {
                materiaPeriodo = em.getReference(MateriaPeriodo.class, id);
                materiaPeriodo.getMateriaPeriodoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The materiaPeriodo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<MateriaPeriodoGrupo> materiaPeriodoGrupoListOrphanCheck = materiaPeriodo.getMateriaPeriodoGrupoList();
            for (MateriaPeriodoGrupo materiaPeriodoGrupoListOrphanCheckMateriaPeriodoGrupo : materiaPeriodoGrupoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MateriaPeriodo (" + materiaPeriodo + ") cannot be destroyed since the MateriaPeriodoGrupo " + materiaPeriodoGrupoListOrphanCheckMateriaPeriodoGrupo + " in its materiaPeriodoGrupoList field has a non-nullable materiaPeriodo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Materia materia = materiaPeriodo.getMateria();
            if (materia != null) {
                materia.getMateriaPeriodoList().remove(materiaPeriodo);
                materia = em.merge(materia);
            }
            em.remove(materiaPeriodo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MateriaPeriodo> findMateriaPeriodoEntities() {
        return findMateriaPeriodoEntities(true, -1, -1);
    }

    public List<MateriaPeriodo> findMateriaPeriodoEntities(int maxResults, int firstResult) {
        return findMateriaPeriodoEntities(false, maxResults, firstResult);
    }

    private List<MateriaPeriodo> findMateriaPeriodoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MateriaPeriodo.class));
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

    public MateriaPeriodo findMateriaPeriodo(MateriaPeriodoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MateriaPeriodo.class, id);
        } finally {
            em.close();
        }
    }

    public int getMateriaPeriodoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MateriaPeriodo> rt = cq.from(MateriaPeriodo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
