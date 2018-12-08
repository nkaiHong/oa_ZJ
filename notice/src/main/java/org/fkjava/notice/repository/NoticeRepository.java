package org.fkjava.notice.repository;


import org.fkjava.identity.domain.User;
import org.fkjava.notice.domain.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, String>{
//	case n.status 
//	when 'DRAFT' 
//		then 0
//	when 'RECALL'
//		then 99 
//	when 'RELEASED' 
//		then 1 
//end
	/*@Query("select n, "
			+ "case n.status when 'DRAFT' then 0 when 'RECALL' then 99 when 'RELEASED' then 1 end as status_int"
			+ "from Notice n"
			+ "where (n.author = :author and (n.status = 'DRAFT' or n.status = 'RECALL')"
			+ "or n.status = 'RELEASED')" + "order by status_int asc, releaseTime desc")*/
	@Query("select n, "//
			+ " case n.status when 'DRAFT' then 0 when 'RECALL' then 99 when 'RELEASED' then 1 end as status_int "//
			+ " from Notice n "//
			+ " where (n.author = :author and (n.status = 'DRAFT' or n.status = 'RECALL') "//
			+ " or n.status = 'RELEASED')" + " order by status_int asc, releaseTime desc")
	Page<Notice> findNotices(@Param("author")User author, Pageable pageable);

	
}
