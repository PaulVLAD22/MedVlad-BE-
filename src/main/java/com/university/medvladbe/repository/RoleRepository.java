package com.university.medvladbe.repository;

import com.university.medvladbe.entity.account.DefinedRole;
import com.university.medvladbe.entity.account.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findRoleByName(DefinedRole role);
}
