package com.example.ContLog.service;

import com.example.ContLog.entity.Terminal;
import com.example.ContLog.repository.TerminalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminalService {

    @Autowired
    private TerminalRepository terminalRepository;

    // Поиск всех терминалов
    public List<Terminal> getAllTerminals() {
        return terminalRepository.findAll();
    }

    // Поиск Терминала по Id
    public Terminal getTerminal(Long id) {
        return terminalRepository.findById(id).orElse(null);
    }

    // Сохранение терминала
    public Terminal createTerminal(Terminal terminal) {
        return terminalRepository.save(terminal);
    }

    // Обновление данных терминала в БД
    public void updateTerminal(Terminal terminal, Long id) {
        Terminal trml = terminalRepository.getReferenceById(id);
        trml.setShortName(terminal.getShortName());
        trml.setFullName(terminal.getFullName());
        trml.setAddress(terminal.getAddress());
        trml.setNavi(terminal.getNavi());
        trml.setFromContainerdrss(terminal.getFromContainerdrss());
        trml.setToContainerdrss(terminal.getToContainerdrss());
        terminalRepository.save(trml);
    }

    // Удаление Терминала из БД
    public void deleteTerminal(Long id) {
        terminalRepository.deleteById(id);
    }
}
