package org.fkjava.storage.repository;

import org.fkjava.identity.domain.User;
import org.fkjava.storage.domanin.FileInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends JpaRepository<FileInfo, String>{

	Page<FileInfo> findByOwner(User user, Pageable pageable);

	Page<FileInfo> findByOwnerAndNameContaining(User user, String keyword, Pageable pageable);

}
