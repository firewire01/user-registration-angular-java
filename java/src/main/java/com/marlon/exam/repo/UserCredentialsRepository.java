package com.marlon.exam.repo;

import com.marlon.exam.model.UserCredentials;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {
	Optional<UserCredentials> findByUserName(String userName);

	List<UserCredentials> findAllByUserName(String userName);

	Boolean existsByUserName(String userName);
}
