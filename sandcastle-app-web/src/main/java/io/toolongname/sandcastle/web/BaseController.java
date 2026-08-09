package io.toolongname.sandcastle.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class BaseController {
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    @RequestMapping("/generate_204")
    public ResponseEntity<Void> generate204() {
        return ResponseEntity.noContent().build();
    }
}
