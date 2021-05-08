package am.example.iseo.service;

import am.example.iseo.domain.LoginRequest;
import am.example.iseo.domain.UserEntity;
import am.example.iseo.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(UserEntity userEntity) {
        String hashedPassword = BCrypt.hashpw(userEntity.getPassword(), BCrypt.gensalt());
        userEntity.setPassword(hashedPassword);
        userRepository.save(userEntity);
    }

    public String login(LoginRequest loginRequest) {
        UserEntity userEntity = getUserByUsername(loginRequest.getUsername());
        if (userEntity == null) {
            throw new IllegalArgumentException();
        }

        boolean isPassCorrect = BCrypt.checkpw(loginRequest.getPassword(), userEntity.getPassword());
        if (!isPassCorrect) {
            throw new IllegalArgumentException();
        }

        return generateJWT(userEntity);
    }

    private UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private String generateJWT(UserEntity userEntity) {
        Algorithm algorithm = Algorithm.HMAC256("auheiuhuiashdfiuhaiuygewyurgewqiu");
        return JWT.create()
                .withClaim("id", userEntity.getId())
                .withClaim("firstName", userEntity.getFirstName())
                .withClaim("lastName", userEntity.getLastName())
                .withClaim("username", userEntity.getUsername())
                .sign(algorithm);
    }

}
