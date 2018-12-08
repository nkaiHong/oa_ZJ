package org.fkjava.security.dao;

import org.fkjava.security.domain.FileInfo222;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDao extends JpaRepository<FileInfo222, String>{

	Page<FileInfo222> findByNameContaining(String keyword, Pageable pageable);


}
