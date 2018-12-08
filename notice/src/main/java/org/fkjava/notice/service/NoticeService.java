package org.fkjava.notice.service;

import java.util.List;

import org.fkjava.notice.domain.Notice;
import org.fkjava.notice.domain.NoticeRead;
import org.fkjava.notice.domain.NoticeType;
import org.springframework.data.domain.Page;

public interface NoticeService {

	List<NoticeType> findAllTypes();

	void save(NoticeType type);

	void deleteTypeById(String id);

	Page<NoticeRead> findNotices(Integer number, String keyword);

	void write(Notice notice);

	Notice findById(String id);

	void read(String id);

	void recall(String id);

	void deletebyId(String id);

	void publish(String id);


}
