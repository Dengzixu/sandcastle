package io.toolongname.sandcastle.web;

import io.toolongname.sandcastle.entity.bo.user.UserBO;
import io.toolongname.sandcastle.entity.dto.user.LoginDTO;
import io.toolongname.sandcastle.entity.dto.user.RegisterDTO;
import io.toolongname.sandcastle.entity.vo.user.LoginVO;
import io.toolongname.sandcastle.entity.vo.user.UserVO;
import io.toolongname.sandcastle.model.ResponseData;
import io.toolongname.sandcastle.property.JwtProperty;
import io.toolongname.sandcastle.services.UserService;
import io.toolongname.sandcastle.utils.JsonWebToken;
import io.toolongname.sandcastlecommon.misc.exception.user.TokenInvalidException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController()
@RequestMapping("/user/api")
@CrossOrigin(origins = "*")
public class UserApiController {
    private final JwtProperty jwtProperty;
    private final UserService userService;


    public UserApiController(JwtProperty jwtProperty, UserService userService) {
        this.jwtProperty = jwtProperty;
        this.userService = userService;
    }

    @PostMapping("/v1/register")
    public ResponseEntity<Void> register(@RequestBody @Validated RegisterDTO registerDTO) {
        userService.register(registerDTO.username(), registerDTO.email(), registerDTO.password());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/v1/login")
    public ResponseEntity<ResponseData> login(@RequestBody @Validated LoginDTO loginDTO) {
        UserBO userBO = userService.loginByEmail(loginDTO.email(), loginDTO.password());

        JsonWebToken jwt = new JsonWebToken(jwtProperty.validityPeriod(),
                jwtProperty.base64Secret(),
                jwtProperty.algorithm());

        UserVO userVO = UserVO.fromUserBO(userBO);


        String token = jwt.encoder()
                .issuer(jwtProperty.issuer())
                .subject(userBO.uuid().toString())
                .compact();
        LoginVO loginVO = new LoginVO(userVO, token);

        return ResponseEntity.ok(ResponseData.SUCCEEDED_WITH_DATA(loginVO));
    }

    @GetMapping("/v1/token/verify")
    public ResponseEntity<ResponseData> verifyToken(@RequestHeader(value = "Authorization") String token) {
        JsonWebToken jwt = new JsonWebToken(jwtProperty.validityPeriod(),
                jwtProperty.base64Secret(),
                jwtProperty.algorithm());

        String userUuid;
        try {
            userUuid = jwt.decoder(token).getSubject();
        } catch (RuntimeException e) {
            throw new TokenInvalidException();
        }

        UserBO userBO = userService.getByUuid(userUuid);
        UserVO userVO = UserVO.fromUserBO(userBO);

        return ResponseEntity.ok(ResponseData.SUCCEEDED_WITH_DATA(new HashMap<>() {{
            put("user", userVO);
        }}));
    }
}
