package sanity.nil.webchat.presentation.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sanity.nil.webchat.application.consts.TokenType;
import sanity.nil.webchat.application.dto.*;
import sanity.nil.webchat.application.interactor.ChatFacade;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@CrossOrigin
@RequiredArgsConstructor
public class Controller {

    private final ChatFacade chatFacade;

    @Operation(description = "Creates a chat")
    @PostMapping("/chat")
    public ResponseEntity<ChatCreatedDTO> createChat(@RequestBody CreateChatDTO dto) {
        return ResponseEntity
                .status(CREATED)
                .body(chatFacade.createChat(dto));
    }

    @Operation(description = "Deletes a chat")
    @DeleteMapping("/chat/{id}")
    public ResponseEntity<Void> deleteChat(
            @PathVariable UUID id
    ) {
        chatFacade.deleteChat(id);
        return ResponseEntity
                .status(NO_CONTENT)
                .body(null);
    }

    @Operation(description = "Adds a member to a chat")
    @PostMapping("/chat/join")
    public ResponseEntity<Void> joinChat(
            @RequestBody JoinChatDTO dto
    ) {
        chatFacade.joinChat(dto);
        return ResponseEntity
                .status(CREATED)
                .body(null);
    }

    @Operation(description = "Removes a member from a chat")
    @DeleteMapping("/chat/leave")
    public ResponseEntity<Void> leaveChat(
            @RequestBody LeaveChatDTO dto
    ) {
        chatFacade.leaveChat(dto);
        return ResponseEntity
                .status(NO_CONTENT)
                .body(null);
    }

    @Operation(description = "Adds a new role to a chat")
    @PostMapping("/chat/role")
    public ResponseEntity<UUID> addChatRole(
            @RequestBody AddChatRoleDTO dto
    ) {
        return ResponseEntity
                .status(CREATED)
                .body(chatFacade.addChatRole(dto));
    }

    @Operation(description = "Adds an existing role to a member")
    @PutMapping("/chat/role")
    public ResponseEntity<Void> addMemberRole(
            @RequestBody AddMemberRoleDTO dto
    ) {
        chatFacade.addMemberRole(dto);
        return ResponseEntity
                .status(OK)
                .body(null);
    }

    @Operation(description = "Deletes a custom role from member, leaves him with default")
    @DeleteMapping("/chat/role")
    public ResponseEntity<Void> deleteMemberRole(
            @RequestBody DeleteMemberRoleDTO dto
    ) {
        chatFacade.deleteMemberRole(dto);
        return ResponseEntity
                .status(NO_CONTENT)
                .body(null);
    }

    @Operation(description = "Gets all chats of a member")
    @GetMapping("/member/{id}/chats")
    public ResponseEntity<List<MemberChatsDTO>> getAllChatsByMember(
            @PathVariable("id") UUID memberID
    ) {
        return ResponseEntity
                .status(OK)
                .body(chatFacade.getAllMemberChats(memberID));
    }

    @Operation(description = "Gets all members in a chat")
    @GetMapping("/chat/{id}/members")
    public ResponseEntity<List<ChatMemberDTO>> getChatMembers(
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "limit", required = false) Integer limit,
            @PathVariable("id") UUID chatID
    ) {
        return ResponseEntity
                .status(OK)
                .body(chatFacade.getChatMembers(chatID));
    }

    @Operation(description = "Searches messages by filters")
    @PostMapping("/message/search")
    public ResponseEntity<PagedChatMessagesDTO> getMessagesByFilters(
            @RequestBody MessageFiltersDTO filters
    ) {
        return ResponseEntity
                .status(OK)
                .body(chatFacade.getFilteredMessages(filters));
    }

    @PostMapping("/message")
    public ResponseEntity<MessageCreatedDTO> sendMessage(
            @RequestBody OnSendMessageDTO messageDTO
    ) {
        return ResponseEntity
                .status(CREATED)
                .body(chatFacade.sendMessage(messageDTO));
    }

    @Operation(description = "Gets a token of specified type for centrifugo")
    @GetMapping("/token")
    public ResponseEntity<String> getToken(
            @RequestParam(value = "type") TokenType tokenType,
            @RequestParam(value = "channel", required = false) String channel
    ) {
        return ResponseEntity
                .status(OK)
                .body(chatFacade.getToken(tokenType, channel));
    }

    @PostMapping("/upload")
    public Mono<ResponseEntity<UploadedFilesDTO>> uploadFiles(
            @RequestPart("files") Flux<FilePart> files
    ) {
        return chatFacade.uploadFiles(files)
                .map(response -> ResponseEntity
                        .status(CREATED)
                        .body(response)
                );
    }


    @Operation(description = "Searches files by filters")
    @PostMapping(value = "/file/search")
    public ResponseEntity<List<FileURLDTO>> getFileURLs(
            @RequestBody FileSearchDTO fileSearchDTO

    ) {
        return ResponseEntity
                .status(OK)
                .body(chatFacade.fileSearch(fileSearchDTO));
    }
}
