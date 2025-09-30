package advanced.ipt_library.controller;

import advanced.ipt_library.request.CreateUserRequest;
import advanced.ipt_library.request.UpdateUserRequest;
import advanced.ipt_library.response.ApiResponse;
import advanced.ipt_library.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Get all users", userService.findAll()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody CreateUserRequest request) {
        userService.create(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("User created", null));
    }

    @PutMapping
    public ResponseEntity<ApiResponse> update(@Valid @RequestBody UpdateUserRequest request) {
        userService.update(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("User updated", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable int id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("User deleted", null));
    }
}
