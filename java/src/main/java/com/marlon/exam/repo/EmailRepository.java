package com.marlon.exam.repo;

import com.marlon.exam.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
	long countByEmail(String email);

	List<Email> findAllByEmail(String email);
}
