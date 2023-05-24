package com.example.demo.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entities.Operation;

public interface OperationRepository extends JpaRepository<Operation, Long> {
	@Query("select o from Operation o where o.compte.codeCompte=:x order by o.dateOperation desc ")
	public Page<Operation> listOperatiion(@Param("x") Long codeCpte, Pageable pageable);

	@Query("SELECT o FROM Operation o WHERE o.compte.codeCompte = :x AND YEAR(o.dateOperation) = :year AND MONTH(o.dateOperation) = :month ORDER BY o.dateOperation DESC")
	public List<Operation> listOperationbydate(@Param("x") Long codeCpte, @Param("year") int year,
			@Param("month") int month);
}
