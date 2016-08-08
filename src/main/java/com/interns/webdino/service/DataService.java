package com.interns.webdino.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.interns.webdino.data.MongoDataHelper;

@RestController
@RequestMapping("/data")
public class DataService {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(EchoService.class);

    @Autowired
    private MongoDataHelper mongoDataHelper;

    // make two methods, one for get and one for post
    @RequestMapping(method = { RequestMethod.POST })
    public ResponseEntity<String> addDoc(
            HttpServletRequest req,
            @RequestParam(name = "id", required = true) String id,
            @RequestParam(name = "content", required = true) String content) {

        ResponseEntity<String> result = null;

        try{

            //mongoDataHelper.addDocument(Integer.valueOf(id), content);

            result = ResponseEntity.ok("OK");

        } catch (Exception ex){

            result = ResponseEntity.badRequest().body("FAILED");
        }

        return result;
    }

    @RequestMapping(method={RequestMethod.GET})
    public ResponseEntity<List<Document>> getDoc(
            HttpServletRequest req,
            @RequestParam(name = "id", required = false) String id){

        ResponseEntity<List<Document>> result = null;

        List<Document> docs = null;

        try{

            if(null != id){
                docs = mongoDataHelper.find(Integer.parseInt(id));
            } else {
                docs = mongoDataHelper.findAll();
            }

            result = ResponseEntity.ok(docs);

        } catch(Exception ex){

            result = ResponseEntity.badRequest().body(null);

        }

        return result;

    }

}
