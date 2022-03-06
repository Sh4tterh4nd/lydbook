package com.lydbook.audiobook.controllers.api;

import com.lydbook.audiobook.services.ProgressService;
import com.lydbook.audiobook.util.BookUtil;
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
    public String updateProgress(@PathVariable("bookId") Long bookId, @RequestBody String body, Principal principal) {
        if (BookUtil.isNumeric(body)) {
            progressService.updateProgress(principal.getName(), bookId, (int) Double.parseDouble(body));
        }
        return "";
    }

}
