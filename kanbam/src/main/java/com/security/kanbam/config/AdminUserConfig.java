package com.security.kanbam.config;

import com.security.kanbam.model.Role;
import com.security.kanbam.model.User;
import com.security.kanbam.repository.RoleRepository;
import com.security.kanbam.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserConfig(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Role roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());
        var userAdmin = userRepository.findByUsername("admin"); //aqui

        // Se o role "ADMIN" não for encontrado, cria-o
        if (roleAdmin == null) {
            roleAdmin = new Role();
            roleAdmin.setName(Role.Values.ADMIN.name());
            roleRepository.save(roleAdmin);
            System.out.println("Role ADMIN criado!");
        }

        // Verifica e cria a role BASIC se não existir
        Role roleBasic = roleRepository.findByName(Role.Values.BASIC.name());
        if (roleBasic == null) {
            roleBasic = new Role();
            roleBasic.setName(Role.Values.BASIC.name());
            roleRepository.save(roleBasic);
            System.out.println("Role BASIC criada!");
        }

        var finalRoleAdmin = roleAdmin;
        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("admin ja existe");
                },
                ()-> {
                    var user = new User();
                    user.setUsername("admin");
                    user.setPassword(passwordEncoder.encode("123"));
                    user.setRoles(Set.of(finalRoleAdmin));
                    userRepository.save(user);
                    System.out.println("Usuário admin criado!");
                }
        );
    }
}
