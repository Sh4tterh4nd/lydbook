package ch.fhnw.webec.controllers;

import ch.fhnw.webec.dao.DAOPassword;
import ch.fhnw.webec.entity.Progress;
import ch.fhnw.webec.services.ProgressService;
import ch.fhnw.webec.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;

@Controller
public class MainController {

    private final ProgressService progressService;
    private final UserService userService;

    public MainController(ProgressService progressService, UserService userService) {
        this.progressService = progressService;
        this.userService = userService;
    }

    @GetMapping({"/", "/index"})
    public String index(Model model, @AuthenticationPrincipal Principal principal) {
        List<Progress> mostRecentProgress = progressService.getMostRecentProgressByUser(principal.getName());
        if (mostRecentProgress.size() > 0) {
            model.addAttribute("last", mostRecentProgress.get(0));
        } else {
            model.addAttribute("last", null);
        }
        if (mostRecentProgress.size() > 1) {
            model.addAttribute("progresses", mostRecentProgress.subList(1, mostRecentProgress.size() > 10 ? 10 : mostRecentProgress.size()));
        } else {
            model.addAttribute("progresses", null);
        }
        return "index";
    }

    @GetMapping("/current")
    public String current(@AuthenticationPrincipal Principal principal) {
        List<Progress> mostRecentProgress = progressService.getMostRecentProgressByUser(principal.getName());
        if (mostRecentProgress.size() > 0) {
            return "redirect:/book/" + mostRecentProgress.get(0).getBook().getId();
        } else {
            return "redirect:/index";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/myaccount")
    public String account() {
        return "/admin/myaccount";
    }

    @PutMapping("/myaccount")
    public String accountUpdate(Model model, @RequestBody DAOPassword password, @AuthenticationPrincipal Principal principal) {
        model.addAttribute("success", userService.updatePassword(principal.getName(), password));
        return "/admin/myaccount";
    }
}
