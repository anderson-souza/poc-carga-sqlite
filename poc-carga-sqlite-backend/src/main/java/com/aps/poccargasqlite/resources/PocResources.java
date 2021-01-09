package com.aps.poccargasqlite.resources;


import com.aps.poccargasqlite.services.PocService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("poc")
public class PocResources {

    @Autowired
    PocService pocService;

    @GetMapping("/banco")
    public ResponseEntity<Resource> baixarBancoSqlite() {

        File file = pocService.gerarBancoDadosSpringTemplate();

        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=banco.db");

            return ResponseEntity.ok().headers(header).contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
