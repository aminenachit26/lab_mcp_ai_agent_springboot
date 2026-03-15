package com.example.agent.web;

import com.example.agent.service.AgentService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping("/run")
    public String run(@RequestBody Map<String, String> body) {
        String prompt = body.get("prompt");
        if (prompt == null || prompt.isBlank()) {
            throw new IllegalArgumentException("Request body must contain a non-empty 'prompt' field");
        }
        return agentService.run(prompt);
    }
}
