package Servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

/**
 * Servlet implementation class FatturazionePDF
 */
@WebServlet("/FatturazionePDF")
public class FatturazionePDF extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PdfPCell cell;
	
	private final int colSpanQuantita = 2;
	private final int colSpanDescrizione = 15;
	private final int colSpanImporto = 3;
	
	private String nomeRistorante = "Nome Ristorante";
	private String intestazioneRistorante = "Intestazione RISTORANTE VIA ETC ETC ETC";
	private String numeroIncrementaleRicevutaFattura = "1234";
	private String data = "21/12/2011";
	private String imponibile = "1234 €";
	private String percentualeIVA = "23";
	private String imposta = "1234€";
	private String corrispettivoPagato = "1234€";
	private String corrispettivoNonPagato = "1234€";
	private String totale = "1234€";
	
	private final String TAB = "        ";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FatturazionePDF() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/pdf"); // Code 1
		Document document = new Document(PageSize.A4);
		GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
		
		try{
			PdfWriter.getInstance(document,response.getOutputStream()); // Code 2
			//  PdfWriter.getInstance(document, new FileOutputStream(filename));
			
			document.setMargins(40, 40, 50, 50);
	        document.setMarginMirroring(true);
			
			document.open();
			
			document.add(createTabellaRicevutaFiscale());
			
//			document.add(	new Paragraph(nomeRistorante)	);
//			document.add(	new Paragraph(intestazioneRistorante)	);
//			document.add(	new Paragraph("\n\n")	);
//			
//			document.add(createFirstTable());
			
			
			
//			document.add(	new Paragraph(nomeRistorante)	);
//			document.add(	new Paragraph(intestazioneRistorante)	);
//			document.add(	new Paragraph("\n\n")	);
			
//			// Code 3
//			PdfPTable table = new PdfPTable( new float[]{ 2, 10, 3 } );
//			Font f = new Font();
//	        f.setColor(BaseColor.WHITE);
//	        f.setSize(20);
//			cell = new PdfPCell(new Phrase(  "RICEVUTA/FATTURA FISCALE" ,f));/*calendar.getTime().toString()*/
//	        cell.setBackgroundColor(BaseColor.BLACK);
//	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//	        cell.setColspan(7);
//	        table.addCell(cell);
//			
//			table.getDefaultCell().setBorder(Rectangle.BOTTOM);
//			table.addCell("Q.TA'");
//			table.addCell("DESCRIZIONE DEL BENE O SERVIZIO");
//			table.addCell("IMPORTO");
//			for(int i=0; i<3; i++) table.addCell("");
//			table.addCell("Kissalini\n\t   pieni\n\t   di\n\t   pane");
//			table.addCell("2");
//			table.addCell("3");
//			for(int i=0; i<66; i++){
//				if(i%3 == 2){
//					Phrase tmp = new Phrase(String.valueOf(i)+" €");
//					cell = new PdfPCell( tmp );
//					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//					table.addCell(cell);
//				}else{
//					table.addCell(String.valueOf(i));
//				}
//				
//			}
//			table.addCell("1");
//			table.addCell("2");
//			table.addCell("3");
//			table.addCell("4");
//			table.addCell("5");
//			table.addCell("6");		
//			
//			//Riga vuota
//			cell = new PdfPCell();
//			cell.setColspan(3);
//			table.addCell(cell);
//			//Riga totale
//			cell = new PdfPCell(new Phrase("Totale"));
//			cell.setColspan(3);
//			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			table.addCell(cell);
//			//Riga valore totale
//			cell = new PdfPCell(new Phrase("30€"));
//			cell.setColspan(3);
//			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			table.addCell(cell);
			
			// Code 4
//			document.add(table);		
			document.close(); 
		}catch(DocumentException e){
			e.printStackTrace();
		}
		
	}
	
	public static PdfPTable createFirstTable() {
    	// a table with three columns
        PdfPTable table = new PdfPTable( new float[]{ 2, 1, 1 } );
        // the cell object
        PdfPCell cell;
        // we add a cell with colspan 3
        cell = new PdfPCell(new Phrase("Cell with colspan 3"));
        cell.setColspan(3);
        table.addCell(cell);
        // now we add a cell with rowspan 2
        cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
        cell.setRowspan(2);
        table.addCell(cell);
        // we add the four remaining cells with addCell()
        table.addCell("row 1; cell 1");
        table.addCell("row 1; cell 2");
        table.addCell("row 2; cell 1");
        table.addCell("row 2; cell 2");
        return table;
    }
	
	public PdfPTable createTabellaRicevutaFiscale() {
		PdfPTable table = new PdfPTable( 20 );
		PdfPCell cell;
		Phrase tmp_phrase;
        
        //Aggiungo la scritta Ricevuta Fiscale
        Font f = new Font();
        f.setColor(BaseColor.WHITE);
        f.setSize(20);
		cell = new PdfPCell(new Phrase(  "RICEVUTA / FATTURA FISCALE" ,f));/*calendar.getTime().toString()*/
        cell.setBackgroundColor(BaseColor.BLACK);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(20);
        table.addCell(cell);
        
        //Aggiungo l'intestazione del ristorante
        cell = new PdfPCell(new Phrase(intestazioneRistorante));
        cell.setColspan(13);
        cell.setRowspan(3);
        table.addCell(cell);
        
        //Aggiungo
        cell = new PdfPCell(new Phrase(  "[×] RICEVUTA FISCALE\n[×] FATTURA FISCALE" ));
        cell.setColspan(7);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(numeroIncrementaleRicevutaFattura));
        cell.setColspan(7);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(data));
        cell.setColspan(7);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        
      //Aggiungo intestazione cliente obbligatorio per fattura
        tmp_phrase = new Phrase();
        tmp_phrase.add(new Chunk("Ditta(cliente):"));
        tmp_phrase.add(new Chunk("\nPIVA:"));
        cell = new PdfPCell(tmp_phrase);/*calendar.getTime().toString()*/
        cell.setColspan(20);
        table.addCell(cell);
        
      //Aggiungo i nomi delle colonne della tabella delle comande
        f = new Font();
        f.setColor(BaseColor.WHITE);
        
		cell = new PdfPCell(new Phrase(  "Q.TA'" ,f));/*calendar.getTime().toString()*/
        cell.setBackgroundColor(BaseColor.BLACK);
        cell.setColspan(colSpanQuantita);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(  "DESCRIZIONE DEL BENE O SERVIZIO" ,f));/*calendar.getTime().toString()*/
        cell.setBackgroundColor(BaseColor.BLACK);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(colSpanDescrizione);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(  "IMPORTO" ,f));/*calendar.getTime().toString()*/
        cell.setBackgroundColor(BaseColor.BLACK);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setColspan(colSpanImporto);
        table.addCell(cell);
        
        
        
        for(int i=0; i<10; i++){
        	
        	cell = new PdfPCell(new Phrase(  "2"));
        	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        	cell.setColspan(colSpanQuantita);
        	table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(  "asdasdasd"));
            cell.setColspan(colSpanDescrizione);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(  "1500.00 €"));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setColspan(colSpanImporto);
            table.addCell(cell);
        	
        }
        
        cell = new PdfPCell(new Phrase(  "2"));
    	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    	cell.setColspan(colSpanQuantita);
    	table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(  "Pizza Capricciosa\n"+TAB+"-Variazione1  +0.50€\n"+TAB+"-Variazione2  -1.50€"));
        cell.setColspan(colSpanDescrizione);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(  "1500.00 €"));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setColspan(colSpanImporto);
        table.addCell(cell);
        
        
        //AGGIUNGO LA PARTE FINALE
        f = new Font();
        f.setSize(6);
        
        tmp_phrase = new Phrase();
        tmp_phrase.add(new Chunk("IMPONIBILE\n",f));
        tmp_phrase.add(new Chunk(imponibile));
        
        cell = new PdfPCell(	tmp_phrase		);/*calendar.getTime().toString()*/
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        tmp_phrase = new Phrase();
        tmp_phrase.add(new Chunk("%\n",f));
        tmp_phrase.add(new Chunk(percentualeIVA));
        cell = new PdfPCell(	tmp_phrase	);/*calendar.getTime().toString()*/
        cell.setColspan(1);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        tmp_phrase = new Phrase();
        tmp_phrase.add(new Chunk("IMPOSTA\n",f));
        tmp_phrase.add(new Chunk(imposta));
        cell = new PdfPCell(	tmp_phrase	);
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        
        cell = new PdfPCell(new Phrase(  "CORRISPETTIVO\nPAGATO €"	));
        cell.setColspan(6);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(corrispettivoPagato));
        cell.setColspan(5);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        //AGGIUNGO IL NUMERO FINANZA
        tmp_phrase = new Phrase();
        tmp_phrase.add(new Chunk("Tesina TecDB\nFabio Pierazzi, Andrea Castelli, Marco Guerri",f));
        tmp_phrase.add(new Chunk("\n\nXFR-A                     /2011"));
        cell = new PdfPCell(tmp_phrase);
        cell.setColspan(9);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(  "CORRISPETTIVO\nNON PAGATO €"	));/*calendar.getTime().toString()*/
        cell.setColspan(6);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(corrispettivoNonPagato));/*calendar.getTime().toString()*/
        cell.setColspan(5);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(  "TOTALE €"	));/*calendar.getTime().toString()*/
        cell.setColspan(6);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(totale));/*calendar.getTime().toString()*/
        cell.setColspan(5);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        
        
        return table;
    }

}
