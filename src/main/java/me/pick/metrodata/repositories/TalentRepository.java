package me.pick.metrodata.repositories;

import me.pick.metrodata.enums.StatusCV;
import me.pick.metrodata.models.entity.Talent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TalentRepository extends JpaRepository<Talent, String>, JpaSpecificationExecutor<Talent> {

        Optional<Talent> findByNik(String nik);

        List<Talent> findTalentByMitraId(Long mitraId);

        @Query("SELECT t FROM Talent t WHERE t.statusCV = 'COMPLETE' AND t.mitra.id = :mitraId")
        List<Talent> findTalentsWithCompleteCVByMitra(@Param("mitraId") Long mitraId);

        @Query("SELECT v FROM Talent v WHERE "
                        + "(:searchName IS NULL OR v.name LIKE %:searchName%) AND "
                        + "(:searchMitra IS NULL OR v.institute.instituteName LIKE %:searchMitra%) AND "
                        + "(:status IS NULL OR v.statusCV = :status)")
        Page<Talent> findAllWithFilters(
                        @Param("searchName") String searchName,
                        @Param("searchMitra") String searchMitra,
                        @Param("status") StatusCV status,
                        Pageable pageable);

        @Query("SELECT COUNT(t) > 0 FROM Talent t WHERE t.talentNik = :nik AND t.id != :id")
        boolean existsByNikAndNotId(@Param("nik") String nik, @Param("id") String id);
}
