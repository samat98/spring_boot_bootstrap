package ru.javamentor.s_boot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.javamentor.s_boot.model.Role;
import ru.javamentor.s_boot.model.User;
import ru.javamentor.s_boot.services.RoleService;
import ru.javamentor.s_boot.services.UserService;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminsController {
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("")
    public String getUsers(Model model) {
        List<User> users = userService.listUsers();
        model.addAttribute("users", users);
        model.addAttribute("formUser", new User());
        List<Role> roles = roleService.getRolesList();
        model.addAttribute("allRoles", roles);
        return "users";
    }

    @GetMapping("/{id}/edit")
    public String getEditForm(@PathVariable Long id, Model model) {
        User userEdit = userService.findUser(id).orElseThrow(() -> new UsernameNotFoundException("user is not exists"));
        List<Role> roles = roleService.getRolesList();
        model.addAttribute("allRoles", roles);
        model.addAttribute("user", userEdit);
        return "update";
    }

    @PostMapping("/adduser")
    public String addUser(@Validated(User.class) @ModelAttribute User user,
                          @RequestParam("authorities") List<String> values,
                          BindingResult result) {
        if(result.hasErrors()) {
            return "error";
        }
        Set<Role> roleSet = userService.getSetOfRoles(values);
        user.setRoles(roleSet);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PutMapping("/update")
    public String updateUser(@Validated(User.class) @ModelAttribute User editedUser,
                             @RequestParam("authorities") List<String> values,
                             BindingResult result) {
        if(result.hasErrors()) {
            return "error";
        }
        Set<Role> roleSet = userService.getSetOfRoles(values);
        editedUser.setRoles(roleSet);
        userService.updateUser(editedUser);
        return "redirect:/admin";
    }

    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute(new User());
        List<Role> roles = roleService.getRolesList();
        model.addAttribute("allRoles", roles);
        return "create";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        User user = userService.findUser(id).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        userService.deleteUser(user.getId());
        return "redirect:/admin";
    }

}
