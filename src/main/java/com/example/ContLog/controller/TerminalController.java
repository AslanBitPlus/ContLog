package com.example.ContLog.controller;

import com.example.ContLog.entity.Terminal;
import com.example.ContLog.service.TerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class TerminalController {

    @Autowired
    private TerminalService terminalService;

    @GetMapping("/terminals")
    public String listTerminals(Model model) {
        model.addAttribute("terminals", terminalService.getAllTerminals());
        return "terminal/list";
    }

    @GetMapping("/terminals/create")
    public String createTerminalPage(Model model) {
        Terminal terminal = new Terminal();
        model.addAttribute("terminal", terminal);
        model.addAttribute("isUpdate", false);
        return "terminal/crud"; // ??
    }

    @GetMapping("/terminals/update/{id}")
    public String updateTerminalPage(@PathVariable Long id, Model model) {
        Terminal terminal = terminalService.getTerminal(id);
        model.addAttribute("terminal", terminal);
        model.addAttribute("isUpdate", true);
        return "terminal/crud"; // ??
    }

    @PostMapping("/terminals/update/{id}")
    public String createTerminal(@ModelAttribute("terminal") Terminal terminal,
                                @PathVariable Long id) {
        terminalService.updateTerminal(terminal, id);
        return "redirect:/terminals";
    }

    @PostMapping("/terminals/create")
    public String createTerminal(@ModelAttribute("terminal") Terminal terminal) {
        terminalService.createTerminal(terminal);
        return "redirect:/terminals";
    }

    @GetMapping("/terminals/delete/{id}")
    public String deleteTerminal(@PathVariable Long id) {
        terminalService.deleteTerminal(id);
        return "redirect:/terminals";
    }

    // Mapping для ссылок Яндекс-Навигатора
    @GetMapping("/terminals/link/{id}")
    public RedirectView redirectToYandexNavi(@PathVariable Long id) {
        Terminal trm = terminalService.getTerminal(id);
        String naviUrl = trm.getNavi();
        return new RedirectView(naviUrl);
    }

}
