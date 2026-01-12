package es.us.dp1.lx_xy_24_25.Escape_From_Elba.bag;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagService;

@SpringBootTest
@AutoConfigureMockMvc
public class BagControllerTests {

    @MockBean
    private BagService bagService;

    @Autowired
    private MockMvc mvc;

    private static final String BASE_URL = "/api/v1/bag/validate-weapon";



    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    void validateWeaponValidWeaponReturnsBonus() throws Exception {

        CardDTO c1 = new CardDTO();
        c1.setLetter("S");
        CardDTO c2 = new CardDTO();
        c2.setLetter("W");
        CardDTO c3 = new CardDTO();
        c3.setLetter("O");
        CardDTO c4 = new CardDTO();
        c4.setLetter("R");
        CardDTO c5 = new CardDTO();
        c5.setLetter("D");

        BagInGameDTO dto = new BagInGameDTO();
        dto.setCards(List.of(c1, c2, c3, c4, c5));

        ObjectMapper mapper = new ObjectMapper();

        reset(bagService);
        when(bagService.isValidWeapon(any()))
            .thenReturn(true);

        mvc.perform(post(BASE_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
            .andExpect(status().isOk());

        verify(bagService, times(1))
            .isValidWeapon(any());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    void validateWeaponInvalidWeaponReturnsNoBonus() throws Exception {

        CardDTO c1 = new CardDTO();
        c1.setLetter("A");
        CardDTO c2 = new CardDTO();
        c2.setLetter("B");

        BagInGameDTO dto = new BagInGameDTO();
        dto.setCards(List.of(c1, c2));

        ObjectMapper mapper = new ObjectMapper();

        reset(bagService);
        when(bagService.isValidWeapon(any()))
            .thenReturn(false);

        mvc.perform(post(BASE_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
            .andExpect(status().isOk());

        verify(bagService, times(1))
            .isValidWeapon(any());
    }

    @Test
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    void validateWeaponInvalidBodyReturnsBadRequest() throws Exception {

        BagInGameDTO dto = new BagInGameDTO();

        ObjectMapper mapper = new ObjectMapper();

        reset(bagService);

        mvc.perform(post(BASE_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());

        verify(bagService, never())
            .isValidWeapon(any());
    }
}
