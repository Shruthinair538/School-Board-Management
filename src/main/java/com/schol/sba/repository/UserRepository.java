package com.schol.sba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.schol.sba.entity.User;
import com.schol.sba.enums.UserRole;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	public boolean existsByUserRole(UserRole role);

}
