package se.service;

import se.domain.File;

import java.util.List;

public interface FileService {

    void save(File file);

    void delete(Integer id);

    List<File> getAll();

    File getById(Integer id);

    void update(Integer id,Integer user_id, String title,String date);

    List<File> select(Integer id, Integer user_id, String title, String date);

    List<File> getAllById(Integer id);
    List<File> sort( Integer user_id, String field);
}