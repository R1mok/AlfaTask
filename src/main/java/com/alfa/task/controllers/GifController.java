package com.alfa.task.controllers;

import com.alfa.task.service.GifService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Контроллер для получения gif изображения
 */
@Controller
public class GifController {

    @Value("${attribute_name}")
    private String attributeName;
    private final GifService gifService;

    public GifController(GifService gifService) {
        this.gifService = gifService;
    }

    @GetMapping("/{code}")
    public String getGif(Model model, @PathVariable String code) {
        try {
            String gifUrl = gifService.getGif(code);
            model.addAttribute(attributeName, gifUrl);
            return "gif.html";
        } catch (IllegalArgumentException e) {
            return "error.html";
        }
    }
}
