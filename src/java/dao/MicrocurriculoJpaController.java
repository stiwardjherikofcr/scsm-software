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
import dto.AreaFormacion;
import dto.Materia;
import dto.Microcurriculo;
import dto.MicrocurriculoPK;
import dto.SeccionMicrocurriculo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Manuel
 */
public class MicrocurriculoJpaController implements Serializable {

    public MicrocurriculoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Microcurriculo microcurriculo) throws PreexistingEntityException, Exception {
        if (microcurriculo.getMicrocurriculoPK() == null) {
            microcurriculo.setMicrocurriculoPK(new MicrocurriculoPK());
        }
        if (microcurriculo.getSeccionMicrocurriculoList() == null) {
            microcurriculo.setSeccionMicrocurriculoList(new ArrayList<SeccionMicrocurriculo>());
        }
        microcurriculo.getMicrocurriculoPK().setMateriaPensumCodigo(microcurriculo.getMateria().getMateriaPK().getPensumCodigo());
        microcurriculo.getMicrocurriculoPK().setMateriaCodigoMateria(microcurriculo.getMateria().getMateriaPK().getCodigoMateria());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AreaFormacion areaDeFormacionId = microcurriculo.getAreaDeFormacionId();
            if (areaDeFormacionId != null) {
                areaDeFormacionId = em.getReference(areaDeFormacionId.getClass(), areaDeFormacionId.getId());
                microcurriculo.setAreaDeFormacionId(areaDeFormacionId);
            }
            Materia materia = microcurriculo.getMateria();
            if (materia != null) {
                materia = em.getReference(materia.getClass(), materia.getMateriaPK());
                microcurriculo.setMateria(materia);
            }
            List<SeccionMicrocurriculo> attachedSeccionMicrocurriculoList = new ArrayList<SeccionMicrocurriculo>();
            for (SeccionMicrocurriculo seccionMicrocurriculoListSeccionMicrocurriculoToAttach : microcurriculo.getSeccionMicrocurriculoList()) {
                seccionMicrocurriculoListSeccionMicrocurriculoToAttach = em.getReference(seccionMicrocurriculoListSeccionMicrocurriculoToAttach.getClass(), seccionMicrocurriculoListSeccionMicrocurriculoToAttach.getId());
                attachedSeccionMicrocurriculoList.add(seccionMicrocurriculoListSeccionMicrocurriculoToAttach);
            }
            microcurriculo.setSeccionMicrocurriculoList(attachedSeccionMicrocurriculoList);
            em.persist(microcurriculo);
            if (areaDeFormacionId != null) {
                areaDeFormacionId.getMicrocurriculoList().add(microcurriculo);
                areaDeFormacionId = em.merge(areaDeFormacionId);
            }
            if (materia != null) {
                materia.getMicrocurriculoList().add(microcurriculo);
                materia = em.merge(materia);
            }
            for (SeccionMicrocurriculo seccionMicrocurriculoListSeccionMicrocurriculo : microcurriculo.getSeccionMicrocurriculoList()) {
                Microcurriculo oldMicrocurriculoOfSeccionMicrocurriculoListSeccionMicrocurriculo = seccionMicrocurriculoListSeccionMicrocurriculo.getMicrocurriculo();
                seccionMicrocurriculoListSeccionMicrocurriculo.setMicrocurriculo(microcurriculo);
                seccionMicrocurriculoListSeccionMicrocurriculo = em.merge(seccionMicrocurriculoListSeccionMicrocurriculo);
                if (oldMicrocurriculoOfSeccionMicrocurriculoListSeccionMicrocurriculo != null) {
                    oldMicrocurriculoOfSeccionMicrocurriculoListSeccionMicrocurriculo.getSeccionMicrocurriculoList().remove(seccionMicrocurriculoListSeccionMicrocurriculo);
                    oldMicrocurriculoOfSeccionMicrocurriculoListSeccionMicrocurriculo = em.merge(oldMicrocurriculoOfSeccionMicrocurriculoListSeccionMicrocurriculo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMicrocurriculo(microcurriculo.getMicrocurriculoPK()) != null) {
                throw new PreexistingEntityException("Microcurriculo " + microcurriculo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Microcurriculo microcurriculo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        microcurriculo.getMicrocurriculoPK().setMateriaPensumCodigo(microcurriculo.getMateria().getMateriaPK().getPensumCodigo());
        microcurriculo.getMicrocurriculoPK().setMateriaCodigoMateria(microcurriculo.getMateria().getMateriaPK().getCodigoMateria());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Microcurriculo persistentMicrocurriculo = em.find(Microcurriculo.class, microcurriculo.getMicrocurriculoPK());
            AreaFormacion areaDeFormacionIdOld = persistentMicrocurriculo.getAreaDeFormacionId();
            AreaFormacion areaDeFormacionIdNew = microcurriculo.getAreaDeFormacionId();
            Materia materiaOld = persistentMicrocurriculo.getMateria();
            Materia materiaNew = microcurriculo.getMateria();
            List<SeccionMicrocurriculo> seccionMicrocurriculoListOld = persistentMicrocurriculo.getSeccionMicrocurriculoList();
            List<SeccionMicrocurriculo> seccionMicrocurriculoListNew = microcurriculo.getSeccionMicrocurriculoList();
            List<String> illegalOrphanMessages = null;
            for (SeccionMicrocurriculo seccionMicrocurriculoListOldSeccionMicrocurriculo : seccionMicrocurriculoListOld) {
                if (!seccionMicrocurriculoListNew.contains(seccionMicrocurriculoListOldSeccionMicrocurriculo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SeccionMicrocurriculo " + seccionMicrocurriculoListOldSeccionMicrocurriculo + " since its microcurriculo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (areaDeFormacionIdNew != null) {
                areaDeFormacionIdNew = em.getReference(areaDeFormacionIdNew.getClass(), areaDeFormacionIdNew.getId());
                microcurriculo.setAreaDeFormacionId(areaDeFormacionIdNew);
            }
            if (materiaNew != null) {
                materiaNew = em.getReference(materiaNew.getClass(), materiaNew.getMateriaPK());
                microcurriculo.setMateria(materiaNew);
            }
            List<SeccionMicrocurriculo> attachedSeccionMicrocurriculoListNew = new ArrayList<SeccionMicrocurriculo>();
            for (SeccionMicrocurriculo seccionMicrocurriculoListNewSeccionMicrocurriculoToAttach : seccionMicrocurriculoListNew) {
                seccionMicrocurriculoListNewSeccionMicrocurriculoToAttach = em.getReference(seccionMicrocurriculoListNewSeccionMicrocurriculoToAttach.getClass(), seccionMicrocurriculoListNewSeccionMicrocurriculoToAttach.getId());
                attachedSeccionMicrocurriculoListNew.add(seccionMicrocurriculoListNewSeccionMicrocurriculoToAttach);
            }
            seccionMicrocurriculoListNew = attachedSeccionMicrocurriculoListNew;
            microcurriculo.setSeccionMicrocurriculoList(seccionMicrocurriculoListNew);
            microcurriculo = em.merge(microcurriculo);
            if (areaDeFormacionIdOld != null && !areaDeFormacionIdOld.equals(areaDeFormacionIdNew)) {
                areaDeFormacionIdOld.getMicrocurriculoList().remove(microcurriculo);
                areaDeFormacionIdOld = em.merge(areaDeFormacionIdOld);
            }
            if (areaDeFormacionIdNew != null && !areaDeFormacionIdNew.equals(areaDeFormacionIdOld)) {
                areaDeFormacionIdNew.getMicrocurriculoList().add(microcurriculo);
                areaDeFormacionIdNew = em.merge(areaDeFormacionIdNew);
            }
            if (materiaOld != null && !materiaOld.equals(materiaNew)) {
                materiaOld.getMicrocurriculoList().remove(microcurriculo);
                materiaOld = em.merge(materiaOld);
            }
            if (materiaNew != null && !materiaNew.equals(materiaOld)) {
                materiaNew.getMicrocurriculoList().add(microcurriculo);
                materiaNew = em.merge(materiaNew);
            }
            for (SeccionMicrocurriculo seccionMicrocurriculoListNewSeccionMicrocurriculo : seccionMicrocurriculoListNew) {
                if (!seccionMicrocurriculoListOld.contains(seccionMicrocurriculoListNewSeccionMicrocurriculo)) {
                    Microcurriculo oldMicrocurriculoOfSeccionMicrocurriculoListNewSeccionMicrocurriculo = seccionMicrocurriculoListNewSeccionMicrocurriculo.getMicrocurriculo();
                    seccionMicrocurriculoListNewSeccionMicrocurriculo.setMicrocurriculo(microcurriculo);
                    seccionMicrocurriculoListNewSeccionMicrocurriculo = em.merge(seccionMicrocurriculoListNewSeccionMicrocurriculo);
                    if (oldMicrocurriculoOfSeccionMicrocurriculoListNewSeccionMicrocurriculo != null && !oldMicrocurriculoOfSeccionMicrocurriculoListNewSeccionMicrocurriculo.equals(microcurriculo)) {
                        oldMicrocurriculoOfSeccionMicrocurriculoListNewSeccionMicrocurriculo.getSeccionMicrocurriculoList().remove(seccionMicrocurriculoListNewSeccionMicrocurriculo);
                        oldMicrocurriculoOfSeccionMicrocurriculoListNewSeccionMicrocurriculo = em.merge(oldMicrocurriculoOfSeccionMicrocurriculoListNewSeccionMicrocurriculo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                MicrocurriculoPK id = microcurriculo.getMicrocurriculoPK();
                if (findMicrocurriculo(id) == null) {
                    throw new NonexistentEntityException("The microcurriculo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(MicrocurriculoPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Microcurriculo microcurriculo;
            try {
                microcurriculo = em.getReference(Microcurriculo.class, id);
                microcurriculo.getMicrocurriculoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The microcurriculo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<SeccionMicrocurriculo> seccionMicrocurriculoListOrphanCheck = microcurriculo.getSeccionMicrocurriculoList();
            for (SeccionMicrocurriculo seccionMicrocurriculoListOrphanCheckSeccionMicrocurriculo : seccionMicrocurriculoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Microcurriculo (" + microcurriculo + ") cannot be destroyed since the SeccionMicrocurriculo " + seccionMicrocurriculoListOrphanCheckSeccionMicrocurriculo + " in its seccionMicrocurriculoList field has a non-nullable microcurriculo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            AreaFormacion areaDeFormacionId = microcurriculo.getAreaDeFormacionId();
            if (areaDeFormacionId != null) {
                areaDeFormacionId.getMicrocurriculoList().remove(microcurriculo);
                areaDeFormacionId = em.merge(areaDeFormacionId);
            }
            Materia materia = microcurriculo.getMateria();
            if (materia != null) {
                materia.getMicrocurriculoList().remove(microcurriculo);
                materia = em.merge(materia);
            }
            em.remove(microcurriculo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Microcurriculo> findMicrocurriculoEntities() {
        return findMicrocurriculoEntities(true, -1, -1);
    }

    public List<Microcurriculo> findMicrocurriculoEntities(int maxResults, int firstResult) {
        return findMicrocurriculoEntities(false, maxResults, firstResult);
    }

    private List<Microcurriculo> findMicrocurriculoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Microcurriculo.class));
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

    public Microcurriculo findMicrocurriculo(MicrocurriculoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Microcurriculo.class, id);
        } finally {
            em.close();
        }
    }

    public int getMicrocurriculoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Microcurriculo> rt = cq.from(Microcurriculo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
