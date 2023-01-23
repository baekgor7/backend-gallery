package com.shbt.backendgallery.repository;

import com.shbt.backendgallery.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    Member findByEmailAndPassword(String email, String password);
}
