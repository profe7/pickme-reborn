package me.pick.metrodata.repositories;

import me.pick.metrodata.models.entity.References;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReferenceRepository extends JpaRepository<References, Long> {

    @Query("SELECT r FROM References r WHERE r.reference_group1 = ?1")
    List<References> findReferencesByGroup1(String referenceGroup1);

    @Query("SELECT r FROM References r WHERE r.reference_group2 = ?1")
    List<References> findReferencesByGroup2(Integer referenceGroup2);

    @Override
    Optional<References> findById(Long Long);
}
