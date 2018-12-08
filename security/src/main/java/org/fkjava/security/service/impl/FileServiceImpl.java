package org.fkjava.security.service.impl;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.fkjava.security.dao.FileDao;
import org.fkjava.security.domain.FileInfo222;
import org.fkjava.security.service.FileService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class FileServiceImpl implements FileService,InitializingBean{

	@Autowired
	private FileDao fileDao;
	// 文件的实际内容保存的目录，文件名是随机的
	private File dir = new File("D:\\my java ee");
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(!dir.exists()) {
			dir.mkdirs();
		}
		System.out.println("实际文件的存储目录：" + dir.getAbsolutePath());
	}
	@Override
	@Transactional
	public void save(String name, String contentType, long fileSize, InputStream in) {
		String fileName = UUID.randomUUID().toString();
		
		//保存文件内容
		File file = new File(dir,fileName);
		Path target = file.toPath();
		
		try {
			Files.copy(in, target);
		} catch (Exception e) {
			// 保存文件出现问题，就不要保存文件信息到数据库
			throw new RuntimeException(e.getMessage(), e);
		}
		
		FileInfo222 info = new FileInfo222();
		info.setContentType(contentType);
		info.setFileName(fileName);
		info.setFileSize(fileSize);
		info.setName(name);
	

		// 保存文件信息
		
		fileDao.save(info);
		
	}
	@Override
	public Page<FileInfo222> findFiles(String keyword, Integer number) {
		if(StringUtils.isEmpty(keyword)) {
			keyword = null;
		}else {
			keyword = "%" + keyword + "%";
		} 
		
		Pageable pageable = PageRequest.of(number, 8);
		
		Page<FileInfo222> page;
		
		if(keyword == null) {
			//如果关键字为空，表示分页查询所有的数据
			page = this.fileDao.findAll(pageable);
		}else {
			//根据姓名查询，为前后模糊查询
			page = this.fileDao.findByNameContaining(keyword, pageable);
		}
		return page;
	}
	@Override
	public FileInfo222 findById(String id) {
		
		return this.fileDao.findById(id).orElse(null);
	}
	@Override
	public InputStream getFileContent(FileInfo222 fi) throws FileNotFoundException {
		
		File file = new File(dir,fi.getFileName());
		FileInputStream inputStream = new FileInputStream(file);
		return inputStream;
	}
	
}
