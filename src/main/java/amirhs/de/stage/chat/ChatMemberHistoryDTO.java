package amirhs.de.stage.chat;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ChatMemberHistoryDTO {
    private String chatId;
    private String senderId;
    private String recipientId;
    private String content;
    private Date timestamp;
    private String senderFirstname;
    private String senderLastname;
    private String recipientFirstname;
    private String recipientLastname;
    private String senderCity;
    private String recipientCity;
    private String senderAvatar;
    private String recipientAvatar;
    private String senderGender;
    private String recipientGender;
    private String unreadMessages;

    public ChatMemberHistoryDTO(String chatId, String senderId, String recipientId, String content, Date timestamp,
                                String senderFirstname, String senderLastname, String recipientFirstname, String recipientLastname,
                                String senderCity, String recipientCity, String senderAvatar, String recipientAvatar,
                                String senderGender, String recipientGender, String unreadMessages) {
        this.chatId = chatId;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
        this.timestamp = timestamp;
        this.senderFirstname = senderFirstname;
        this.senderLastname = senderLastname;
        this.recipientFirstname = recipientFirstname;
        this.recipientLastname = recipientLastname;
        this.senderCity = senderCity;
        this.recipientCity = recipientCity;
        this.senderAvatar = senderAvatar;
        this.recipientAvatar = recipientAvatar;
        this.senderGender = senderGender;
        this.recipientGender = recipientGender;
        this.unreadMessages = unreadMessages;
    }
}
