package me.pick.metrodata.repositories;

import me.pick.metrodata.models.entity.Holiday;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long>, JpaSpecificationExecutor<Holiday> {

    Optional<Holiday> findByDate(LocalDate date);

    @Query("SELECT h FROM Holiday h WHERE "
            + "(:searchName IS NULL OR h.name LIKE %:searchName%) AND "
            + "(:date IS NULL OR h.date >= :date)")
    Page<Holiday> findAllWithFilters(
            @Param("searchName") String searchName,
            @Param("date") LocalDate date,
            Pageable pageable);
}
