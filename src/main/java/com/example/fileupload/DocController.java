package com.example.fileupload;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class DocController {
	
	 @Autowired
	 private DocService docService;
	 
	@RequestMapping(value="/", method=RequestMethod.GET)
	public @ResponseBody String hello() {
		return "Hello World";
	}
	    
	@PostMapping("/upload")
	public ResponseEntity<?> upload(@RequestParam("file")MultipartFile file) throws IOException {
	    return new ResponseEntity<>(docService.uploadFile(file), HttpStatus.OK);
	}
		
    @RequestMapping(value="/list", method=RequestMethod.GET)
    public @ResponseBody List<File> listFiles() {
    	return docService.listFiles();
	}
}
