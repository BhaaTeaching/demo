package com.helper.demo.service;

import com.amazonaws.services.appstream.model.User;

import java.util.List;

public interface DemoService {
    String getService();

    void completableFuture();

    User processUser(User user);
    List<User> bulkProcess(List<User> users);
}
