package com.divergepoint.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by kim on 09/11/16.
 */
@Component
public class DatabaseLoader implements CommandLineRunner {

    private final FileUploadRepository repository;

    @Autowired
    public DatabaseLoader(FileUploadRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... strings) throws Exception {
//        this.repository.save(new UploadFile("Frodo", new Date(), "ring bearer"));
    }
}
