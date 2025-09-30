package advanced.ipt_library.repository;

import advanced.ipt_library.entity.Archive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Integer> {
    Archive findByShelfIgnoreCase(String shelf);
}
