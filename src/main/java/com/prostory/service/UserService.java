package com.prostory.service;

import com.prostory.dto.response.UserChosenOneResponse;

import java.util.List;

public interface UserService {
    List<UserChosenOneResponse> getAll();
}
