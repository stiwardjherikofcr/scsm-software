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
import dto.Pensum;
import dto.TipoAsignatura;
import dto.PrerrequisitoMateria;
import java.util.ArrayList;
import java.util.List;
import dto.EquivalenciaMateria;
import dto.Materia;
import dto.MateriaPK;
import dto.Microcurriculo;
import dto.MateriaPeriodo;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Manuel
 */
public class MateriaJpaController implements Serializable {

    public MateriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Materia materia) throws PreexistingEntityException, Exception {
        if (materia.getMateriaPK() == null) {
            materia.setMateriaPK(new MateriaPK());
        }
        if (materia.getPrerrequisitoMateriaList() == null) {
            materia.setPrerrequisitoMateriaList(new ArrayList<PrerrequisitoMateria>());
        }
        if (materia.getPrerrequisitoMateriaList1() == null) {
            materia.setPrerrequisitoMateriaList1(new ArrayList<PrerrequisitoMateria>());
        }
        if (materia.getEquivalenciaMateriaList() == null) {
            materia.setEquivalenciaMateriaList(new ArrayList<EquivalenciaMateria>());
        }
        if (materia.getMicrocurriculoList() == null) {
            materia.setMicrocurriculoList(new ArrayList<Microcurriculo>());
        }
        if (materia.getMateriaPeriodoList() == null) {
            materia.setMateriaPeriodoList(new ArrayList<MateriaPeriodo>());
        }
        materia.getMateriaPK().setPensumCodigo(materia.getPensum().getPensumPK().getCodigo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pensum pensum = materia.getPensum();
            if (pensum != null) {
                pensum = em.getReference(pensum.getClass(), pensum.getPensumPK());
                materia.setPensum(pensum);
            }
            TipoAsignatura tipoAsignaturaId = materia.getTipoAsignaturaId();
            if (tipoAsignaturaId != null) {
                tipoAsignaturaId = em.getReference(tipoAsignaturaId.getClass(), tipoAsignaturaId.getId());
                materia.setTipoAsignaturaId(tipoAsignaturaId);
            }
            List<PrerrequisitoMateria> attachedPrerrequisitoMateriaList = new ArrayList<PrerrequisitoMateria>();
            for (PrerrequisitoMateria prerrequisitoMateriaListPrerrequisitoMateriaToAttach : materia.getPrerrequisitoMateriaList()) {
                prerrequisitoMateriaListPrerrequisitoMateriaToAttach = em.getReference(prerrequisitoMateriaListPrerrequisitoMateriaToAttach.getClass(), prerrequisitoMateriaListPrerrequisitoMateriaToAttach.getId());
                attachedPrerrequisitoMateriaList.add(prerrequisitoMateriaListPrerrequisitoMateriaToAttach);
            }
            materia.setPrerrequisitoMateriaList(attachedPrerrequisitoMateriaList);
            List<PrerrequisitoMateria> attachedPrerrequisitoMateriaList1 = new ArrayList<PrerrequisitoMateria>();
            for (PrerrequisitoMateria prerrequisitoMateriaList1PrerrequisitoMateriaToAttach : materia.getPrerrequisitoMateriaList1()) {
                prerrequisitoMateriaList1PrerrequisitoMateriaToAttach = em.getReference(prerrequisitoMateriaList1PrerrequisitoMateriaToAttach.getClass(), prerrequisitoMateriaList1PrerrequisitoMateriaToAttach.getId());
                attachedPrerrequisitoMateriaList1.add(prerrequisitoMateriaList1PrerrequisitoMateriaToAttach);
            }
            materia.setPrerrequisitoMateriaList1(attachedPrerrequisitoMateriaList1);
            List<EquivalenciaMateria> attachedEquivalenciaMateriaList = new ArrayList<EquivalenciaMateria>();
            for (EquivalenciaMateria equivalenciaMateriaListEquivalenciaMateriaToAttach : materia.getEquivalenciaMateriaList()) {
                equivalenciaMateriaListEquivalenciaMateriaToAttach = em.getReference(equivalenciaMateriaListEquivalenciaMateriaToAttach.getClass(), equivalenciaMateriaListEquivalenciaMateriaToAttach.getId());
                attachedEquivalenciaMateriaList.add(equivalenciaMateriaListEquivalenciaMateriaToAttach);
            }
            materia.setEquivalenciaMateriaList(attachedEquivalenciaMateriaList);
            List<Microcurriculo> attachedMicrocurriculoList = new ArrayList<Microcurriculo>();
            for (Microcurriculo microcurriculoListMicrocurriculoToAttach : materia.getMicrocurriculoList()) {
                microcurriculoListMicrocurriculoToAttach = em.getReference(microcurriculoListMicrocurriculoToAttach.getClass(), microcurriculoListMicrocurriculoToAttach.getMicrocurriculoPK());
                attachedMicrocurriculoList.add(microcurriculoListMicrocurriculoToAttach);
            }
            materia.setMicrocurriculoList(attachedMicrocurriculoList);
            List<MateriaPeriodo> attachedMateriaPeriodoList = new ArrayList<MateriaPeriodo>();
            for (MateriaPeriodo materiaPeriodoListMateriaPeriodoToAttach : materia.getMateriaPeriodoList()) {
                materiaPeriodoListMateriaPeriodoToAttach = em.getReference(materiaPeriodoListMateriaPeriodoToAttach.getClass(), materiaPeriodoListMateriaPeriodoToAttach.getMateriaPeriodoPK());
                attachedMateriaPeriodoList.add(materiaPeriodoListMateriaPeriodoToAttach);
            }
            materia.setMateriaPeriodoList(attachedMateriaPeriodoList);
            em.persist(materia);
            if (pensum != null) {
                pensum.getMateriaList().add(materia);
                pensum = em.merge(pensum);
            }
            if (tipoAsignaturaId != null) {
                tipoAsignaturaId.getMateriaList().add(materia);
                tipoAsignaturaId = em.merge(tipoAsignaturaId);
            }
            for (PrerrequisitoMateria prerrequisitoMateriaListPrerrequisitoMateria : materia.getPrerrequisitoMateriaList()) {
                Materia oldMateriaOfPrerrequisitoMateriaListPrerrequisitoMateria = prerrequisitoMateriaListPrerrequisitoMateria.getMateria();
                prerrequisitoMateriaListPrerrequisitoMateria.setMateria(materia);
                prerrequisitoMateriaListPrerrequisitoMateria = em.merge(prerrequisitoMateriaListPrerrequisitoMateria);
                if (oldMateriaOfPrerrequisitoMateriaListPrerrequisitoMateria != null) {
                    oldMateriaOfPrerrequisitoMateriaListPrerrequisitoMateria.getPrerrequisitoMateriaList().remove(prerrequisitoMateriaListPrerrequisitoMateria);
                    oldMateriaOfPrerrequisitoMateriaListPrerrequisitoMateria = em.merge(oldMateriaOfPrerrequisitoMateriaListPrerrequisitoMateria);
                }
            }
            for (PrerrequisitoMateria prerrequisitoMateriaList1PrerrequisitoMateria : materia.getPrerrequisitoMateriaList1()) {
                Materia oldMateria1OfPrerrequisitoMateriaList1PrerrequisitoMateria = prerrequisitoMateriaList1PrerrequisitoMateria.getMateria1();
                prerrequisitoMateriaList1PrerrequisitoMateria.setMateria1(materia);
                prerrequisitoMateriaList1PrerrequisitoMateria = em.merge(prerrequisitoMateriaList1PrerrequisitoMateria);
                if (oldMateria1OfPrerrequisitoMateriaList1PrerrequisitoMateria != null) {
                    oldMateria1OfPrerrequisitoMateriaList1PrerrequisitoMateria.getPrerrequisitoMateriaList1().remove(prerrequisitoMateriaList1PrerrequisitoMateria);
                    oldMateria1OfPrerrequisitoMateriaList1PrerrequisitoMateria = em.merge(oldMateria1OfPrerrequisitoMateriaList1PrerrequisitoMateria);
                }
            }
            for (EquivalenciaMateria equivalenciaMateriaListEquivalenciaMateria : materia.getEquivalenciaMateriaList()) {
                Materia oldMateriaOfEquivalenciaMateriaListEquivalenciaMateria = equivalenciaMateriaListEquivalenciaMateria.getMateria();
                equivalenciaMateriaListEquivalenciaMateria.setMateria(materia);
                equivalenciaMateriaListEquivalenciaMateria = em.merge(equivalenciaMateriaListEquivalenciaMateria);
                if (oldMateriaOfEquivalenciaMateriaListEquivalenciaMateria != null) {
                    oldMateriaOfEquivalenciaMateriaListEquivalenciaMateria.getEquivalenciaMateriaList().remove(equivalenciaMateriaListEquivalenciaMateria);
                    oldMateriaOfEquivalenciaMateriaListEquivalenciaMateria = em.merge(oldMateriaOfEquivalenciaMateriaListEquivalenciaMateria);
                }
            }
            for (Microcurriculo microcurriculoListMicrocurriculo : materia.getMicrocurriculoList()) {
                Materia oldMateriaOfMicrocurriculoListMicrocurriculo = microcurriculoListMicrocurriculo.getMateria();
                microcurriculoListMicrocurriculo.setMateria(materia);
                microcurriculoListMicrocurriculo = em.merge(microcurriculoListMicrocurriculo);
                if (oldMateriaOfMicrocurriculoListMicrocurriculo != null) {
                    oldMateriaOfMicrocurriculoListMicrocurriculo.getMicrocurriculoList().remove(microcurriculoListMicrocurriculo);
                    oldMateriaOfMicrocurriculoListMicrocurriculo = em.merge(oldMateriaOfMicrocurriculoListMicrocurriculo);
                }
            }
            for (MateriaPeriodo materiaPeriodoListMateriaPeriodo : materia.getMateriaPeriodoList()) {
                Materia oldMateriaOfMateriaPeriodoListMateriaPeriodo = materiaPeriodoListMateriaPeriodo.getMateria();
                materiaPeriodoListMateriaPeriodo.setMateria(materia);
                materiaPeriodoListMateriaPeriodo = em.merge(materiaPeriodoListMateriaPeriodo);
                if (oldMateriaOfMateriaPeriodoListMateriaPeriodo != null) {
                    oldMateriaOfMateriaPeriodoListMateriaPeriodo.getMateriaPeriodoList().remove(materiaPeriodoListMateriaPeriodo);
                    oldMateriaOfMateriaPeriodoListMateriaPeriodo = em.merge(oldMateriaOfMateriaPeriodoListMateriaPeriodo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMateria(materia.getMateriaPK()) != null) {
                throw new PreexistingEntityException("Materia " + materia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Materia materia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        materia.getMateriaPK().setPensumCodigo(materia.getPensum().getPensumPK().getCodigo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materia persistentMateria = em.find(Materia.class, materia.getMateriaPK());
            Pensum pensumOld = persistentMateria.getPensum();
            Pensum pensumNew = materia.getPensum();
            TipoAsignatura tipoAsignaturaIdOld = persistentMateria.getTipoAsignaturaId();
            TipoAsignatura tipoAsignaturaIdNew = materia.getTipoAsignaturaId();
            List<PrerrequisitoMateria> prerrequisitoMateriaListOld = persistentMateria.getPrerrequisitoMateriaList();
            List<PrerrequisitoMateria> prerrequisitoMateriaListNew = materia.getPrerrequisitoMateriaList();
            List<PrerrequisitoMateria> prerrequisitoMateriaList1Old = persistentMateria.getPrerrequisitoMateriaList1();
            List<PrerrequisitoMateria> prerrequisitoMateriaList1New = materia.getPrerrequisitoMateriaList1();
            List<EquivalenciaMateria> equivalenciaMateriaListOld = persistentMateria.getEquivalenciaMateriaList();
            List<EquivalenciaMateria> equivalenciaMateriaListNew = materia.getEquivalenciaMateriaList();
            List<Microcurriculo> microcurriculoListOld = persistentMateria.getMicrocurriculoList();
            List<Microcurriculo> microcurriculoListNew = materia.getMicrocurriculoList();
            List<MateriaPeriodo> materiaPeriodoListOld = persistentMateria.getMateriaPeriodoList();
            List<MateriaPeriodo> materiaPeriodoListNew = materia.getMateriaPeriodoList();
            List<String> illegalOrphanMessages = null;
            for (PrerrequisitoMateria prerrequisitoMateriaListOldPrerrequisitoMateria : prerrequisitoMateriaListOld) {
                if (!prerrequisitoMateriaListNew.contains(prerrequisitoMateriaListOldPrerrequisitoMateria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PrerrequisitoMateria " + prerrequisitoMateriaListOldPrerrequisitoMateria + " since its materia field is not nullable.");
                }
            }
            for (PrerrequisitoMateria prerrequisitoMateriaList1OldPrerrequisitoMateria : prerrequisitoMateriaList1Old) {
                if (!prerrequisitoMateriaList1New.contains(prerrequisitoMateriaList1OldPrerrequisitoMateria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PrerrequisitoMateria " + prerrequisitoMateriaList1OldPrerrequisitoMateria + " since its materia1 field is not nullable.");
                }
            }
            for (EquivalenciaMateria equivalenciaMateriaListOldEquivalenciaMateria : equivalenciaMateriaListOld) {
                if (!equivalenciaMateriaListNew.contains(equivalenciaMateriaListOldEquivalenciaMateria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EquivalenciaMateria " + equivalenciaMateriaListOldEquivalenciaMateria + " since its materia field is not nullable.");
                }
            }
            for (Microcurriculo microcurriculoListOldMicrocurriculo : microcurriculoListOld) {
                if (!microcurriculoListNew.contains(microcurriculoListOldMicrocurriculo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Microcurriculo " + microcurriculoListOldMicrocurriculo + " since its materia field is not nullable.");
                }
            }
            for (MateriaPeriodo materiaPeriodoListOldMateriaPeriodo : materiaPeriodoListOld) {
                if (!materiaPeriodoListNew.contains(materiaPeriodoListOldMateriaPeriodo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MateriaPeriodo " + materiaPeriodoListOldMateriaPeriodo + " since its materia field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pensumNew != null) {
                pensumNew = em.getReference(pensumNew.getClass(), pensumNew.getPensumPK());
                materia.setPensum(pensumNew);
            }
            if (tipoAsignaturaIdNew != null) {
                tipoAsignaturaIdNew = em.getReference(tipoAsignaturaIdNew.getClass(), tipoAsignaturaIdNew.getId());
                materia.setTipoAsignaturaId(tipoAsignaturaIdNew);
            }
            List<PrerrequisitoMateria> attachedPrerrequisitoMateriaListNew = new ArrayList<PrerrequisitoMateria>();
            for (PrerrequisitoMateria prerrequisitoMateriaListNewPrerrequisitoMateriaToAttach : prerrequisitoMateriaListNew) {
                prerrequisitoMateriaListNewPrerrequisitoMateriaToAttach = em.getReference(prerrequisitoMateriaListNewPrerrequisitoMateriaToAttach.getClass(), prerrequisitoMateriaListNewPrerrequisitoMateriaToAttach.getId());
                attachedPrerrequisitoMateriaListNew.add(prerrequisitoMateriaListNewPrerrequisitoMateriaToAttach);
            }
            prerrequisitoMateriaListNew = attachedPrerrequisitoMateriaListNew;
            materia.setPrerrequisitoMateriaList(prerrequisitoMateriaListNew);
            List<PrerrequisitoMateria> attachedPrerrequisitoMateriaList1New = new ArrayList<PrerrequisitoMateria>();
            for (PrerrequisitoMateria prerrequisitoMateriaList1NewPrerrequisitoMateriaToAttach : prerrequisitoMateriaList1New) {
                prerrequisitoMateriaList1NewPrerrequisitoMateriaToAttach = em.getReference(prerrequisitoMateriaList1NewPrerrequisitoMateriaToAttach.getClass(), prerrequisitoMateriaList1NewPrerrequisitoMateriaToAttach.getId());
                attachedPrerrequisitoMateriaList1New.add(prerrequisitoMateriaList1NewPrerrequisitoMateriaToAttach);
            }
            prerrequisitoMateriaList1New = attachedPrerrequisitoMateriaList1New;
            materia.setPrerrequisitoMateriaList1(prerrequisitoMateriaList1New);
            List<EquivalenciaMateria> attachedEquivalenciaMateriaListNew = new ArrayList<EquivalenciaMateria>();
            for (EquivalenciaMateria equivalenciaMateriaListNewEquivalenciaMateriaToAttach : equivalenciaMateriaListNew) {
                equivalenciaMateriaListNewEquivalenciaMateriaToAttach = em.getReference(equivalenciaMateriaListNewEquivalenciaMateriaToAttach.getClass(), equivalenciaMateriaListNewEquivalenciaMateriaToAttach.getId());
                attachedEquivalenciaMateriaListNew.add(equivalenciaMateriaListNewEquivalenciaMateriaToAttach);
            }
            equivalenciaMateriaListNew = attachedEquivalenciaMateriaListNew;
            materia.setEquivalenciaMateriaList(equivalenciaMateriaListNew);
            List<Microcurriculo> attachedMicrocurriculoListNew = new ArrayList<Microcurriculo>();
            for (Microcurriculo microcurriculoListNewMicrocurriculoToAttach : microcurriculoListNew) {
                microcurriculoListNewMicrocurriculoToAttach = em.getReference(microcurriculoListNewMicrocurriculoToAttach.getClass(), microcurriculoListNewMicrocurriculoToAttach.getMicrocurriculoPK());
                attachedMicrocurriculoListNew.add(microcurriculoListNewMicrocurriculoToAttach);
            }
            microcurriculoListNew = attachedMicrocurriculoListNew;
            materia.setMicrocurriculoList(microcurriculoListNew);
            List<MateriaPeriodo> attachedMateriaPeriodoListNew = new ArrayList<MateriaPeriodo>();
            for (MateriaPeriodo materiaPeriodoListNewMateriaPeriodoToAttach : materiaPeriodoListNew) {
                materiaPeriodoListNewMateriaPeriodoToAttach = em.getReference(materiaPeriodoListNewMateriaPeriodoToAttach.getClass(), materiaPeriodoListNewMateriaPeriodoToAttach.getMateriaPeriodoPK());
                attachedMateriaPeriodoListNew.add(materiaPeriodoListNewMateriaPeriodoToAttach);
            }
            materiaPeriodoListNew = attachedMateriaPeriodoListNew;
            materia.setMateriaPeriodoList(materiaPeriodoListNew);
            materia = em.merge(materia);
            if (pensumOld != null && !pensumOld.equals(pensumNew)) {
                pensumOld.getMateriaList().remove(materia);
                pensumOld = em.merge(pensumOld);
            }
            if (pensumNew != null && !pensumNew.equals(pensumOld)) {
                pensumNew.getMateriaList().add(materia);
                pensumNew = em.merge(pensumNew);
            }
            if (tipoAsignaturaIdOld != null && !tipoAsignaturaIdOld.equals(tipoAsignaturaIdNew)) {
                tipoAsignaturaIdOld.getMateriaList().remove(materia);
                tipoAsignaturaIdOld = em.merge(tipoAsignaturaIdOld);
            }
            if (tipoAsignaturaIdNew != null && !tipoAsignaturaIdNew.equals(tipoAsignaturaIdOld)) {
                tipoAsignaturaIdNew.getMateriaList().add(materia);
                tipoAsignaturaIdNew = em.merge(tipoAsignaturaIdNew);
            }
            for (PrerrequisitoMateria prerrequisitoMateriaListNewPrerrequisitoMateria : prerrequisitoMateriaListNew) {
                if (!prerrequisitoMateriaListOld.contains(prerrequisitoMateriaListNewPrerrequisitoMateria)) {
                    Materia oldMateriaOfPrerrequisitoMateriaListNewPrerrequisitoMateria = prerrequisitoMateriaListNewPrerrequisitoMateria.getMateria();
                    prerrequisitoMateriaListNewPrerrequisitoMateria.setMateria(materia);
                    prerrequisitoMateriaListNewPrerrequisitoMateria = em.merge(prerrequisitoMateriaListNewPrerrequisitoMateria);
                    if (oldMateriaOfPrerrequisitoMateriaListNewPrerrequisitoMateria != null && !oldMateriaOfPrerrequisitoMateriaListNewPrerrequisitoMateria.equals(materia)) {
                        oldMateriaOfPrerrequisitoMateriaListNewPrerrequisitoMateria.getPrerrequisitoMateriaList().remove(prerrequisitoMateriaListNewPrerrequisitoMateria);
                        oldMateriaOfPrerrequisitoMateriaListNewPrerrequisitoMateria = em.merge(oldMateriaOfPrerrequisitoMateriaListNewPrerrequisitoMateria);
                    }
                }
            }
            for (PrerrequisitoMateria prerrequisitoMateriaList1NewPrerrequisitoMateria : prerrequisitoMateriaList1New) {
                if (!prerrequisitoMateriaList1Old.contains(prerrequisitoMateriaList1NewPrerrequisitoMateria)) {
                    Materia oldMateria1OfPrerrequisitoMateriaList1NewPrerrequisitoMateria = prerrequisitoMateriaList1NewPrerrequisitoMateria.getMateria1();
                    prerrequisitoMateriaList1NewPrerrequisitoMateria.setMateria1(materia);
                    prerrequisitoMateriaList1NewPrerrequisitoMateria = em.merge(prerrequisitoMateriaList1NewPrerrequisitoMateria);
                    if (oldMateria1OfPrerrequisitoMateriaList1NewPrerrequisitoMateria != null && !oldMateria1OfPrerrequisitoMateriaList1NewPrerrequisitoMateria.equals(materia)) {
                        oldMateria1OfPrerrequisitoMateriaList1NewPrerrequisitoMateria.getPrerrequisitoMateriaList1().remove(prerrequisitoMateriaList1NewPrerrequisitoMateria);
                        oldMateria1OfPrerrequisitoMateriaList1NewPrerrequisitoMateria = em.merge(oldMateria1OfPrerrequisitoMateriaList1NewPrerrequisitoMateria);
                    }
                }
            }
            for (EquivalenciaMateria equivalenciaMateriaListNewEquivalenciaMateria : equivalenciaMateriaListNew) {
                if (!equivalenciaMateriaListOld.contains(equivalenciaMateriaListNewEquivalenciaMateria)) {
                    Materia oldMateriaOfEquivalenciaMateriaListNewEquivalenciaMateria = equivalenciaMateriaListNewEquivalenciaMateria.getMateria();
                    equivalenciaMateriaListNewEquivalenciaMateria.setMateria(materia);
                    equivalenciaMateriaListNewEquivalenciaMateria = em.merge(equivalenciaMateriaListNewEquivalenciaMateria);
                    if (oldMateriaOfEquivalenciaMateriaListNewEquivalenciaMateria != null && !oldMateriaOfEquivalenciaMateriaListNewEquivalenciaMateria.equals(materia)) {
                        oldMateriaOfEquivalenciaMateriaListNewEquivalenciaMateria.getEquivalenciaMateriaList().remove(equivalenciaMateriaListNewEquivalenciaMateria);
                        oldMateriaOfEquivalenciaMateriaListNewEquivalenciaMateria = em.merge(oldMateriaOfEquivalenciaMateriaListNewEquivalenciaMateria);
                    }
                }
            }
            for (Microcurriculo microcurriculoListNewMicrocurriculo : microcurriculoListNew) {
                if (!microcurriculoListOld.contains(microcurriculoListNewMicrocurriculo)) {
                    Materia oldMateriaOfMicrocurriculoListNewMicrocurriculo = microcurriculoListNewMicrocurriculo.getMateria();
                    microcurriculoListNewMicrocurriculo.setMateria(materia);
                    microcurriculoListNewMicrocurriculo = em.merge(microcurriculoListNewMicrocurriculo);
                    if (oldMateriaOfMicrocurriculoListNewMicrocurriculo != null && !oldMateriaOfMicrocurriculoListNewMicrocurriculo.equals(materia)) {
                        oldMateriaOfMicrocurriculoListNewMicrocurriculo.getMicrocurriculoList().remove(microcurriculoListNewMicrocurriculo);
                        oldMateriaOfMicrocurriculoListNewMicrocurriculo = em.merge(oldMateriaOfMicrocurriculoListNewMicrocurriculo);
                    }
                }
            }
            for (MateriaPeriodo materiaPeriodoListNewMateriaPeriodo : materiaPeriodoListNew) {
                if (!materiaPeriodoListOld.contains(materiaPeriodoListNewMateriaPeriodo)) {
                    Materia oldMateriaOfMateriaPeriodoListNewMateriaPeriodo = materiaPeriodoListNewMateriaPeriodo.getMateria();
                    materiaPeriodoListNewMateriaPeriodo.setMateria(materia);
                    materiaPeriodoListNewMateriaPeriodo = em.merge(materiaPeriodoListNewMateriaPeriodo);
                    if (oldMateriaOfMateriaPeriodoListNewMateriaPeriodo != null && !oldMateriaOfMateriaPeriodoListNewMateriaPeriodo.equals(materia)) {
                        oldMateriaOfMateriaPeriodoListNewMateriaPeriodo.getMateriaPeriodoList().remove(materiaPeriodoListNewMateriaPeriodo);
                        oldMateriaOfMateriaPeriodoListNewMateriaPeriodo = em.merge(oldMateriaOfMateriaPeriodoListNewMateriaPeriodo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                MateriaPK id = materia.getMateriaPK();
                if (findMateria(id) == null) {
                    throw new NonexistentEntityException("The materia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(MateriaPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materia materia;
            try {
                materia = em.getReference(Materia.class, id);
                materia.getMateriaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The materia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PrerrequisitoMateria> prerrequisitoMateriaListOrphanCheck = materia.getPrerrequisitoMateriaList();
            for (PrerrequisitoMateria prerrequisitoMateriaListOrphanCheckPrerrequisitoMateria : prerrequisitoMateriaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Materia (" + materia + ") cannot be destroyed since the PrerrequisitoMateria " + prerrequisitoMateriaListOrphanCheckPrerrequisitoMateria + " in its prerrequisitoMateriaList field has a non-nullable materia field.");
            }
            List<PrerrequisitoMateria> prerrequisitoMateriaList1OrphanCheck = materia.getPrerrequisitoMateriaList1();
            for (PrerrequisitoMateria prerrequisitoMateriaList1OrphanCheckPrerrequisitoMateria : prerrequisitoMateriaList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Materia (" + materia + ") cannot be destroyed since the PrerrequisitoMateria " + prerrequisitoMateriaList1OrphanCheckPrerrequisitoMateria + " in its prerrequisitoMateriaList1 field has a non-nullable materia1 field.");
            }
            List<EquivalenciaMateria> equivalenciaMateriaListOrphanCheck = materia.getEquivalenciaMateriaList();
            for (EquivalenciaMateria equivalenciaMateriaListOrphanCheckEquivalenciaMateria : equivalenciaMateriaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Materia (" + materia + ") cannot be destroyed since the EquivalenciaMateria " + equivalenciaMateriaListOrphanCheckEquivalenciaMateria + " in its equivalenciaMateriaList field has a non-nullable materia field.");
            }
            List<Microcurriculo> microcurriculoListOrphanCheck = materia.getMicrocurriculoList();
            for (Microcurriculo microcurriculoListOrphanCheckMicrocurriculo : microcurriculoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Materia (" + materia + ") cannot be destroyed since the Microcurriculo " + microcurriculoListOrphanCheckMicrocurriculo + " in its microcurriculoList field has a non-nullable materia field.");
            }
            List<MateriaPeriodo> materiaPeriodoListOrphanCheck = materia.getMateriaPeriodoList();
            for (MateriaPeriodo materiaPeriodoListOrphanCheckMateriaPeriodo : materiaPeriodoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Materia (" + materia + ") cannot be destroyed since the MateriaPeriodo " + materiaPeriodoListOrphanCheckMateriaPeriodo + " in its materiaPeriodoList field has a non-nullable materia field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Pensum pensum = materia.getPensum();
            if (pensum != null) {
                pensum.getMateriaList().remove(materia);
                pensum = em.merge(pensum);
            }
            TipoAsignatura tipoAsignaturaId = materia.getTipoAsignaturaId();
            if (tipoAsignaturaId != null) {
                tipoAsignaturaId.getMateriaList().remove(materia);
                tipoAsignaturaId = em.merge(tipoAsignaturaId);
            }
            em.remove(materia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Materia> findMateriaEntities() {
        return findMateriaEntities(true, -1, -1);
    }

    public List<Materia> findMateriaEntities(int maxResults, int firstResult) {
        return findMateriaEntities(false, maxResults, firstResult);
    }

    private List<Materia> findMateriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Materia.class));
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

    public Materia findMateria(MateriaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Materia.class, id);
        } finally {
            em.close();
        }
    }

    public int getMateriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Materia> rt = cq.from(Materia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
