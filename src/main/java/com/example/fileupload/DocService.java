package com.example.fileupload;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsObject;

@Service
public class DocService {
	
	@Autowired
    private GridFsTemplate template;
	
    @Autowired
    private GridFsOperations operations;
    
    public String uploadFile(MultipartFile upload) throws IOException {

        DBObject metadata = new BasicDBObject();
        metadata.put("fileSize", upload.getSize());
        metadata.put("tef_id", "20230923123456789");

        Object fileID = template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(), metadata);

        return fileID.toString();
    }
    
    public List<File> listFiles() {
		List<GridFSFile> list = new ArrayList<GridFSFile>();;
		
		template.find(new Query().addCriteria(Criteria.where("metadata.tef_id").is("20230923123456789"))).into(list);
		List<File> listFiles = new ArrayList<File>();
		for (GridFSFile gridFSDBFile : list) {
			listFiles.add(convertToFile(gridFSDBFile));
		}
		return listFiles;
	}
    
    private File convertToFile(GridFSFile file){
    	return new File(file.getFilename());
    }
    
}
