package ch.fhnw.webec.controllers.api;

import ch.fhnw.webec.services.ProgressService;
import ch.fhnw.webec.util.AudiobookUtil;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/v1/progress")
public class ApiProgressController {
    private final ProgressService progressService;

    public ApiProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @PostMapping("{bookId}/")
    public String updateProgress(@PathVariable("bookId") Long bookId, @RequestBody String body, @AuthenticationPrincipal Principal principal) {
        if (AudiobookUtil.isNumeric(body)) {
            progressService.updateProgress(principal.getName(), bookId, (int) Double.parseDouble(body));
        }
        return "";
    }

}
