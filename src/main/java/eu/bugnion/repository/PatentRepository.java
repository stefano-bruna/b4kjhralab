package eu.bugnion.repository;
import eu.bugnion.domain.Patent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Patent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatentRepository extends JpaRepository<Patent, Long> {

}
