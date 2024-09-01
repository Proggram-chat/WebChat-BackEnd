package sanity.nil.webchat.presentation.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sanity.nil.webchat.application.dto.message.*;
import sanity.nil.webchat.application.facade.MessageFacade;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/message")
@Slf4j
@CrossOrigin
@RequiredArgsConstructor
public class MessageController {

    private final MessageFacade messageFacade;

    @Operation(description = "Searches messages by filters")
    @PostMapping("/search")
    public ResponseEntity<PagedChatMessagesDTO> getMessagesByFilters(
            @RequestBody MessageFiltersDTO filters
    ) {
        return ResponseEntity
                .status(OK)
                .body(messageFacade.getFilteredMessages(filters));
    }

    @Operation(description = "Sends a message to a chat")
    @PostMapping("")
    public ResponseEntity<MessageCreatedDTO> sendMessage(
            @RequestBody OnSendMessageDTO messageDTO
    ) {
        return ResponseEntity
                .status(CREATED)
                .body(messageFacade.sendMessage(messageDTO));
    }

    @Operation(description = "Modifies a message in a chat")
    @PatchMapping("")
    public ResponseEntity<Void> modifyMessage(
            @RequestBody OnModifyMessageDTO messageDTO
    ) {
        messageFacade.modifyMessage(messageDTO);
        return ResponseEntity
                .status(OK)
                .body(null);
    }

}
