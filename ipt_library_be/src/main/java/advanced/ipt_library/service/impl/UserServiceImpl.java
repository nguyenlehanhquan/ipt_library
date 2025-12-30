package advanced.ipt_library.service.impl;

import advanced.ipt_library.entity.History;
import advanced.ipt_library.entity.Role;
import advanced.ipt_library.entity.User;
import advanced.ipt_library.repository.HistoryRepository;
import advanced.ipt_library.repository.UserRepository;
import advanced.ipt_library.request.AuthRequest;
import advanced.ipt_library.request.CreateUserRequest;
import advanced.ipt_library.request.UpdateUserRequest;
import advanced.ipt_library.response.AuthResponse;
import advanced.ipt_library.response.UserResponse;
import advanced.ipt_library.security.JWTUtils;
import advanced.ipt_library.service.UserService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final Gson gson;
    private final HistoryRepository historyRepository;

    @Override
    public List<UserResponse> findAll() {

        // lấy ra toàn bộ user
        List<User> users = userRepository.findAll();

        // chuyển user --> List<UserReponse>
        List<UserResponse> UserResponses = new ArrayList<>();

        for (User user : users) {
            UserResponse response = new UserResponse();
            response.setId(user.getId());
            response.setFullName(user.getFullName());
            response.setUsername(user.getUsername());
            UserResponses.add(response);
        }

        return UserResponses;
    }



    @Override
    public void create(CreateUserRequest request) {

        User user = new User();
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(Role.USER);
        userRepository.save(user);
    }

    @Override
    public void update(UpdateUserRequest request) {

        User user = userRepository.findByUsernameIgnoreCase(request.getUsername());
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (request.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("Password cannot be the same as the old password");  // chỗ này nên làm hàm update password riêng hay gộp chung update user?
        }

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        userRepository.save(user);
    }

    @Override
    public void delete(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public AuthResponse login(AuthRequest request) {

        // check trong database xem có tài khoản này ở trong database xem có trùng khớp hay không?
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = userRepository.findByUsername(request.getUsername());
        System.out.println(gson.toJson(user));
        if (user == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        String token = jwtUtils.getJWTToken(user.getUsername());
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUsername(user.getUsername());
        this.addHistory(user.getClass().getSimpleName(), "login", gson.toJson(request), null, "Đăng nhập", "admin");
        return response;
    }

    private void addHistory(String object, String action, String dataBefore, String dataAfter, String description, String userLogin) {
        History newRecord = new History();
        newRecord.setObject(object);
        newRecord.setAction(action);
        newRecord.setDataBefore(dataBefore);
        newRecord.setDataAfter(dataAfter);
        newRecord.setDescription(description);
        newRecord.setUserLogin(userLogin);
        historyRepository.save(newRecord);
    }


}
