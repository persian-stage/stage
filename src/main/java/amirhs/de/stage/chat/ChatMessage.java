package amirhs.de.stage.chat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String chatId;
    private String senderId;
    private String recipientId;
    private String content;
    private boolean isRead;
    private Date timestamp;

    @Transient
    private String senderName;
    @Transient
    private String recipientName;
}
