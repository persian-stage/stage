package amirhs.de.stage.chat;

import amirhs.de.stage.service.UserService;
import amirhs.de.stage.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final UserService userService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage, @AuthenticationPrincipal Principal principal) {
        Optional<User> currentUser = userService.getUserWithEmail(principal.getName());
        String senderId = currentUser.map(user -> user.getId() + "").orElse("0");

        if (senderId.equals("0") || chatMessage.getRecipientId().equals(senderId)) {
            return;
        }

        chatMessage.setRead(false);
        ChatMessage saved = chatMessageService.save(chatMessage);

        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(),
                "/queue/messages",
                new ChatNotification(
                        1,
                        senderId,
                        chatMessage.getRecipientId(),
                        chatMessage.getContent()
                )
        );
    }

    @GetMapping("/history")
    public ResponseEntity<List<ChatMemberHistoryDTO>> findChatWithMembers(
            @AuthenticationPrincipal UserDetails user
    ) {
        Optional<User> currentUser = userService.getUserWithEmail(user.getUsername());
        return currentUser.map(value -> ResponseEntity
                .ok(chatMessageService.findChatMessagesWithMember(value.getId() + ""))).orElseGet(() -> ResponseEntity.ok(List.of()));
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(
            @PathVariable String senderId,
            @PathVariable String recipientId,
            @AuthenticationPrincipal UserDetails user
    ) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }
}
