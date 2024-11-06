package me.pick.metrodata.repositories;

import me.pick.metrodata.models.entity.Client;
import me.pick.metrodata.models.entity.InterviewSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByUserId(Long userId);

    Optional<Client> findClientById(Long id);

    @Query("SELECT is FROM InterviewSchedule is "
            + "JOIN is.client c "
            + "WHERE is.status = 'ACCEPTED' AND c.id = :clientId")
    List<InterviewSchedule> findEmployeeByInterviewAccepted(@Param("clientId") Long clientId);

    @Query("SELECT is.position, COUNT(is.position) FROM InterviewSchedule is "
            + "JOIN is.client c "
            + "WHERE is.status = 'ACCEPTED' AND c.id = :clientId "
            + "GROUP BY is.position")
    List<Object[]> findUniquePositionsAndCountByClientId(@Param("clientId") Long clientId);
}
