package com.prostory.service;

import com.prostory.dto.AuthenticationResponse;
import com.prostory.dto.SignInRequest;
import com.prostory.dto.SignUpRequest;
import com.prostory.dto.SimpleResponse;
import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;

public interface AuthenticationService {
    AuthenticationResponse signUp(SignUpRequest request) throws BadRequestException;

    AuthenticationResponse signIn(SignInRequest request) throws BadRequestException;

    SimpleResponse forgotPassword(String email) throws MessagingException;

    SimpleResponse resetPassword(String token, String newPassword);
}
