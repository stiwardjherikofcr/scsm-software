/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class TableHeader extends PdfPageEventHelper {

    private PdfPTable table;
    private float tableHeight;

    public TableHeader(PdfPTable tab) {
        table = tab;
        table.setTotalWidth(523);
        table.setLockedWidth(true);
        tableHeight = table.getTotalHeight();
    }

    public float getTableHeight() {
        return tableHeight;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        table.writeSelectedRows(0, -1,
                document.left(),
                document.top() + ((document.topMargin() + tableHeight) / 2),
                writer.getDirectContent());
    }
}
