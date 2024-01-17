package com.example.metroinder.user.repository;

import com.example.metroinder.user.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    UserAccount findByProviderAndEmail(String provider, String email);

}
