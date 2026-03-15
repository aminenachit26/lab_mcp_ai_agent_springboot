package com.example.agent.web;

import com.example.agent.agent.BacklogAgent;
import com.example.agent.mcp.McpHttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AgentControllerIT {

    @Autowired
    WebTestClient web;

    @MockitoBean
    McpHttpClient mcp;

    @MockitoBean
    BacklogAgent backlogAgent;

    @Test
    void should_call_endpoint() {
        when(backlogAgent.handle(anyString()))
                .thenReturn("Issue created: #1 https://github.com/o/r/issues/1");
        when(mcp.callTool(eq("create_issue"), anyMap()))
                .thenReturn(Mono.just(Map.of("number", 1, "html_url", "https://github.com/o/r/issues/1")));

        web.post()
                .uri("/api/run")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("prompt", "Create a task to add OpenTelemetry"))
                .exchange()
                .expectStatus().isOk();
    }
}
