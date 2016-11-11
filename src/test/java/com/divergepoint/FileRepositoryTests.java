package com.divergepoint;

import com.divergepoint.entity.FileUploadRepository;
import com.divergepoint.entity.UploadFile;
import com.divergepoint.storage.StorageService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by kim on 11/11/16.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class FileRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @MockBean
    private StorageService storageService;

    @Autowired
    private FileUploadRepository repository;

    @Test
    public void findAllUloadedFilesShouldReturnUser() {
        Date dateset = new Date();
        this.entityManager.persist(new UploadFile("test.txt", "title", dateset, "description"));
        Iterable <UploadFile> fileloaded = this.repository.findAll();
        fileloaded.forEach(uploadFile -> {
            assertThat (uploadFile.getOriginalName()).isEqualTo("test.txt");
            assertThat (uploadFile.getTitle()).isEqualTo("title");
            assertThat(uploadFile.getDescription()).isEqualTo("description");
            assertThat(uploadFile.getDateCreated()).isEqualTo(dateset);
        });

    }
}
