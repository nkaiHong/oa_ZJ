package org.fkjava.storage.serviece;

import java.io.InputStream;

import org.fkjava.common.data.domain.Result;
import org.fkjava.storage.domanin.FileInfo;
import org.springframework.data.domain.Page;

public interface StorageService {

	Page<FileInfo> findFiles(String keyword, Integer number);

	void save(FileInfo info, InputStream in);

	FileInfo findById(String id);

	InputStream findFileContent(FileInfo fi);

	Result deleteFile(String id);

}
