package br.com.indiegamesapi.repository;

import br.com.indiegamesapi.model.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    @Query("SELECT p FROM UserProfile p WHERE LOWER(p.bio) LIKE LOWER(CONCAT('%', :text, '%'))")
    Page<UserProfile> searchByBio(@Param("text") String text, Pageable pageable);
}
