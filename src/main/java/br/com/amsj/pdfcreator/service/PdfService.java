package br.com.amsj.pdfcreator.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PdfService {

	public byte[] getFile(String fileName) {
		
		byte[] bytes = null;
		
		try {
			bytes = this.createFile(fileName);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    return bytes;
	}
	
	private byte[] createFile(String fileName) throws DocumentException, IOException {
		
		final String userPassword = "password";
		final String ownerPassword = "password";
		
		final String filePrefix = "createPdf";
	 
		byte[] bytes = null;
		
		File pdfFile = File.createTempFile(filePrefix, fileName);
		
		try (FileOutputStream fileOutputStream = new FileOutputStream(pdfFile)){
		 
			Document document = new Document();
			PdfWriter pdfWriter = PdfWriter.getInstance(document, fileOutputStream);
		 
			pdfWriter.setEncryption (userPassword.getBytes(), 
				    				ownerPassword.getBytes(),
				    				PdfWriter.ALLOW_PRINTING,
				    				PdfWriter.ENCRYPTION_AES_256);
		        document.open();
		        
		        document.add(new Paragraph("Maroto File"));
		        document.add(new Paragraph("Test 123"));
		        document.add(new Paragraph("Test 426"));
		        document.close();
		        fileOutputStream.close();
	
		        bytes = Files.readAllBytes (pdfFile.toPath());
		} finally {
			this.deleteFile(pdfFile.toPath());
		}	        
		return bytes;
	}
	
	private void deleteFile(Path path) {
		
		File file = path.toFile();
		
		if(file.exists() && file.isFile()) {
			System.out.println("Deleting File on Path: " + file.getPath());
			file.delete();
		}
	}
}
