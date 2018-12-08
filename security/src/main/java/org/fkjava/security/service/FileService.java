package org.fkjava.security.service;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.fkjava.security.domain.FileInfo222;
import org.springframework.data.domain.Page;

public interface FileService {

	void save(String name, String contentType, long fileSize, InputStream in);

	Page<FileInfo222> findFiles(String keyword, Integer number);

	FileInfo222 findById(String id);

	InputStream getFileContent(FileInfo222 fi)  throws FileNotFoundException;

}
