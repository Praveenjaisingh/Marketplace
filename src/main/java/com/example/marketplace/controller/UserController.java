package com.example.marketplace.controller;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.example.marketplace.util.ResponseUtil;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.entity.User;
import com.example.marketplace.service.UserService;

@Component
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public ServerResponse createUser(ServerRequest request) throws Exception {
        try {
            User user = request.body(User.class);
            User savedUser = userService.createUser(user);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "User created successfully",
                "data", savedUser
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getAllUsers(ServerRequest request) throws Exception {
        try {
            List<User> allUsers = userService.getAllUsers();
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Users fetched successfully",
                "data", allUsers
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getUserById(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            User user = userService.getUserById(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "User fetched successfully",
                "data", user
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse updateUser(ServerRequest request) {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            User user = new User();
            user.setUsername((String) body.get("username"));
            user.setEmail((String) body.get("email"));
            user.setPassword((String) body.get("password"));
            user.setRole((String) body.get("role"));
            user.setPhone((String) body.get("phone"));
            user.setActive((Boolean) body.get("active"));
            User updatedUser = userService.updateUser(id, user);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "User updated successfully",
                "data", updatedUser
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse deleteUser(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            userService.deleteUser(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "User deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }
}
