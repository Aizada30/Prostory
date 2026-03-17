package com.prostory.service;

import com.prostory.dto.response.AuthenticationResponse;
import com.prostory.dto.request.SignInRequest;
import com.prostory.dto.request.SignUpRequest;
import com.prostory.dto.response.SimpleResponse;
import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;
import com.google.firebase.auth.FirebaseAuthException;

public interface AuthenticationService {
    AuthenticationResponse signUp(SignUpRequest request) throws BadRequestException;

    AuthenticationResponse signIn(SignInRequest request) throws BadRequestException;

    SimpleResponse forgotPassword(String email) throws MessagingException;

    SimpleResponse resetPassword(String token, String newPassword);
    AuthenticationResponse authWithGoogle(String tokenId) throws FirebaseAuthException;
}
