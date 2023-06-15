package com.marlon.exam.repo;

import com.marlon.exam.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	long countByEmail(String email);

	List<User> findAllByEmail(String email);

	Boolean existsByEmail(String email);

	Optional<User> findByUserCredentialsId(Long id);
}
