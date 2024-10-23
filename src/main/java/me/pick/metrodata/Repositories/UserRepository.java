package me.pick.metrodata.Repositories;

import lombok.NonNull;
import me.pick.metrodata.enums.InstituteType;
import me.pick.metrodata.models.entity.Institute;
import me.pick.metrodata.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  @NonNull
  List<User> findAll();

  List<User> findByInstitute(Institute institute);

  Optional<User> findByEmail(String email);

  Optional<User> findByInstituteId(Long id);

  List<User> findByInstitute_InstituteType(InstituteType instituteType);

}