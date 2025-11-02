package org.example.grc.Controllers;

import org.example.grc.Entities.Role;
import org.example.grc.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class RoleController {


    @Autowired
    RoleRepository roleRepository;


    @PostMapping("/addRole")
    public Role AddRole ( @RequestBody Role role)
    {
        return roleRepository.save(role);
    }

}
