package me.pick.metrodata.repositories;

import me.pick.metrodata.models.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long>, JpaSpecificationExecutor<Holiday> {

    Optional<Holiday> findByDate(LocalDate date);

}
