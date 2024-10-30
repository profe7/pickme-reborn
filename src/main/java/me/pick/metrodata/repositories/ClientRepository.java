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

	@Query("SELECT i FROM InterviewSchedule i " +
       "JOIN i.client c " +
       "WHERE i.status = 'ACCEPTED' AND c.id = :clientId")
	List<InterviewSchedule> findEmployeeByInterviewAccepted(@Param("clientId") Long clientId);
}
