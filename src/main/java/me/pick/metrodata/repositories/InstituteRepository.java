package me.pick.metrodata.repositories;

import me.pick.metrodata.enums.InstituteType;
import me.pick.metrodata.models.entity.Institute;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstituteRepository extends JpaRepository<Institute, Long>, JpaSpecificationExecutor<Institute> {

        List<Institute> findInstitutesByInstituteType(InstituteType instituteType);

        @Query("SELECT i FROM Institute i WHERE "
                        + "(:searchName IS NULL OR i.instituteName LIKE %:searchName%) AND "
                        + "(:searchtype IS NULL OR i.instituteType LIKE %:searchtype%)")
        Page<Institute> findAllWithFilters(
                        @Param("searchName") String searchName,
                        @Param("searchtype") InstituteType searchtype,
                        Pageable pageable);
}
