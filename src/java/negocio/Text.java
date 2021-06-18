/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.List;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author dunke
 */
public class Text {

    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPTable table = new PdfPTable(5);
        table.setWidths(new int[]{1, 2, 1, 1, 1});
        table.addCell(createCell("SKU", 2, 1, Element.ALIGN_LEFT));
        table.addCell(createCell("Description", 2, 1, Element.ALIGN_LEFT));
        table.addCell(createCell("Unit Price", 2, 1, Element.ALIGN_LEFT));
        table.addCell(createCell("Quantity", 2, 1, Element.ALIGN_LEFT));
        table.addCell(createCell("Extension", 2, 1, Element.ALIGN_LEFT));
        String[][] data = {
            {"ABC123", "The descriptive text may be more than one line and the text should wrap automatically", "$5.00", "10", "$50.00"},
            {"QRS557", "Another description", "$100.00", "15", "$1,500.00"},
            {"XYZ999", "Some stuff", "$1.00", "2", "$2.00"}
        };
        for (String[] row : data) {
            table.addCell(createCell(row[0], 1, 1, Element.ALIGN_LEFT));
            table.addCell(createCell(row[1], 1, 1, Element.ALIGN_LEFT));
            table.addCell(createCell(row[2], 1, 1, Element.ALIGN_RIGHT));
            table.addCell(createCell(row[3], 1, 1, Element.ALIGN_RIGHT));
            table.addCell(createCell(row[4], 1, 1, Element.ALIGN_RIGHT));
        }
        table.addCell(createCell("Totals", 2, 4, Element.ALIGN_LEFT));
        table.addCell(createCell("$1,552.00", 2, 1, Element.ALIGN_RIGHT));
        document.add(table);
        document.close();
    }

    public PdfPCell createCell(String content, float borderWidth, int colspan, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(content));
        cell.setBorderWidth(borderWidth);
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    protected void manipulatePdf(String dest) throws Exception {
    }

    public static void main(String[] args) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("micro.pdf"));
        document.open();
        document.add(new Paragraph("Numbered list with automatic indentation"));
        List list1 = new List(List.ORDERED);
        list1.setFirst(8);
        for (int i = 0; i < 5; i++) {
            list1.add("item");
        }
        document.add(list1);
        document.add(new Paragraph("Numbered list without indentation"));
        List list2 = new List(List.ORDERED);
        list2.setFirst(8);
        list2.setAlignindent(false);
        for (int i = 0; i < 5; i++) {
            list2.add("item");
        }
        document.add(list2);
        document.add(new Paragraph("Nested numbered list"));
        List list3 = new List(List.ORDERED);
        list3.setFirst(8);
        list3.setAlignindent(false);
        list3.setPostSymbol(" ");
        for (int i = 0; i < 5; i++) {
            list3.add("item");
            List list = new List(List.ORDERED);
            list.setPreSymbol(String.valueOf(8 + i) + "_");
            list.setPostSymbol(" ");
            list.add("item 1");
            list.add("item 2");
            list3.add(list);
        }
        document.add(list3);
        document.close();
    }
}
