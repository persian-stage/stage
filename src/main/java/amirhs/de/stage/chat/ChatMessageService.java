package amirhs.de.stage.chat;

import amirhs.de.stage.chat.chatroom.ChatRoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;

//    @Transactional
//    public ChatMessage save(ChatMessage chatMessage) {
//        var chatId = chatRoomService
//                .getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
//                .orElseThrow(); // You can create your own dedicated exception
//        chatMessage.setChatId(chatId);
//        repository.save(chatMessage);
//        return chatMessage;
//    }

    public List<ChatMessage> findChatMessages(Integer senderId, Integer recipientId) {
        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);
        return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
    }
}
