package com.library.app.repository;

import com.library.app.entity.Borrow;
import com.library.app.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    Optional<Borrow> findByTraceCode(String traceCode);

    List<Borrow> findByUserId(Long userId);

    List<Borrow> findByBookId(Long bookId);

    List<Borrow> findByStatus(Status status);
}