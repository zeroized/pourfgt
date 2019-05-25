package cn.edu.shu.pourfgt.controller;

import cn.edu.shu.pourfgt.dataSource.dao.UserRepository;
import cn.edu.shu.pourfgt.dataSource.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class LoginController {
    private final UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/signIn")
    public String signIn(String userId, String password, HttpServletRequest request) {
        System.out.println(userId + "/" + password);
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println(user.toString());
            if (password.equals(user.getPassword())) {
                int role = user.getRole();
                HttpSession session = request.getSession();
                session.setAttribute("userId", userId);
                if (role == 0) {
                    session.setAttribute("role", "teacher");
                    return "redirect:/teacher";
                } else {
                    session.setAttribute("role", "student");
                    return "redirect:/student";
                }
            }
        }
        return "redirect:/login";
    }

}
