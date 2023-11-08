/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfwriter;

// Adding table in a pdf using java
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
  
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import dynamichashtable.HashTable;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import libraryjobscheduler.ApplicationSettings;
import libraryjobscheduler.Employee;
import libraryjobscheduler.Timetable;

/**
 *
 * @author b-abi-karam
 */
public class PDFWriter
{
    
    public static void createPDFTimetable(String file, HashTable<Employee, Timetable> tables)
    {
        try 
        {
            ApplicationSettings as = ApplicationSettings.getInstance();
            
            int dayLength = as.getSlotCount();
            
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(file));
            Document doc = new Document(pdfDoc);
            
            Table table = new Table(tables.length()+1);
            
            table.addCell(new Cell(2,1).add(new Paragraph("Slots")));
            table.addCell(new Cell(1,tables.getKeys().length()).add(new Paragraph("Employees")));
            for (int i = 0; i < tables.length(); i++)
            {
                Employee emp = tables.getKeys().get(i);
                table.addCell(new Cell().add(new Paragraph(emp.getFirstName() + " " + emp.getLastName())));
            }
            for (int i = 0; i < dayLength; i++)
            {   table.addCell(new Cell().add(new Paragraph((String.valueOf(i+1)))));
                for (int j = 0; j < tables.length(); j++)
                {
                    Timetable tt = tables.item(tables.getKeys().get(j));
                    if (tt.getSlot(i).getID() != -1)
                    {
                        table.addCell(new Cell().add(new Paragraph(tt.getSlot(i).getName())));
                    }
                    else
                    {
                        table.addCell(new Cell().add(new Paragraph("Free")));
                    }
                }
            }
            doc.add(table);
            doc.close();
            System.out.println("Table created successfully...");
            
        } catch (FileNotFoundException ex) {
            System.out.println("Error Encountered.");
            Logger.getLogger(PDFWriter.class.getName()).log(Level.WARNING, null, ex);
        }
        
    }
}
