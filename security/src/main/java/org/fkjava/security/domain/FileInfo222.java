package org.fkjava.security.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "file_info")
public class FileInfo222 {
	@Id
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@GeneratedValue(generator = "uuid2")
	private String id;
	// 原始文件名，上传的时候指定的文件名
	// 下载的时候，使用name指定实际文件名，查看的时候也才能直观看到文件名！
	private String name;
	private String contentType;
	private long fileSize;
	// 文件内容，一般不会存储在数据库里面，因为文件内容太大了！
	// 所以数据库里面一般是保存文件的文件名（通常是随机的、或者是文件指纹）
	// 现在没有处理文件指纹，所以直接使用随机文件名。
	private String fileName;

	@Lob
	private byte[] file;
	

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
