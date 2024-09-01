package sanity.nil.webchat.presentation.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sanity.nil.webchat.application.dto.chat.*;
import sanity.nil.webchat.application.dto.role.AddChatRoleDTO;
import sanity.nil.webchat.application.dto.role.AddMemberRoleDTO;
import sanity.nil.webchat.application.dto.role.DeleteMemberRoleDTO;
import sanity.nil.webchat.application.facade.ChatFacade;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/chat")
@Slf4j
@CrossOrigin
@RequiredArgsConstructor
public class ChatController {

    private final ChatFacade chatFacade;

    @Operation(description = "Creates a chat")
    @PostMapping("")
    public ResponseEntity<ChatCreatedDTO> createChat(@RequestBody CreateChatDTO dto) {
        return ResponseEntity
                .status(CREATED)
                .body(chatFacade.createChat(dto));
    }

    @Operation(description = "Deletes a chat")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChat(
            @PathVariable UUID id
    ) {
        chatFacade.deleteChat(id);
        return ResponseEntity
                .status(NO_CONTENT)
                .body(null);
    }

    @Operation(description = "Adds a member to a chat")
    @PostMapping("/join")
    public ResponseEntity<Void> joinChat(
            @RequestBody JoinChatDTO dto
    ) {
        chatFacade.joinChat(dto);
        return ResponseEntity
                .status(CREATED)
                .body(null);
    }

    @Operation(description = "Removes a member from a chat")
    @DeleteMapping("/leave")
    public ResponseEntity<Void> leaveChat(
            @RequestBody LeaveChatDTO dto
    ) {
        chatFacade.leaveChat(dto);
        return ResponseEntity
                .status(NO_CONTENT)
                .body(null);
    }

    @Operation(description = "Adds a new role to a chat")
    @PostMapping("/role")
    public ResponseEntity<UUID> addChatRole(
            @RequestBody AddChatRoleDTO dto
    ) {
        return ResponseEntity
                .status(CREATED)
                .body(chatFacade.addChatRole(dto));
    }

    @Operation(description = "Adds an existing role to a member")
    @PutMapping("/role")
    public ResponseEntity<Void> addMemberRole(
            @RequestBody AddMemberRoleDTO dto
    ) {
        chatFacade.addMemberRole(dto);
        return ResponseEntity
                .status(OK)
                .body(null);
    }

    @Operation(description = "Deletes a custom role from member, leaves him with default")
    @DeleteMapping("/role")
    public ResponseEntity<Void> deleteMemberRole(
            @RequestBody DeleteMemberRoleDTO dto
    ) {
        chatFacade.deleteMemberRole(dto);
        return ResponseEntity
                .status(NO_CONTENT)
                .body(null);
    }

    @Operation(description = "Gets all members in a chat")
    @GetMapping("/{id}/members")
    public ResponseEntity<List<ChatMemberDTO>> getChatMembers(
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "limit", required = false) Integer limit,
            @PathVariable("id") UUID chatID
    ) {
        return ResponseEntity
                .status(OK)
                .body(chatFacade.getChatMembers(chatID));
    }

}
