package sanity.nil.webchat.presentation.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sanity.nil.webchat.application.consts.TokenType;
import sanity.nil.webchat.application.dto.role.FunctionDTO;
import sanity.nil.webchat.application.facade.GeneralFacade;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@CrossOrigin
@RequiredArgsConstructor
public class GeneralController {

    private final GeneralFacade generalFacade;

    @Operation(description = "Gets all the available functions to assign to a custom role")
    @GetMapping("/functions")
    public ResponseEntity<List<FunctionDTO>> getAllFunctions() {
        return ResponseEntity
                .status(OK)
                .body(generalFacade.getAllFunctions());
    }

    @Operation(description = "Gets a token of specified type for centrifugo")
    @GetMapping("/token")
    public ResponseEntity<String> getToken(
            @RequestParam(value = "type") TokenType tokenType,
            @RequestParam(value = "channel", required = false) String channel
    ) {
        return ResponseEntity
                .status(OK)
                .body(generalFacade.getToken(tokenType, channel));
    }
}
