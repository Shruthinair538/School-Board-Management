package com.schol.sba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schol.sba.entity.ClassHour;

@Repository
public interface ClassHourRepository extends JpaRepository<ClassHour, Integer>{

}
