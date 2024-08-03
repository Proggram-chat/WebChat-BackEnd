package sanity.nil.webchat.presentation.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Component;
import sanity.nil.webchat.application.consts.MessageStatus;
import sanity.nil.webchat.application.dto.MessageSentDTO;
import sanity.nil.webchat.presentation.websocket.impl.MessageToSend;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageSender {

    private final SendMessage sendMessage;

    public CompletableFuture<MessageSentDTO> sendMessageAsync(UUID messageID, UUID senderID, UUID chatID, String content) {
        return CompletableFuture.supplyAsync(() -> sendMessage.send(new MessageToSend(messageID, senderID,
                        chatID, content, MessageStatus.DELIVERED)));
    }

}
