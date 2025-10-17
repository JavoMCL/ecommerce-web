package com.ecommerce.ecommerce_web.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class PerifericosController {

        @GetMapping("/perifericos")
        public String perifericos() {
        return "perifericos";
    }

}