package fa.hust.repositories;

import fa.hust.entities.Account;
import fa.hust.entities.Information;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InformationRepository extends JpaRepository<Information, Long> {


}
