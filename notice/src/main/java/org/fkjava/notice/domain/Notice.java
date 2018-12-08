package org.fkjava.notice.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.fkjava.identity.domain.User;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="notice")
public class Notice implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator="uuid2")
	@GenericGenerator(name="uuid2",strategy="uuid2")
	@Column(length=36)
	private String id;

	private String title;
	
	//类型
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="type_id")
	private NoticeType type;
	
	//作者
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="author_user_id")
	private User author;
	
	//撰写的时间，也是最后一次修改的时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date writeTime;
	
	//发布时间，如果没有发布，还是草稿的话就没有时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date releaseTime;
	
	//状态
	@Enumerated(EnumType.STRING)
	private Status status;
	
	//公告内容
	@Lob // 大的数据、长的字段，这种字段不适合LIKE查询，如果要模糊查询就需要结合后面的Lucene、Solr技术
	private String content;
	
	/**
	 * 为了记录用户的阅读状态，每个用户都会有阅读记录
	 *
	 */
	@OneToMany(mappedBy = "notice")
	private List<NoticeRead> reads;
	
	public static enum Status {
		//草稿
		DRAFT,
		//已发布
		RELEASED,
		//已经取消，撤回的。
		RECALL;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public NoticeType getType() {
		return type;
	}

	public void setType(NoticeType type) {
		this.type = type;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Date getWriteTime() {
		return writeTime;
	}

	public void setWriteTime(Date writeTime) {
		this.writeTime = writeTime;
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<NoticeRead> getReads() {
		return reads;
	}

	public void setReads(List<NoticeRead> reads) {
		this.reads = reads;
	}
	
	
}
