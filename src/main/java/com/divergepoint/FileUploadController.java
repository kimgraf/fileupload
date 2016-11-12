package com.divergepoint;

import com.divergepoint.entity.FileUploadRepository;
import com.divergepoint.storage.StorageException;
import com.divergepoint.storage.StorageFileNotFoundException;
import com.divergepoint.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;
import com.divergepoint.entity.UploadFile;

@Controller
public class FileUploadController {

    private final StorageService storageService;
    private final FileUploadRepository repository;

    @Autowired
    public FileUploadController(StorageService storageService, FileUploadRepository repository) {
        this.storageService = storageService;
        this.repository = repository;
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
                .body(file);
    }

    @PostMapping("/files")
    @ResponseBody
    public UploadFile handleFileUpload(@RequestParam("file") MultipartFile file,
                                       @RequestParam("title") String title,
                                       @RequestParam("description") String description) {
        storageService.store(file);
        UploadFile f = new UploadFile(file.getOriginalFilename(), title, new Date(), description);
        repository.save(f);
        return f;
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity handleStorageException(StorageFileNotFoundException exc) {
        return new ResponseEntity(exc, HttpStatus.CONFLICT);
    }

}