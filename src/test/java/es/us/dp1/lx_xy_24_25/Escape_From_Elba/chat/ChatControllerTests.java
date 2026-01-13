package es.us.dp1.lx_xy_24_25.Escape_From_Elba.chat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.configuration.SecurityConfiguration;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class ChatControllerTests {

    @MockBean
    private ChatService chatService;

    @MockBean
    private UserService userService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private ChatRepository chatRepository;

    @Autowired
    private MockMvc mvc;

    private static final String BASE_URL = "/api/v1/match/{matchId}/chat";


    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void getMyChatEmptyTest() throws Exception {

        reset(chatService);
        when(chatService.findChatOfMyGame(1))
            .thenReturn(Collections.emptyList());

        mvc.perform(get(BASE_URL + "/my", 1))
            .andExpect(status().isOk());

        verify(chatService, times(1)).findChatOfMyGame(1);
    }


    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void createChatMessageValidTest()
            throws JsonProcessingException, Exception {

        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setMessage("Mensaje válido");

        ChatMessage saved = new ChatMessage();
        saved.setMessage("Mensaje válido");

        ObjectMapper mapper = new ObjectMapper();

        reset(chatService);
        when(chatService.createChatMessage(eq(1), any(ChatMessageDTO.class)))
            .thenReturn(saved);

        mvc.perform(post(BASE_URL, 1)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
            .andExpect(status().isOk());

        verify(chatService, times(1))
            .createChatMessage(eq(1), any(ChatMessageDTO.class));
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void createChatMessageInvalidTest()
            throws JsonProcessingException, Exception {

        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setMessage(""); // @NotBlank

        ObjectMapper mapper = new ObjectMapper();

        reset(chatService);

        mvc.perform(post(BASE_URL, 1)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());

        verify(chatService, never())
            .createChatMessage(any(Integer.class), any(ChatMessageDTO.class));
    }
}
