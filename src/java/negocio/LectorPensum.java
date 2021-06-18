/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import dto.EquivalenciaMateria;
import dto.Materia;
import dto.MateriaPK;
import dto.PrerrequisitoMateria;
import dto.TipoAsignatura;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

/**
 *
 * @author dunke
 */
public class LectorPensum extends PDFTextStripper {
    private final String COL_NAMES[] = {"codigo", "nombre", "ht", "hp", "hti", "cr", "prereq", "si", "rc", "te", "equis"};
    private HashMap<String, TextPosition[]> encaPos;
    private List<HashMap<String, Object>> materias;

    public LectorPensum() throws IOException {
        this.encaPos = new HashMap<>();
        this.materias = new ArrayList<>();
    }

    /**
     *
     * @param filePath Ruta del archivo(pensum)
     */
    public void parsePDFDocument(String filePath) {
        try {
            PDDocument pdDocument = PDDocument.load(new File(filePath));
            this.setSortByPosition(true);
            Writer writer = new OutputStreamWriter(new ByteArrayOutputStream());
            this.writeText(pdDocument, writer);
            pdDocument.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     *
     * @param text Texto leido
     * @param textPositions Posición de cada letra de el texto leido
     * @throws IOException
     *
     * Se envia a this.add el texto, la posición de la primera y ultima letra de
     * la misma
     */
    @Override
    protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
        this.add(text, new TextPosition[]{textPositions.get(0), textPositions.get(textPositions.size() - 1)});
    }

    public void add(String text, TextPosition[] textP) {
        //Se pregunta si el texto es un encabezado
        if (this.esEnca(text)) {
            this.encaPos.put(text.toLowerCase(), textP);
            //El <771> es para que no lea(ignore) el footer del documento
        } else if (this.encaPos.size() > 0 && textP[0].getY() >= this.encaPos.get(COL_NAMES[0])[0].getY() + 18 && textP[0].getY() < 771) {

            //Se pregunta si es el inicio de una fila, si es asi inserte un nuevo HashMap
            if (textP[0].getX() < this.encaPos.get(COL_NAMES[0])[1].getEndX()) {
                HashMap<String, Object> h = new HashMap<>();

                h.put(COL_NAMES[0], text);
                //Se define la estructura del HashMap
                this.defineHashMap(h);

                this.materias.add(h);
            } else {
                //Basicamente mira en que columna cae el texto segun la posición de la primera letra
                this.ubicarTexto(text, textP[0]);
            }
        }
    }

    /**
     *
     * @param t Texto leido
     * @return True si el texto leido es un encabezado, false de lo contrario
     */
    private boolean esEnca(String text) {
        for (String col : this.COL_NAMES) {
            if (text.toLowerCase().equals(col)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param map HashMap al cual se le definira la estructura
     *
     * Con estructura se refiere a segun los diferentes tipos de columnas se
     * definiran los tipos de datos para cada una.<br>
     * <ul><li>Ej. map.put(COL_NAMES[i]="prerreq", new ArrayList) Ya que se
     * almacenaran varios datos en esa columna.</li>
     * <li>EEj. map.put(COL_NAMES[i]="nombre", "") Ya que solo se almacenara un
     * String en esa columna.</li></ul>
     * Comienza desde uno ya que antes de llamar este metodo se definio la
     * primera columna y se almaceno su dato.
     */
    private void defineHashMap(HashMap<String, Object> map) {
        for (int i = 1; i < COL_NAMES.length; i++) {
            //Como la columna <6> y <10> del documento pueden ser varios datos
            //se define para el hashmap en esa posicón un ArrayList
            if (i == 6 || i == 10) {
                map.put(COL_NAMES[i], new ArrayList<>());
            } else {
                map.put(COL_NAMES[i], "");
            }
        }
    }

    /**
     *
     * @param text Texto a ubicar
     * @param textP Posición de la primera letra del texto
     */
    private void ubicarTexto(String text, TextPosition textP) {
        for (int i = 1; i < COL_NAMES.length; i++) {
            if (textP.getX() < this.encaPos.get(COL_NAMES[i])[1].getEndX() && textP.getX() > this.encaPos.get(COL_NAMES[i - 1])[1].getEndX()) {
                if (i == 6 || i == 10) {
                    if (i == 6 || text.split("-").length == 2) {
                        ((ArrayList) this.materias.get(this.materias.size() - 1).get(COL_NAMES[i])).add(text);
                    } else if (i == 10) {
                        String ac = ((ArrayList) this.materias.get(this.materias.size() - 1).get(COL_NAMES[i])).get(((ArrayList) this.materias.get(this.materias.size() - 1).get(COL_NAMES[i])).size() - 1).toString() + " " + text;
                        ((ArrayList) this.materias.get(this.materias.size() - 1).get(COL_NAMES[i])).set(((ArrayList) this.materias.get(this.materias.size() - 1).get(COL_NAMES[i])).size() - 1, ac);
                    }
                } else {
                    this.materias.get(this.materias.size() - 1).put(COL_NAMES[i], this.materias.get(this.materias.size() - 1).get(COL_NAMES[i]).toString() + " " + text);
                }
                break;
            }
        }
    }

    //    0         1       2     3      4      5       6      7      8     9      10
    //{"codigo", "nombre", "ht", "hp", "hti", "cr", "prereq", "si", "rc", "te", "equis"}
    public List<Materia> getMaterias(Integer codigo_pensum) {
        List<Materia> materias_rs = new ArrayList<>();
        for (HashMap<String, Object> h : this.materias) {
            Integer prob = Integer.parseInt(h.get(COL_NAMES[0]).toString().replaceAll("\\s+", "").substring(4, 5));
            Integer semestre = prob == 0 ? 10 : prob;
            Integer codigo = Integer.parseInt(h.get(COL_NAMES[0]).toString().replaceAll("\\s+", ""));
            String nombre = h.get(COL_NAMES[1]).toString();
            Integer ht = campoValido(h.get(COL_NAMES[2]).toString().replaceAll("\\s+", ""));
            Integer hp = campoValido(h.get(COL_NAMES[3]).toString().replaceAll("\\s+", ""));
            Integer hti = campoValido(h.get(COL_NAMES[4]).toString().replaceAll("\\s+", ""));
            Integer creditos = campoValido(h.get(COL_NAMES[5]).toString().replaceAll("\\s+", ""));
            Integer cre = campoValido(h.get(COL_NAMES[8]).toString().replaceAll("\\s+", ""));
            Boolean type = h.get(COL_NAMES[9]).toString().replaceAll("\\s+", "").equalsIgnoreCase("X");
            Materia m = new Materia(new MateriaPK(codigo, codigo_pensum), nombre, creditos, semestre, ht, hp, hti);
            m.setTipoAsignaturaId(new TipoAsignatura(type ? 2 : 1));
            List<PrerrequisitoMateria> prerreq = this.formatPrerreq(m, codigo_pensum, ((ArrayList<String>) h.get(COL_NAMES[6])));
            List<EquivalenciaMateria> equis = this.formatEquis(m, ((ArrayList<String>) h.get(COL_NAMES[10])));
            m.setEquivalenciaMateriaList(equis);
            m.setPrerrequisitoMateriaList(prerreq);
            m.setCr(cre);
            materias_rs.add(m);
        }
        return materias_rs;
    }

    /**
     *
     * @param text Texto a validar
     * @return
     *
     */
    private Integer campoValido(String text) {
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }

    private List<EquivalenciaMateria> formatEquis(Materia parent, ArrayList<String> listEquis) {
        List<EquivalenciaMateria> equis = new ArrayList<>();
        for (String s : listEquis) {
            String data[] = s.split("-");
            EquivalenciaMateria eq = new EquivalenciaMateria();
            eq.setMateria(parent);
            eq.setEquivalenciaMateria(Integer.parseInt(data[0]));
            eq.setNombre(data[1]);
            equis.add(eq);
        }
        return equis;
    }

    private List<PrerrequisitoMateria> formatPrerreq(Materia parent, Integer codigo_pensum, ArrayList<String> listPrerreq) {
        List<PrerrequisitoMateria> prerreqForm = new ArrayList<>();
        for (String s : listPrerreq) {
            PrerrequisitoMateria pr = new PrerrequisitoMateria();
            pr.setMateria(parent);
            pr.setMateria1(new Materia(Integer.parseInt(s), codigo_pensum));
            prerreqForm.add(pr);
        }
        return prerreqForm;
    }
}
