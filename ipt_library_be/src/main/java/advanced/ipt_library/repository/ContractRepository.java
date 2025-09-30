package advanced.ipt_library.repository;

import advanced.ipt_library.entity.Contract;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {
    Contract findByCodeIgnoreCase(@NotBlank(message = "code is required") String code);
}
