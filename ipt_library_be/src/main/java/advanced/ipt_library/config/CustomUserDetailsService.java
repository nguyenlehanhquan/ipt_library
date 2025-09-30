//package advanced.ipt_library.config;
//
//import advanced.ipt_library.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        if (username.equals("admin")) {
//            return User.withUsername("admin")
//                    .password("$2a$10$r4TFaxXlbk259CD/zN2s4eq2hTsWMcUuy2ZWcQnSVoB72snMy.Pu6")
//                    .roles("ADMIN")
//                    .build();
//        }
//
//        if (username.equals("user")) {
//            return User.withUsername("user")
//                    .password("$2a$10$r4TFaxXlbk259CD/zN2s4eq2hTsWMcUuy2ZWcQnSVoB72snMy.Pu6")
//                    .roles("USER")
//                    .build();
//        }
//
//        advanced.ipt_library.entity.User user = userRepository.findByUsernameIgnoreCase(username);
//
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found");
//        }
//
//        return User.withUsername(username)
//                .password(user.getPassword())
//                .roles(user.getRole())
//                .build();
//    }
//}
