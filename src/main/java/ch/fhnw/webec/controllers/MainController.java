package ch.fhnw.webec.controllers;

import ch.fhnw.webec.entity.Progress;
import ch.fhnw.webec.services.ProgressService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class MainController {

    private final ProgressService progressService;

    public MainController(ProgressService progressService) {
        this.progressService = progressService;
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
            model.addAttribute("progresses", mostRecentProgress.subList(1, mostRecentProgress.size() > 10 ? 10: mostRecentProgress.size() ));
        } else {
            model.addAttribute("progresses", null);
        }
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
