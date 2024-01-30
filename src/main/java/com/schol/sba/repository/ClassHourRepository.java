package com.schol.sba.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schol.sba.entity.ClassHour;

@Repository
public interface ClassHourRepository extends JpaRepository<ClassHour, Integer>{

	boolean existsByRoomNoAndBeginsAtAndEndsAt(int roomNo, LocalDateTime beginsAt, LocalDateTime endsAt);

}
