package com.capstone.jobtographer.repositories;


import com.capstone.jobtographer.models.UserCert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserCertsRepository extends JpaRepository<UserCert,Long> {

    @Query("SELECT uc FROM UserCert uc WHERE uc.user_id.id= :id")
    List<UserCert> findAllByUser_id(long id);


}
