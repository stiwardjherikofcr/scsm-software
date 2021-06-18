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
import dto.Rol;
import dto.Docente;
import dto.Usuario;
import dto.UsuarioPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Manuel
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getUsuarioPK() == null) {
            usuario.setUsuarioPK(new UsuarioPK());
        }
        usuario.getUsuarioPK().setRolId(usuario.getRol().getId());
        usuario.getUsuarioPK().setDocenteCodigo(usuario.getDocente().getCodigoDocente());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rol rol = usuario.getRol();
            if (rol != null) {
                rol = em.getReference(rol.getClass(), rol.getId());
                usuario.setRol(rol);
            }
            Docente docente = usuario.getDocente();
            if (docente != null) {
                docente = em.getReference(docente.getClass(), docente.getCodigoDocente());
                usuario.setDocente(docente);
            }
            em.persist(usuario);
            if (rol != null) {
                rol.getUsuarioList().add(usuario);
                rol = em.merge(rol);
            }
            if (docente != null) {
                docente.getUsuarioList().add(usuario);
                docente = em.merge(docente);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getUsuarioPK()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        usuario.getUsuarioPK().setRolId(usuario.getRol().getId());
        usuario.getUsuarioPK().setDocenteCodigo(usuario.getDocente().getCodigoDocente());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getUsuarioPK());
            Rol rolOld = persistentUsuario.getRol();
            Rol rolNew = usuario.getRol();
            Docente docenteOld = persistentUsuario.getDocente();
            Docente docenteNew = usuario.getDocente();
            if (rolNew != null) {
                rolNew = em.getReference(rolNew.getClass(), rolNew.getId());
                usuario.setRol(rolNew);
            }
            if (docenteNew != null) {
                docenteNew = em.getReference(docenteNew.getClass(), docenteNew.getCodigoDocente());
                usuario.setDocente(docenteNew);
            }
            usuario = em.merge(usuario);
            if (rolOld != null && !rolOld.equals(rolNew)) {
                rolOld.getUsuarioList().remove(usuario);
                rolOld = em.merge(rolOld);
            }
            if (rolNew != null && !rolNew.equals(rolOld)) {
                rolNew.getUsuarioList().add(usuario);
                rolNew = em.merge(rolNew);
            }
            if (docenteOld != null && !docenteOld.equals(docenteNew)) {
                docenteOld.getUsuarioList().remove(usuario);
                docenteOld = em.merge(docenteOld);
            }
            if (docenteNew != null && !docenteNew.equals(docenteOld)) {
                docenteNew.getUsuarioList().add(usuario);
                docenteNew = em.merge(docenteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                UsuarioPK id = usuario.getUsuarioPK();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(UsuarioPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getUsuarioPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            Rol rol = usuario.getRol();
            if (rol != null) {
                rol.getUsuarioList().remove(usuario);
                rol = em.merge(rol);
            }
            Docente docente = usuario.getDocente();
            if (docente != null) {
                docente.getUsuarioList().remove(usuario);
                docente = em.merge(docente);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(UsuarioPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
