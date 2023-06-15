package com.marlon.exam.repo;

import com.marlon.exam.model.Email;
import com.marlon.exam.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
	long countByEmail(String email);

	List<Email> findAllByEmail(String email);
}
