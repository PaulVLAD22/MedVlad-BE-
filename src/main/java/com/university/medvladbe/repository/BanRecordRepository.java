package com.university.medvladbe.repository;

import com.university.medvladbe.model.entity.account.User;
import com.university.medvladbe.model.entity.ban.BanRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BanRecordRepository extends JpaRepository<BanRecord,Long> {
    List<BanRecord> findByAdmin(User admin);
}
