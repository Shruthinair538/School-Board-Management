package com.schol.sba.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.schol.sba.entity.School;
import com.schol.sba.entity.User;
import com.schol.sba.enums.UserRole;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	public boolean existsByUserRole(UserRole role);

	Optional<User> findByUserName(String userName);
	
	User findByUserRole(UserRole role);
	

}
