package amirhs.de.stage.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
    List<ChatMessage> findByChatId(String chatId);

    @Query("SELECT new amirhs.de.stage.chat.ChatMemberHistoryDTO(" +
            "cm.chatId, cm.senderId, cm.recipientId, cm.content, cm.timestamp, " +
            "u1.firstname, u1.lastname, u2.firstname, u2.lastname, " +
            "u1.city, u2.city, u1.avatar, u2.avatar, u1.gender, u2.gender, " +
            "(SELECT CAST(COUNT(cm2.id) AS string) FROM ChatMessage cm2 WHERE cm2.chatId = cm.chatId AND cm2.isRead = false AND cm2.recipientId = :senderId)) " +
            "FROM ChatMessage cm " +
            "JOIN User u1 ON CAST(cm.senderId AS integer) = u1.id " +
            "JOIN User u2 ON CAST(cm.recipientId AS integer) = u2.id " +
            "WHERE cm.timestamp = (SELECT MAX(cm2.timestamp) FROM ChatMessage cm2 WHERE cm2.chatId = cm.chatId) " +
            "AND (cm.chatId LIKE CONCAT(:senderId, '_%') OR cm.chatId LIKE CONCAT('%_', :senderId)) " +
            "ORDER BY cm.timestamp DESC")
    List<ChatMemberHistoryDTO> findLastMessagesExcludingSenderWithUserData(@Param("senderId") String senderId);
}
