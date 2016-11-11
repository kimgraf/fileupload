package com.divergepoint.entity;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by kim on 09/11/16.
 */
public interface FileUploadRepository extends CrudRepository <UploadFile, Long> {
}
