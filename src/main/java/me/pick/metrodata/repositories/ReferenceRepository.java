package me.pick.metrodata.repositories;

import me.pick.metrodata.models.entity.References;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferenceRepository extends JpaRepository<References, Long> {

        @Query("SELECT r FROM References r WHERE r.reference_group1 = ?1")
        List<References> findReferencesByGroup1(String referenceGroup1);

        @Query("SELECT r FROM References r WHERE r.reference_group2 = ?1")
        List<References> findReferencesByGroup2(Integer referenceGroup2);

        @Query("SELECT r FROM References r WHERE "
                        + "(:searchParameterName IS NULL OR r.reference_group1 LIKE %:searchParameterName%) AND "
                        + "(:searchParameterValue IS NULL OR r.reference_name LIKE %:searchParameterValue%)")
        Page<References> findAllWithFilters(
                        @Param("searchParameterName") String searchParameterName,
                        @Param("searchParameterValue") String searchParameterValue,
                        Pageable pageable);
}
