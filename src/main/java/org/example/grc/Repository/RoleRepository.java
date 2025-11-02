package org.example.grc.Repository;

import org.example.grc.Entities.Role;
import org.example.grc.Entities.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(RoleName roleName);

}
