package com.okchem.osa.service;

import com.okchem.osa.model.User;

public interface UserService {
    void save(User user);
    void createBuyer(User user);
    User findByUsername(String username);
}
