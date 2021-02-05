package ru.javamentor.s_boot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javamentor.s_boot.dao.UserRepo;
import ru.javamentor.s_boot.model.Role;
import ru.javamentor.s_boot.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private UserRepo userRepository;

    private RoleService roleService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setAll(RoleService roleService, UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userRepository = userRepo;
        this.passwordEncoder = passwordEncoder;
    }
    public User findByUsername(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByUsername(email);
        if(user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", email));
        }
        return user;
    }

    public void saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
    public Optional<User> findUser(Long id){
        return userRepository.findById(id);
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public Set<Role> getSetOfRoles(List<String> rolesId){
        Set<Role> roleSet = new HashSet<>();
        for (String id: rolesId) {
            roleSet.add(roleService.getRoleById(Long.parseLong(id)));
        }
        return roleSet;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void updateUser(User user) {
        User userToEdit = userRepository.findById(user.getId()).orElseThrow(() -> new UsernameNotFoundException("not found"));
        userToEdit.setEmail(user.getEmail());
        userToEdit.setPassword(user.getPassword());
        userToEdit.setLastname(user.getLastname());
        userToEdit.setFirstname(user.getFirstname());
        userToEdit.setAge(user.getAge());
        userToEdit.setRoles(user.getRoles());
        userRepository.save(user);
    }
}
