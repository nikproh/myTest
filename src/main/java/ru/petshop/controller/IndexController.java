package ru.petshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class IndexController {
    /**
     * @return редирект на страницу Swagger-UI
     */
    @GetMapping("/")
    public View swagger() {
        return new RedirectView("swagger-ui.html");
    }

    @GetMapping("/OK")
    @ResponseBody
    String root() {
        return "OK";
    }
}
