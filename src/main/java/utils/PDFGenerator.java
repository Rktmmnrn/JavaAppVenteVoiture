/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import static com.fasterxml.jackson.databind.cfg.CoercionInputShape.String;
import dao.AchatDAO;
import model.Facture;

import java.io.FileNotFoundException;
import java.util.Date;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

public class PDFGenerator {
    public static void genPDF(String numachat) throws FileNotFoundException {
        LocalDate date = null;
        String email = null;
        int prix = 0;
        int qte = 0;
        String design = null;
        String nomCli = null;

        try {
            List<Facture> achat = new AchatDAO().factureAchat(numachat);
            for (Facture f : achat) {
                numachat = f.getNumachat();
                nomCli = f.getNom();
                date = f.getDate();
                email = f.getEmail();
                design = f.getDesign();
                qte = f.getQte();
                prix = f.getPrix();
            }

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("/home/fenohery/NetBeansProjects/newAppVenteVoiture/pdf/Facture_"+numachat+".pdf"));
            document.open();
            Paragraph titre = new Paragraph("Facture N° "+numachat, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK));
            titre.setAlignment(Element.ALIGN_CENTER);
            document.add(titre);
            document.add(new Paragraph(""));
            document.add(new Paragraph("Date de facture : " +date));
            document.add(new Paragraph("Nom du Client : " +nomCli));
            document.add(new Paragraph("email du Client : " +email));

            PdfPTable table = new PdfPTable(4); // tableau à 4 colonne
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            //table.setWidths(new float[] {2, 5, 3}); // largeur colonne
            
            PdfPCell cell; // entête du tableau
            cell = new PdfPCell(new Phrase("Désignation"));
            //cell.setBackgroundColor(BaseColor.LIGHT_GRAY); // backgroundColor tableau
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Quantité"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Prix Unitaire"));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Total"));
            table.addCell(cell);

            table.addCell(design); // 1er ligne
            table.addCell(""+qte);
            table.addCell(""+qte);
            table.addCell(""+prix*qte);
            //table.addCell("2"); // 2em ligne
            //table.addCell("2_1");
            //table.addCell("2_2");
            //table.addCell("2_3");

            document.add(table);
            document.add(new Paragraph("Arrêté par la présente facture à la somme de "));

            document.close();
            System.out.println("PDF générer succes...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
