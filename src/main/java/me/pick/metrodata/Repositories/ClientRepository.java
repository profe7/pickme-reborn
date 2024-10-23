package me.pick.metrodata.Repositories;

import me.pick.metrodata.models.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
	public Client findByUserId(Long userId);
}
