package es.us.dp1.lx_xy_24_25.Escape_From_Elba.chat;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<ChatMessage, Integer> {
   
    List<ChatMessage> findByMatchId(Integer id);
} 
