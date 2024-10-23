package me.pick.metrodata.Repositories;

import me.pick.metrodata.models.entity.Talent;
import me.pick.metrodata.models.entity.User;
import me.pick.metrodata.models.dto.responses.TalentDataDTO;
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

@SqlResultSetMapping(name = "TalentDataMapping", classes = @ConstructorResult(targetClass = TalentDataDTO.class, columns = {
		@ColumnResult(name = "id", type = Long.class), @ColumnResult(name = "name", type = String.class),
		@ColumnResult(name = "institute_name", type = String.class),
		@ColumnResult(name = "first_name", type = String.class), @ColumnResult(name = "apnid", type = Long.class),
		@ColumnResult(name = "jobid", type = Long.class) }))

@Repository
public interface TalentRepository extends JpaRepository<Talent, String>, JpaSpecificationExecutor<Talent> {

	@Query("SELECT t FROM Talent t WHERE t.mitra.id = :mitraId")
	List<Talent> findAllByMitra(@Param("mitraId") Long mitraId);

	@Query("SELECT t FROM Talent t " + "JOIN t.mitra m " + "LEFT JOIN t.skills s " + "LEFT JOIN t.jobHistories jh "
			+ "LEFT JOIN jh.position p " + "WHERE (:mitraId IS NULL OR m.id = :mitraId) "
			+ "AND (:name IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))) "
			+ "AND (:skill IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :skill, '%'))) "
			+ "AND (:job IS NULL OR (LOWER(p.reference_name) LIKE LOWER(CONCAT('%', :job, '%')) "
			+ "AND p.reference_group1 = 'posisi_pekerjaan'))")
	Page<Talent> findAllByFilters(@Param("mitraId") Long mitraId, @Param("name") String name,
			@Param("skill") String skill, @Param("job") String job, Pageable pageable);

	@Query("SELECT t FROM Talent t " + "JOIN t.institute i " + "LEFT JOIN t.skills s " + "LEFT JOIN t.jobHistories jh "
			+ "LEFT JOIN jh.position p " + "WHERE (:instituteId IS NULL OR i.id = :instituteId) "
			+ "AND (:name IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))) "
			+ "AND (:skill IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :skill, '%'))) "
			+ "AND (:job IS NULL OR (LOWER(p.reference_name) LIKE LOWER(CONCAT('%', :job, '%')) "
			+ "AND p.reference_group1 = 'posisi_pekerjaan'))")
	Page<Talent> findByInstituteAndFilter(@Param("instituteId") Long instituteId, @Param("name") String name,
			@Param("skill") String skill, @Param("job") String job, Pageable pageable);

	@Query("SELECT t FROM Talent t " + "LEFT JOIN t.skills s " + "LEFT JOIN t.jobHistories jh "
			+ "LEFT JOIN jh.position p " + "LEFT JOIN t.user u "
			+ "WHERE (:skill IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :skill, '%'))) "
			+ "AND (:job IS NULL OR (LOWER(p.reference_name) LIKE LOWER(CONCAT('%', :job, '%')) "
			+ "AND p.reference_group1 = 'posisi_pekerjaan')) " + "AND (:budget IS NULL OR u.limitBudget <= :budget)")
	Page<Talent> findTalentBySkillJobAndBudget(@Param("skill") String skill, @Param("job") String job,
			@Param("budget") Long budget, Pageable pageable);

	@Query("SELECT DISTINCT t FROM Talent t " + "LEFT JOIN t.skills s " + "LEFT JOIN t.jobHistories jh "
			+ "LEFT JOIN jh.position p "
			+ "WHERE(:skill IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :skill, '%'))) "
			+ "AND (:job IS NULL OR (LOWER(p.reference_name) LIKE LOWER(CONCAT('%', :job, '%')) AND p.reference_group1 = 'posisi_pekerjaan'))")
	Page<Talent> findTalentBySkillandJob(@Param("skill") String skill, @Param("job") String job, Pageable pageable);

	@Query("SELECT t FROM Talent t JOIN t.institute i JOIN i.user u WHERE u.id = :userId AND t.id NOT IN (SELECT an.talent.id FROM Applicant an WHERE an.id = :jobId)")
	Optional<List<Talent>> findTalentsByUserIdAndNotInApplicant(@Param("userId") Long userId, @Param("jobId") Long jobId);

	@Procedure(procedureName = "Get_Talent")
	List<Object[]> getTalent(@Param("userId") Integer userId, @Param("vacancyId") Integer jobId);

	@Procedure(procedureName = "Get_Talent_Data_Procedure", outputParameterName = "TalentDataMapping")
	List<Object[]> getTalentDataByVacancyIdAndNotRecommended(@Param("userId") Long userId, @Param("vacancyId") Long vacancyId,
			@Param("talentsId") String talentsId);

	@Query("SELECT t FROM Talent t WHERE (:nik IS NULL OR t.nik = :nik) AND (:userId IS NULL OR t.user.id = :userId)")
	List<Talent> findByNikOrUserId(@Param("nik") String nik, @Param("userId") Long userId);

	Optional<Talent> findByNik(String nik);

	Optional<Talent> findTalentByUser(User user);
}
