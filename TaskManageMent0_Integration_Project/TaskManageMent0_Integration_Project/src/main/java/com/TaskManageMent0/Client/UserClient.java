package com.TaskManageMent0.Client;

import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.TaskManageMent0.Enum.Role;

@FeignClient(name = "user-service", url = "${user-service.url}")
public interface UserClient {

    @GetMapping("/api/users/{email}/roles")
    Set<Role> getRoles(@PathVariable String email);
}
