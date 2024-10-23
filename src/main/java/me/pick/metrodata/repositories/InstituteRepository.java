package me.pick.metrodata.repositories;

import me.pick.metrodata.enums.InstituteType;
import me.pick.metrodata.models.entity.Institute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstituteRepository extends JpaRepository<Institute, Long>, JpaSpecificationExecutor<Institute> {
        List<Institute> findInstitutesByInstituteType(InstituteType instituteType);
}