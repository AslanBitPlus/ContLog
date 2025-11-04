package com.example.ContLog.controller;

import com.example.ContLog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ContainerdrsController {

    @Autowired
    private ContainerdrsService containerdrsService;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private SaleService saleService;
}
