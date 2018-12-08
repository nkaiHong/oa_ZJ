package org.fkjava.storage.serviece.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.UUID;

import org.fkjava.common.data.domain.Result;
import org.fkjava.identity.UserHolder;
import org.fkjava.identity.domain.User;
import org.fkjava.storage.domanin.FileInfo;
import org.fkjava.storage.repository.StorageRepository;
import org.fkjava.storage.serviece.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
@ConfigurationProperties(prefix = "fkjava.storage")
public class StoragServiceImpl implements StorageService{

	@Autowired
	private StorageRepository storageRepository;
	//配置输出日志
	private static final Logger LOG = LoggerFactory.getLogger(StoragServiceImpl.class);
	
	private String  dir = "D:\\my java ee";
	
	
	@Override
	public void save(FileInfo info, InputStream in) {
		//保存文件
		String path = UUID.randomUUID().toString();
		File file = new File(dir,path);
		
		if(!file.getParentFile().exists()) {
			file.mkdirs();
		}
		
		LOG.trace("文件实际的存储路径：{}", file.getAbsolutePath());
		
		Path target = file.toPath();
		
		
		try {
			Files.copy(in, target);
		} catch (IOException e) {
			
			throw new RuntimeException("保存文件到硬盘失败" + e.getLocalizedMessage(),e);
		}
		
		//保存文件信息
		info.setOwner(UserHolder.get());
		info.setUploadTime(new Date());
		info.setPath(path);
		
		FileInfo fi = this.storageRepository.save(info);
		// 为了把id返回给控制器使用
		info.setId(fi.getId());
	
	}
	
	
	@Override
	public Page<FileInfo> findFiles(String keyword, Integer number) {
		//只查询当前用户 之前配置的一个工具类，里面就存放有user
		User user = UserHolder.get();
		
		if(StringUtils.isEmpty(keyword)) {
			keyword = null;
		}
		
		Pageable pageable = PageRequest.of(number, 8);
		
		Page<FileInfo> page;
		if(keyword == null) {
			page = this.storageRepository.findByOwner(user,pageable);
		}else {
			page = this.storageRepository.findByOwnerAndNameContaining(user,keyword,pageable);
		}
		return page;
	}


	@Override
	public FileInfo findById(String id) {
		
		return this.storageRepository.findById(id).orElse(null);
	}


	@Override
	public InputStream findFileContent(FileInfo fi) {
		
		File file = new File(dir,fi.getPath());
		try {
			FileInputStream in = new FileInputStream(file);
			return in;
		} catch (FileNotFoundException e) {
			LOG.trace("文件没有找到："+ e.getLocalizedMessage(),e);
		}
		return null;
	}


	@Override
	public Result deleteFile(String id) {
		System.out.println("---------------deleteFile");
		//根据id获取文件信息
		FileInfo info = this.storageRepository.findById(id).orElse(null);
		if(info != null) {
			//删除文件
			File file = new File(dir,info.getPath());
			file.delete();
			//删除数据库里面的文件信息
			this.storageRepository.delete(info);
		}
		return null;
	}

}
