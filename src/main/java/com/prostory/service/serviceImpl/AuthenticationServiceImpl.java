package com.prostory.service.serviceImpl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.prostory.configs.jwt.JwtService;
import com.prostory.dto.response.AuthenticationResponse;
import com.prostory.dto.request.SignInRequest;
import com.prostory.dto.request.SignUpRequest;
import com.prostory.dto.response.SimpleResponse;
import com.prostory.entity.User;
import com.prostory.entity.UserInfo;
import com.prostory.enums.Role;
import com.prostory.exception.exceptions.AlreadyExistException;
import com.prostory.exception.exceptions.BadCredentialException;
import com.prostory.repository.UserInfoRepository;
import com.prostory.repository.UserRepository;
import com.prostory.service.AuthenticationService;
import com.prostory.service.EmailService;
import jakarta.transaction.Transactional;
import com.prostory.exception.exceptions.NotFoundException;
import com.prostory.exception.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.core.io.ClassPathResource;
import jakarta.annotation.PostConstruct;
import java.io.IOException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final TemplateEngine templateEngine;

    @Override
    public AuthenticationResponse signUp(SignUpRequest request) throws BadRequestException {
        if (userInfoRepository.existsByEmail(request.email())) {
            log.error(String.format("User with email %s is already exists", request.email()));
            throw new AlreadyExistException(String.format("User with email %s is already exists", request.email()));
        }
        String split = request.email().split("@")[0];
        if (split.equals(request.password())) {
            throw new BadRequestException("Create a stronger password");
        }
        UserInfo userInfo = UserInfo.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        User user = User.builder()
                .userName(request.userName())
                .phoneNumber(request.phoneNumber())
                .userInfo(userInfo)
                .build();
        userRepository.save(user);
        log.info(String.format("User %s successfully saved!", userInfo.getEmail()));
        String token = jwtService.generateToken(userInfo);

        return AuthenticationResponse.builder()
                .email(userInfo.getEmail())
                .role(userInfo.getRole())
                .token(token)
                .build();
    }

    @Override
    public AuthenticationResponse signIn(SignInRequest request) throws BadRequestException {

        UserInfo userInfo = userInfoRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    log.error(String.format("User with email %s does not exists", request.email()));
                    throw new BadCredentialException(String.format("User with email %s does not exists", request.email()));
                });
        if (!passwordEncoder.matches(request.password(), userInfo.getPassword())) {
            log.error("Password does not match");
            throw new BadRequestException("Password does not match");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        log.info(String.format("User %s authenticated successfully", userInfo.getEmail()));
        String token = jwtService.generateToken(userInfo);

        return AuthenticationResponse.builder()
                .email(userInfo.getEmail())
                .role(userInfo.getRole())
                .token(token)
                .build();
    }

    @Override
    public SimpleResponse forgotPassword(String email) {
        UserInfo userInfo = userRepository.findUserInfoByEmail(email)
                .orElseThrow(() -> {
                    log.error(String.format("User with email %s does not exists", email));
                    try {
                        throw new NotFoundException(String.format("User with email %s does not exists", email));
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
        String token = UUID.randomUUID().toString();
        userInfo.setResetPasswordToken(token);
        userInfoRepository.save(userInfo);

        String subject = "Password Reset Request";
        String resetPasswordLink = "http://localhost:8080/swagger-ui/index.html#/Authentication%20API/resetPassword";

        Context context = new Context();
        context.setVariable("title", "Password Reset");
        context.setVariable("message", "Please click link below for password reset!");
        context.setVariable("token", resetPasswordLink);
        context.setVariable("tokenTitle", "Reset Password");

        String htmlContent = templateEngine.process("reset-password-template.html", context);

        emailService.sendEmail(email, subject, htmlContent);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("The password reset was sent to your email. Please check your email.")
                .build();
    }

    @Override
    public SimpleResponse resetPassword(String token, String newPassword) {
        UserInfo userInfo = userInfoRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new NotFoundException("..."));

        userInfo.setPassword(passwordEncoder.encode(newPassword));
        userInfo.setResetPasswordToken(null);
        userInfoRepository.save(userInfo);
        log.info("User password changed successfully!");
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("User password changed successfully!")
                .build();
    }

    @PostConstruct
    void init() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource("prostory-4902f-firebase-adminsdk-fbsvc-82f9a15fb6.json")
                        .getInputStream());
        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build();
        FirebaseApp.initializeApp(firebaseOptions);
    }

    @Override
    public AuthenticationResponse authWithGoogle(String tokenId) throws FirebaseAuthException {
        log.info("auth with google has been started");
        FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(tokenId);

        if (!userInfoRepository.existsByEmail(firebaseToken.getEmail())) {
            String[] names = firebaseToken.getName().split(" ", 2);

            UserInfo userInfo = UserInfo.builder()
                    .email(firebaseToken.getEmail())
                    .password(passwordEncoder.encode(firebaseToken.getEmail()))
                    .role(Role.USER)
                    .build();

            User newUser = User.builder()
                    .userName(names[0])
                    .userInfo(userInfo)
                    .build();

            userRepository.save(newUser);
            log.info("New user via Google saved: {}", firebaseToken.getEmail());
        }

        UserInfo userInfo = userInfoRepository.findByEmail(firebaseToken.getEmail())
                .orElseThrow(() -> new NotFoundException(
                        String.format("Not found user with email %s", firebaseToken.getEmail())
                ));

        String token = jwtService.generateToken(userInfo);
        log.info("auth with google has been ended");

        return AuthenticationResponse.builder()
                .token(token)
                .email(userInfo.getEmail())
                .role(userInfo.getRole())
                .build();
    }

    @PostConstruct
    public void initSaveAdmin() {
        if (!userInfoRepository.existsByEmail("admin@gmail.com")) {
            UserInfo adminInfo = UserInfo.builder()
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("Admin123"))
                    .role(Role.ADMIN)
                    .build();

            User admin = User.builder()
                    .userName("Admin")
                    .phoneNumber("+996558870024")
                    .userInfo(adminInfo)
                    .build();

            userRepository.save(admin);
            log.info("Admin successfully created and saved");
        }
    }
}
