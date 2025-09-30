package advanced.ipt_library.service;

import advanced.ipt_library.request.CreateContractRequest;
import advanced.ipt_library.request.UpdateContractRequest;
import advanced.ipt_library.response.ContractResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface ContractService {
    List<ContractResponse> findAll();
    void create(CreateContractRequest request);
    void update(int id, UpdateContractRequest request);
    void delete(int id);
}
