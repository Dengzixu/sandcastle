package io.toolongname.sandcastle.web;

import io.toolongname.sandcastle.model.ResponseData;
import io.toolongname.sandcastle.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class BaseController {
    @GetMapping("/ping")
    public ResponseEntity<ResponseData> ping() {
        return ResponseEntity.ok(ResponseData.SUCCEEDED("pong"));
    }
}
