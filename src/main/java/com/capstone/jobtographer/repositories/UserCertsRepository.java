package com.capstone.jobtographer.repositories;


import com.capstone.jobtographer.models.UserCert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCertsRepository extends JpaRepository<UserCert,Long> {
//    List<UserCert> findAllByUser_id(long user_id);


}
