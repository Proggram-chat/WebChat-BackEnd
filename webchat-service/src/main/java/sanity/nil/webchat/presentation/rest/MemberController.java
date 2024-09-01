package sanity.nil.webchat.presentation.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sanity.nil.webchat.application.dto.chat.MemberChatsDTO;
import sanity.nil.webchat.application.facade.MemberFacade;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/member")
@Slf4j
@CrossOrigin
@RequiredArgsConstructor
public class MemberController {

    private final MemberFacade memberFacade;

    @Operation(description = "Gets all chats of a member")
    @GetMapping("/{id}/chats")
    public ResponseEntity<List<MemberChatsDTO>> getAllChatsByMember(
            @PathVariable("id") UUID memberID
    ) {
        return ResponseEntity
                .status(OK)
                .body(memberFacade.getAllMemberChats(memberID));
    }
}

