package org.fkjava.notice.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name="notice_read")
public class NoticeRead implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="uuid2")
	@GenericGenerator(name="uuid2",strategy="uuid2")
	@Column(length=36)
	private String id;
	
	/**
	 * 谁读取的
	 */
	@ManyToOne()
	@JoinColumn(name="user_id")
	private User user;
	
	/**
	 * 阅读时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date readTime;
	
	/**
	 * 阅读哪个公告
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="notice_id")
	private Notice notice;
	
	public NoticeRead() {}
	// 如果没有id、没有user、没有readTime，表示这个公告用户还未阅读

	public NoticeRead(String id, Date readTime, Notice notice) {
		super();
		this.id = id;
		this.readTime = readTime;
		this.notice = notice;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

	public Notice getNotice() {
		return notice;
	}

	public void setNotice(Notice notice) {
		this.notice = notice;
	}
	
	
	
}
