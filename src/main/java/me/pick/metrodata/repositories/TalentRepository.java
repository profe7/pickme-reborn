package me.pick.metrodata.repositories;

import me.pick.metrodata.models.entity.Talent;
import me.pick.metrodata.models.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import java.util.List;
import java.util.Optional;

@Repository
public interface TalentRepository extends JpaRepository<Talent, String>, JpaSpecificationExecutor<Talent> {
	Optional<Talent> findByNik(String nik);

	Optional<Talent> findTalentByUser(User user);

	@Query("SELECT t FROM Talent t WHERE t.statusCV = 'COMPLETE' AND t.mitra.id = :mitraId")
	List<Talent> findTalentsWithCompleteCVByMitra(@Param("mitraId") Long mitraId);
}
