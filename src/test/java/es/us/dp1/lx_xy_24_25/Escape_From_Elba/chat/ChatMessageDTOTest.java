package es.us.dp1.lx_xy_24_25.Escape_From_Elba.chat;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ChatMessageDTO Tests")
public class ChatMessageDTOTest {

    private ChatMessageDTO dto;
    private ChatMessage chatMessage;
    private Player testPlayer;
    private User testUser;
    private Match testMatch;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(10);
        testUser.setUsername("testUser");

        testPlayer = new Player();
        testPlayer.setId(1);
        testPlayer.setUser(testUser);

        testMatch = new Match();
        testMatch.setId(100);

        chatMessage = new ChatMessage();
        chatMessage.setId(1);
        chatMessage.setPlayer(testPlayer);
        chatMessage.setMatch(testMatch);
        chatMessage.setMessage("Test message");
        chatMessage.setTime(LocalDateTime.of(2026, 1, 15, 12, 0));

        dto = new ChatMessageDTO();
    }

    @Test
    @DisplayName("Should create empty ChatMessageDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(dto.getPlayerId()).isNull();
        assertThat(dto.getMatchId()).isNull();
        assertThat(dto.getMessage()).isNull();
        assertThat(dto.getTime()).isNull();
        assertThat(dto.getPlayerUsername()).isNull();
    }

    @Test
    @DisplayName("Should create ChatMessageDTO from ChatMessage entity")
    void testConstructorWithChatMessage() {
        ChatMessageDTO dto = new ChatMessageDTO(chatMessage);

        assertThat(dto.getPlayerId()).isEqualTo(1);
        assertThat(dto.getMatchId()).isEqualTo(100);
        assertThat(dto.getMessage()).isEqualTo("Test message");
        assertThat(dto.getTime()).isEqualTo(LocalDateTime.of(2026, 1, 15, 12, 0));
        assertThat(dto.getPlayerUsername()).isEqualTo("testUser");
    }

    @Test
    @DisplayName("Should handle null ChatMessage in constructor")
    void testConstructorWithNullChatMessage() {
        ChatMessageDTO dto = new ChatMessageDTO(null);

        assertThat(dto.getPlayerId()).isNull();
        assertThat(dto.getMatchId()).isNull();
        assertThat(dto.getMessage()).isNull();
        assertThat(dto.getTime()).isNull();
        assertThat(dto.getPlayerUsername()).isNull();
    }

    @Test
    @DisplayName("Should handle ChatMessage with null player")
    void testConstructorWithNullPlayer() {
        chatMessage.setPlayer(null);
        ChatMessageDTO dto = new ChatMessageDTO(chatMessage);

        assertThat(dto.getPlayerId()).isNull();
        assertThat(dto.getPlayerUsername()).isNull();
        assertThat(dto.getMatchId()).isEqualTo(100);
        assertThat(dto.getMessage()).isEqualTo("Test message");
    }

    @Test
    @DisplayName("Should handle ChatMessage with null match")
    void testConstructorWithNullMatch() {
        chatMessage.setMatch(null);
        ChatMessageDTO dto = new ChatMessageDTO(chatMessage);

        assertThat(dto.getPlayerId()).isEqualTo(1);
        assertThat(dto.getPlayerUsername()).isEqualTo("testUser");
        assertThat(dto.getMatchId()).isNull();
        assertThat(dto.getMessage()).isEqualTo("Test message");
    }

    @Test
    @DisplayName("Should set and get playerId correctly")
    void testSetAndGetPlayerId() {
        dto.setPlayerId(5);
        assertThat(dto.getPlayerId()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should set and get matchId correctly")
    void testSetAndGetMatchId() {
        dto.setMatchId(200);
        assertThat(dto.getMatchId()).isEqualTo(200);
    }

    @Test
    @DisplayName("Should set and get message correctly")
    void testSetAndGetMessage() {
        dto.setMessage("Hello World");
        assertThat(dto.getMessage()).isEqualTo("Hello World");
    }

    @Test
    @DisplayName("Should set and get time correctly")
    void testSetAndGetTime() {
        LocalDateTime time = LocalDateTime.now();
        dto.setTime(time);
        assertThat(dto.getTime()).isEqualTo(time);
    }

    @Test
    @DisplayName("Should set and get playerUsername correctly")
    void testSetAndGetPlayerUsername() {
        dto.setPlayerUsername("player123");
        assertThat(dto.getPlayerUsername()).isEqualTo("player123");
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testHandleNullValues() {
        dto.setPlayerId(1);
        dto.setMatchId(2);
        dto.setMessage("test");
        dto.setTime(LocalDateTime.now());
        dto.setPlayerUsername("user");

        dto.setPlayerId(null);
        dto.setMatchId(null);
        dto.setMessage(null);
        dto.setTime(null);
        dto.setPlayerUsername(null);

        assertThat(dto.getPlayerId()).isNull();
        assertThat(dto.getMatchId()).isNull();
        assertThat(dto.getMessage()).isNull();
        assertThat(dto.getTime()).isNull();
        assertThat(dto.getPlayerUsername()).isNull();
    }

    @Test
    @DisplayName("Should update playerId from non-null to different value")
    void testUpdatePlayerId() {
        dto.setPlayerId(1);
        assertThat(dto.getPlayerId()).isEqualTo(1);

        dto.setPlayerId(2);
        assertThat(dto.getPlayerId()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should update matchId from non-null to different value")
    void testUpdateMatchId() {
        dto.setMatchId(100);
        assertThat(dto.getMatchId()).isEqualTo(100);

        dto.setMatchId(200);
        assertThat(dto.getMatchId()).isEqualTo(200);
    }

    @Test
    @DisplayName("Should update message from non-null to different value")
    void testUpdateMessage() {
        dto.setMessage("First message");
        assertThat(dto.getMessage()).isEqualTo("First message");

        dto.setMessage("Second message");
        assertThat(dto.getMessage()).isEqualTo("Second message");
    }

    @Test
    @DisplayName("Should handle long messages within validation limit")
    void testLongMessage() {
        String longMessage = "A".repeat(500);
        dto.setMessage(longMessage);
        assertThat(dto.getMessage()).hasSize(500);
        assertThat(dto.getMessage()).isEqualTo(longMessage);
    }

    @Test
    @DisplayName("Should handle empty string message")
    void testEmptyMessage() {
        dto.setMessage("");
        assertThat(dto.getMessage()).isEmpty();
    }

    @Test
    @DisplayName("Should handle message with special characters")
    void testMessageWithSpecialCharacters() {
        String specialMessage = "Hello! @#$%^&*() 123 🎉";
        dto.setMessage(specialMessage);
        assertThat(dto.getMessage()).isEqualTo(specialMessage);
    }

    @Test
    @DisplayName("Should handle large player and match IDs")
    void testLargeIds() {
        dto.setPlayerId(Integer.MAX_VALUE);
        dto.setMatchId(Integer.MAX_VALUE);

        assertThat(dto.getPlayerId()).isEqualTo(Integer.MAX_VALUE);
        assertThat(dto.getMatchId()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("Should handle different time values")
    void testDifferentTimeValues() {
        LocalDateTime pastTime = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime futureTime = LocalDateTime.of(2030, 12, 31, 23, 59);

        dto.setTime(pastTime);
        assertThat(dto.getTime()).isEqualTo(pastTime);

        dto.setTime(futureTime);
        assertThat(dto.getTime()).isEqualTo(futureTime);
    }

    @Test
    @DisplayName("Should create independent instances")
    void testIndependentInstances() {
        ChatMessageDTO dto1 = new ChatMessageDTO();
        dto1.setPlayerId(1);
        dto1.setMessage("Message 1");

        ChatMessageDTO dto2 = new ChatMessageDTO();
        dto2.setPlayerId(2);
        dto2.setMessage("Message 2");

        assertThat(dto1.getPlayerId()).isNotEqualTo(dto2.getPlayerId());
        assertThat(dto1.getMessage()).isNotEqualTo(dto2.getMessage());

        dto1.setPlayerId(99);
        assertThat(dto2.getPlayerId()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should handle multiline messages")
    void testMultilineMessage() {
        String multilineMessage = "Line 1\nLine 2\nLine 3";
        dto.setMessage(multilineMessage);
        assertThat(dto.getMessage()).isEqualTo(multilineMessage);
        assertThat(dto.getMessage()).contains("\n");
    }

    @Test
    @DisplayName("Should preserve message with leading and trailing spaces")
    void testMessageWithSpaces() {
        String messageWithSpaces = "  message with spaces  ";
        dto.setMessage(messageWithSpaces);
        assertThat(dto.getMessage()).isEqualTo(messageWithSpaces);
    }

    @Test
    @DisplayName("Should handle username with special characters")
    void testUsernameWithSpecialCharacters() {
        dto.setPlayerUsername("user_123-test");
        assertThat(dto.getPlayerUsername()).isEqualTo("user_123-test");
    }

    @Test
    @DisplayName("Should handle zero values for IDs")
    void testZeroValues() {
        dto.setPlayerId(0);
        dto.setMatchId(0);

        assertThat(dto.getPlayerId()).isEqualTo(0);
        assertThat(dto.getMatchId()).isEqualTo(0);
    }
}
