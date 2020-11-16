package ch.fhnw.webec;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("/admin/upload")
    public String uploadView(){
        return "/admin/upload";
    }

    @GetMapping("/admin/permissions")
    public String permissionView(){
        return "/admin/permissions";
    }
}
