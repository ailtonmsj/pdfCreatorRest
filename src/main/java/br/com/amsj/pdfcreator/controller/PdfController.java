package br.com.amsj.pdfcreator.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.amsj.pdfcreator.service.PdfService;

@RestController
public class PdfController {
	
	@Autowired
	PdfService pdfService;
	
	@RequestMapping(value="/", method=RequestMethod.GET, produces="application/pdf")
	public ResponseEntity<byte[]> getPDF() {
	    
		String fileName = this.getFileName();
		
		byte[] bytes = pdfService.getFile(fileName);
	    
	    HttpHeaders headers = new HttpHeaders();
	    
	    // to avoid cache
	    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
	    headers.add("Pragma", "no-cache");
	    headers.add("Expires", "0");
	    
	    // to pass to the browser the filename
	    headers.add("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

		return ResponseEntity
		        .ok()
		        .headers(headers)
		        .contentLength(bytes.length)
		        .contentType(MediaType.APPLICATION_OCTET_STREAM)
		        .body(bytes);
	}
	
	private String getFileName() {
		
		DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder().appendPattern("ddMMYYYY").toFormatter();
	    String date = LocalDate.now().format(dateTimeFormatter);
	    String fileName = "fileNameMaroto" + date + ".pdf";
	    
	    return fileName;
		
	}
}
