package com.university.medvladbe.repository;

import com.university.medvladbe.entity.ban.BanRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BanRecordRepository extends JpaRepository<BanRecord,Long> {
}
