package pl.sportaihub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sportaihub.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    long countByActiveTrue();
}