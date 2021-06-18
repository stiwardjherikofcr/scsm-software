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
import dto.Departamento;
import dto.Docente;
import dto.Programa;
import java.util.ArrayList;
import java.util.List;
import dto.MateriaPeriodoGrupo;
import dto.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Manuel
 */
public class DocenteJpaController implements Serializable {

    public DocenteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Docente docente) throws PreexistingEntityException, Exception {
        if (docente.getProgramaList() == null) {
            docente.setProgramaList(new ArrayList<Programa>());
        }
        if (docente.getMateriaPeriodoGrupoList() == null) {
            docente.setMateriaPeriodoGrupoList(new ArrayList<MateriaPeriodoGrupo>());
        }
        if (docente.getUsuarioList() == null) {
            docente.setUsuarioList(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento departamentoId = docente.getDepartamentoId();
            if (departamentoId != null) {
                departamentoId = em.getReference(departamentoId.getClass(), departamentoId.getId());
                docente.setDepartamentoId(departamentoId);
            }
            List<Programa> attachedProgramaList = new ArrayList<Programa>();
            for (Programa programaListProgramaToAttach : docente.getProgramaList()) {
                programaListProgramaToAttach = em.getReference(programaListProgramaToAttach.getClass(), programaListProgramaToAttach.getCodigo());
                attachedProgramaList.add(programaListProgramaToAttach);
            }
            docente.setProgramaList(attachedProgramaList);
            List<MateriaPeriodoGrupo> attachedMateriaPeriodoGrupoList = new ArrayList<MateriaPeriodoGrupo>();
            for (MateriaPeriodoGrupo materiaPeriodoGrupoListMateriaPeriodoGrupoToAttach : docente.getMateriaPeriodoGrupoList()) {
                materiaPeriodoGrupoListMateriaPeriodoGrupoToAttach = em.getReference(materiaPeriodoGrupoListMateriaPeriodoGrupoToAttach.getClass(), materiaPeriodoGrupoListMateriaPeriodoGrupoToAttach.getMateriaPeriodoGrupoPK());
                attachedMateriaPeriodoGrupoList.add(materiaPeriodoGrupoListMateriaPeriodoGrupoToAttach);
            }
            docente.setMateriaPeriodoGrupoList(attachedMateriaPeriodoGrupoList);
            List<Usuario> attachedUsuarioList = new ArrayList<Usuario>();
            for (Usuario usuarioListUsuarioToAttach : docente.getUsuarioList()) {
                usuarioListUsuarioToAttach = em.getReference(usuarioListUsuarioToAttach.getClass(), usuarioListUsuarioToAttach.getUsuarioPK());
                attachedUsuarioList.add(usuarioListUsuarioToAttach);
            }
            docente.setUsuarioList(attachedUsuarioList);
            em.persist(docente);
            if (departamentoId != null) {
                departamentoId.getDocenteList().add(docente);
                departamentoId = em.merge(departamentoId);
            }
            for (Programa programaListPrograma : docente.getProgramaList()) {
                Docente oldDirectorProgramaOfProgramaListPrograma = programaListPrograma.getDirectorPrograma();
                programaListPrograma.setDirectorPrograma(docente);
                programaListPrograma = em.merge(programaListPrograma);
                if (oldDirectorProgramaOfProgramaListPrograma != null) {
                    oldDirectorProgramaOfProgramaListPrograma.getProgramaList().remove(programaListPrograma);
                    oldDirectorProgramaOfProgramaListPrograma = em.merge(oldDirectorProgramaOfProgramaListPrograma);
                }
            }
            for (MateriaPeriodoGrupo materiaPeriodoGrupoListMateriaPeriodoGrupo : docente.getMateriaPeriodoGrupoList()) {
                Docente oldDocenteOfMateriaPeriodoGrupoListMateriaPeriodoGrupo = materiaPeriodoGrupoListMateriaPeriodoGrupo.getDocente();
                materiaPeriodoGrupoListMateriaPeriodoGrupo.setDocente(docente);
                materiaPeriodoGrupoListMateriaPeriodoGrupo = em.merge(materiaPeriodoGrupoListMateriaPeriodoGrupo);
                if (oldDocenteOfMateriaPeriodoGrupoListMateriaPeriodoGrupo != null) {
                    oldDocenteOfMateriaPeriodoGrupoListMateriaPeriodoGrupo.getMateriaPeriodoGrupoList().remove(materiaPeriodoGrupoListMateriaPeriodoGrupo);
                    oldDocenteOfMateriaPeriodoGrupoListMateriaPeriodoGrupo = em.merge(oldDocenteOfMateriaPeriodoGrupoListMateriaPeriodoGrupo);
                }
            }
            for (Usuario usuarioListUsuario : docente.getUsuarioList()) {
                Docente oldDocenteOfUsuarioListUsuario = usuarioListUsuario.getDocente();
                usuarioListUsuario.setDocente(docente);
                usuarioListUsuario = em.merge(usuarioListUsuario);
                if (oldDocenteOfUsuarioListUsuario != null) {
                    oldDocenteOfUsuarioListUsuario.getUsuarioList().remove(usuarioListUsuario);
                    oldDocenteOfUsuarioListUsuario = em.merge(oldDocenteOfUsuarioListUsuario);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDocente(docente.getCodigoDocente()) != null) {
                throw new PreexistingEntityException("Docente " + docente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Docente docente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Docente persistentDocente = em.find(Docente.class, docente.getCodigoDocente());
            Departamento departamentoIdOld = persistentDocente.getDepartamentoId();
            Departamento departamentoIdNew = docente.getDepartamentoId();
            List<Programa> programaListOld = persistentDocente.getProgramaList();
            List<Programa> programaListNew = docente.getProgramaList();
            List<MateriaPeriodoGrupo> materiaPeriodoGrupoListOld = persistentDocente.getMateriaPeriodoGrupoList();
            List<MateriaPeriodoGrupo> materiaPeriodoGrupoListNew = docente.getMateriaPeriodoGrupoList();
            List<Usuario> usuarioListOld = persistentDocente.getUsuarioList();
            List<Usuario> usuarioListNew = docente.getUsuarioList();
            List<String> illegalOrphanMessages = null;
            for (Programa programaListOldPrograma : programaListOld) {
                if (!programaListNew.contains(programaListOldPrograma)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Programa " + programaListOldPrograma + " since its directorPrograma field is not nullable.");
                }
            }
            for (MateriaPeriodoGrupo materiaPeriodoGrupoListOldMateriaPeriodoGrupo : materiaPeriodoGrupoListOld) {
                if (!materiaPeriodoGrupoListNew.contains(materiaPeriodoGrupoListOldMateriaPeriodoGrupo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MateriaPeriodoGrupo " + materiaPeriodoGrupoListOldMateriaPeriodoGrupo + " since its docente field is not nullable.");
                }
            }
            for (Usuario usuarioListOldUsuario : usuarioListOld) {
                if (!usuarioListNew.contains(usuarioListOldUsuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usuario " + usuarioListOldUsuario + " since its docente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (departamentoIdNew != null) {
                departamentoIdNew = em.getReference(departamentoIdNew.getClass(), departamentoIdNew.getId());
                docente.setDepartamentoId(departamentoIdNew);
            }
            List<Programa> attachedProgramaListNew = new ArrayList<Programa>();
            for (Programa programaListNewProgramaToAttach : programaListNew) {
                programaListNewProgramaToAttach = em.getReference(programaListNewProgramaToAttach.getClass(), programaListNewProgramaToAttach.getCodigo());
                attachedProgramaListNew.add(programaListNewProgramaToAttach);
            }
            programaListNew = attachedProgramaListNew;
            docente.setProgramaList(programaListNew);
            List<MateriaPeriodoGrupo> attachedMateriaPeriodoGrupoListNew = new ArrayList<MateriaPeriodoGrupo>();
            for (MateriaPeriodoGrupo materiaPeriodoGrupoListNewMateriaPeriodoGrupoToAttach : materiaPeriodoGrupoListNew) {
                materiaPeriodoGrupoListNewMateriaPeriodoGrupoToAttach = em.getReference(materiaPeriodoGrupoListNewMateriaPeriodoGrupoToAttach.getClass(), materiaPeriodoGrupoListNewMateriaPeriodoGrupoToAttach.getMateriaPeriodoGrupoPK());
                attachedMateriaPeriodoGrupoListNew.add(materiaPeriodoGrupoListNewMateriaPeriodoGrupoToAttach);
            }
            materiaPeriodoGrupoListNew = attachedMateriaPeriodoGrupoListNew;
            docente.setMateriaPeriodoGrupoList(materiaPeriodoGrupoListNew);
            List<Usuario> attachedUsuarioListNew = new ArrayList<Usuario>();
            for (Usuario usuarioListNewUsuarioToAttach : usuarioListNew) {
                usuarioListNewUsuarioToAttach = em.getReference(usuarioListNewUsuarioToAttach.getClass(), usuarioListNewUsuarioToAttach.getUsuarioPK());
                attachedUsuarioListNew.add(usuarioListNewUsuarioToAttach);
            }
            usuarioListNew = attachedUsuarioListNew;
            docente.setUsuarioList(usuarioListNew);
            docente = em.merge(docente);
            if (departamentoIdOld != null && !departamentoIdOld.equals(departamentoIdNew)) {
                departamentoIdOld.getDocenteList().remove(docente);
                departamentoIdOld = em.merge(departamentoIdOld);
            }
            if (departamentoIdNew != null && !departamentoIdNew.equals(departamentoIdOld)) {
                departamentoIdNew.getDocenteList().add(docente);
                departamentoIdNew = em.merge(departamentoIdNew);
            }
            for (Programa programaListNewPrograma : programaListNew) {
                if (!programaListOld.contains(programaListNewPrograma)) {
                    Docente oldDirectorProgramaOfProgramaListNewPrograma = programaListNewPrograma.getDirectorPrograma();
                    programaListNewPrograma.setDirectorPrograma(docente);
                    programaListNewPrograma = em.merge(programaListNewPrograma);
                    if (oldDirectorProgramaOfProgramaListNewPrograma != null && !oldDirectorProgramaOfProgramaListNewPrograma.equals(docente)) {
                        oldDirectorProgramaOfProgramaListNewPrograma.getProgramaList().remove(programaListNewPrograma);
                        oldDirectorProgramaOfProgramaListNewPrograma = em.merge(oldDirectorProgramaOfProgramaListNewPrograma);
                    }
                }
            }
            for (MateriaPeriodoGrupo materiaPeriodoGrupoListNewMateriaPeriodoGrupo : materiaPeriodoGrupoListNew) {
                if (!materiaPeriodoGrupoListOld.contains(materiaPeriodoGrupoListNewMateriaPeriodoGrupo)) {
                    Docente oldDocenteOfMateriaPeriodoGrupoListNewMateriaPeriodoGrupo = materiaPeriodoGrupoListNewMateriaPeriodoGrupo.getDocente();
                    materiaPeriodoGrupoListNewMateriaPeriodoGrupo.setDocente(docente);
                    materiaPeriodoGrupoListNewMateriaPeriodoGrupo = em.merge(materiaPeriodoGrupoListNewMateriaPeriodoGrupo);
                    if (oldDocenteOfMateriaPeriodoGrupoListNewMateriaPeriodoGrupo != null && !oldDocenteOfMateriaPeriodoGrupoListNewMateriaPeriodoGrupo.equals(docente)) {
                        oldDocenteOfMateriaPeriodoGrupoListNewMateriaPeriodoGrupo.getMateriaPeriodoGrupoList().remove(materiaPeriodoGrupoListNewMateriaPeriodoGrupo);
                        oldDocenteOfMateriaPeriodoGrupoListNewMateriaPeriodoGrupo = em.merge(oldDocenteOfMateriaPeriodoGrupoListNewMateriaPeriodoGrupo);
                    }
                }
            }
            for (Usuario usuarioListNewUsuario : usuarioListNew) {
                if (!usuarioListOld.contains(usuarioListNewUsuario)) {
                    Docente oldDocenteOfUsuarioListNewUsuario = usuarioListNewUsuario.getDocente();
                    usuarioListNewUsuario.setDocente(docente);
                    usuarioListNewUsuario = em.merge(usuarioListNewUsuario);
                    if (oldDocenteOfUsuarioListNewUsuario != null && !oldDocenteOfUsuarioListNewUsuario.equals(docente)) {
                        oldDocenteOfUsuarioListNewUsuario.getUsuarioList().remove(usuarioListNewUsuario);
                        oldDocenteOfUsuarioListNewUsuario = em.merge(oldDocenteOfUsuarioListNewUsuario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = docente.getCodigoDocente();
                if (findDocente(id) == null) {
                    throw new NonexistentEntityException("The docente with id " + id + " no longer exists.");
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
            Docente docente;
            try {
                docente = em.getReference(Docente.class, id);
                docente.getCodigoDocente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The docente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Programa> programaListOrphanCheck = docente.getProgramaList();
            for (Programa programaListOrphanCheckPrograma : programaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Docente (" + docente + ") cannot be destroyed since the Programa " + programaListOrphanCheckPrograma + " in its programaList field has a non-nullable directorPrograma field.");
            }
            List<MateriaPeriodoGrupo> materiaPeriodoGrupoListOrphanCheck = docente.getMateriaPeriodoGrupoList();
            for (MateriaPeriodoGrupo materiaPeriodoGrupoListOrphanCheckMateriaPeriodoGrupo : materiaPeriodoGrupoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Docente (" + docente + ") cannot be destroyed since the MateriaPeriodoGrupo " + materiaPeriodoGrupoListOrphanCheckMateriaPeriodoGrupo + " in its materiaPeriodoGrupoList field has a non-nullable docente field.");
            }
            List<Usuario> usuarioListOrphanCheck = docente.getUsuarioList();
            for (Usuario usuarioListOrphanCheckUsuario : usuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Docente (" + docente + ") cannot be destroyed since the Usuario " + usuarioListOrphanCheckUsuario + " in its usuarioList field has a non-nullable docente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Departamento departamentoId = docente.getDepartamentoId();
            if (departamentoId != null) {
                departamentoId.getDocenteList().remove(docente);
                departamentoId = em.merge(departamentoId);
            }
            em.remove(docente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Docente> findDocenteEntities() {
        return findDocenteEntities(true, -1, -1);
    }

    public List<Docente> findDocenteEntities(int maxResults, int firstResult) {
        return findDocenteEntities(false, maxResults, firstResult);
    }

    private List<Docente> findDocenteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Docente.class));
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

    public Docente findDocente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Docente.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocenteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Docente> rt = cq.from(Docente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
