package com.company.platform.diagnosis;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/diagnoses")
@Validated
public class DiagnosisController {
    private final DiagnosisService service;
    public DiagnosisController(DiagnosisService service) { this.service = service; }
    public record CreateRequest(@NotBlank @Size(max = 120) String circuitId) {}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Long> create(@Valid @RequestBody CreateRequest request, Principal principal) {
        return Map.of("diagnosisId", service.create(request.circuitId(), principal.getName()).diagnosisId());
    }

    @GetMapping("/{id}")
    public DiagnosisView get(@PathVariable long id) { return service.get(id); }

    @GetMapping
    public List<DiagnosisView> history(@RequestParam @NotBlank String circuitId) {
        return service.history(circuitId);
    }
}
