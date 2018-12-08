package org.fkjava.storage.domanin;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.fkjava.identity.domain.User;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="storage_file")
public class FileInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator="uuid2")
	@GenericGenerator(name="uuid2",strategy="uuid2")
	@Column(length=36)
	private String id;
	@Column(length = 20)
	private String name;
	
	private Long fileSize;
	private String contentType;
	//谁上传的
	@ManyToOne
	@JoinColumn(name="owner_user_id")
	private User owner;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date uploadTime;
	
	// 实际文件的存储路径，相对于文件存储目录的路径
	private String path;
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
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public Date getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
