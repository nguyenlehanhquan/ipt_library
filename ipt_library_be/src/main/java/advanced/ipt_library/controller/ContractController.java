package advanced.ipt_library.controller;

import advanced.ipt_library.request.CreateContractRequest;
import advanced.ipt_library.request.UpdateContractRequest;
import advanced.ipt_library.response.ApiResponse;
import advanced.ipt_library.service.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @GetMapping
    public ResponseEntity<ApiResponse> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Get all contracts", contractService.findAll()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CreateContractRequest request) {
        contractService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Contract created", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable int id, @Valid @RequestBody UpdateContractRequest request) {
        contractService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Contract updated", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable int id) {
        contractService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Contract deleted", null));
    }



}
