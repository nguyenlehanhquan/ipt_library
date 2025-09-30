package advanced.ipt_library.service;

import advanced.ipt_library.request.AuthRequest;
import advanced.ipt_library.request.CreateUserRequest;
import advanced.ipt_library.request.UpdateUserRequest;
import advanced.ipt_library.response.AuthResponse;
import advanced.ipt_library.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> findAll();
    void create(CreateUserRequest request);
    void update(UpdateUserRequest request);
    void delete(int id);
    AuthResponse login(AuthRequest request);
}
