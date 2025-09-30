package advanced.ipt_library.repository;

import advanced.ipt_library.entity.BookContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookContractRepository extends JpaRepository<BookContract, Integer> {
}
