package amirhs.de.stage.chat.chatroom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
    Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId);
    @Query("SELECT cr FROM ChatRoom cr WHERE (cr.senderId = :senderId AND cr.recipientId = :recipientId) OR (cr.senderId = :recipientId AND cr.recipientId = :senderId)")
    Optional<ChatRoom> findBySenderIdAndRecipientIdEitherWay(@Param("senderId") String senderId, @Param("recipientId") String recipientId);
}
