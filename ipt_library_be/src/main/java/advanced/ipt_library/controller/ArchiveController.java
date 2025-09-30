package advanced.ipt_library.controller;

import advanced.ipt_library.request.CreateArchiveRequest;
import advanced.ipt_library.request.UpdateArchiveRequest;
import advanced.ipt_library.response.ApiResponse;
import advanced.ipt_library.service.ArchiveService;
import advanced.ipt_library.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("archives")
@RequiredArgsConstructor
public class ArchiveController {
    private final ArchiveService archiveService;

    @GetMapping
    public ResponseEntity<ApiResponse> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Get all archive", archiveService.findAll()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody CreateArchiveRequest request) {
        archiveService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Archive created", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable int id, @Valid @RequestBody UpdateArchiveRequest request) {
        archiveService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Archive updated", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable int id) {
        archiveService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Archive deleted", null));
    }
}
