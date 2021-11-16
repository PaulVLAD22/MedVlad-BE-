package com.university.medvladbe.repository;

import com.university.medvladbe.model.entity.ban.BanRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BanRecordRepository extends JpaRepository<BanRecord,Long> {
}
