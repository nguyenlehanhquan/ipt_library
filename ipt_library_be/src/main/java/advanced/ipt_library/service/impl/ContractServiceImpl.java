package advanced.ipt_library.service.impl;

import advanced.ipt_library.entity.Contract;
import advanced.ipt_library.exception.BusinessException;
import advanced.ipt_library.exception.ErrorCodeConstant;
import advanced.ipt_library.exception.NotFoundException;
import advanced.ipt_library.repository.ContractRepository;
import advanced.ipt_library.request.CreateContractRequest;
import advanced.ipt_library.request.UpdateContractRequest;
import advanced.ipt_library.response.ContractResponse;
import advanced.ipt_library.service.BookContractService;
import advanced.ipt_library.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final ModelMapper mapper;

    @Override
    public List<ContractResponse> findAll() {
        List<Contract> contracts = contractRepository.findAll();
        List<ContractResponse> contractResponses = new ArrayList<>();

        for (Contract contract : contracts) {
            ContractResponse contractResponse = mapper.map(contract, ContractResponse.class);
            contractResponses.add(contractResponse);
        }
        return contractResponses;
    }

    @Override
    public void create(CreateContractRequest request) {
        Contract contract = contractRepository.findByCodeIgnoreCase(request.getCode());
        if (contract != null) {
            throw new BusinessException("Contract already exists", ErrorCodeConstant.existing_data);
        }

        Contract newContract = new Contract();
        newContract.setCode(request.getCode());

        contractRepository.save(newContract);
    }

    @Override
    public void update(int id, UpdateContractRequest request) {
        Contract contract = contractRepository.findById(id).orElseThrow(() -> new NotFoundException("Contract does not exist", ErrorCodeConstant.no_existing_data));

        Contract contractFoundByCode = contractRepository.findByCodeIgnoreCase(request.getCode());
        if (contractFoundByCode != null && contractFoundByCode.getId() != id) {
            throw new BusinessException("Contract code already exists", ErrorCodeConstant.existing_data);
        }

        contract.setCode(request.getCode());
        contractRepository.save(contract);
    }

    @Override
    public void delete(int id) {
        Contract contract = contractRepository.findById(id).orElseThrow(() -> new NotFoundException("Contract does not exist", ErrorCodeConstant.no_existing_data));
        contractRepository.delete(contract);
    }


}
