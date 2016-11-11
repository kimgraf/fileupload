package com.divergepoint.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

/**
 * Created by kim on 09/11/16.
 */
@Data
@Entity
public class UploadFile {
    private @Id @GeneratedValue Long id;
    private String originalName;
    private String title;
    @JsonFormat(pattern="dd.MMM.yyyy hh:mm:ss")
    private Date dateCreated;
    private String description;

    private UploadFile() {}

    public UploadFile(String originalName, String title, Date dateCreated, String description) {
        this.originalName = originalName;
        this.title = title;
        this.dateCreated = dateCreated;
        this.description = description;
    }
}
