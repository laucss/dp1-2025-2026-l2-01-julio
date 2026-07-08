package es.us.dp1.lx_xy_24_25.Escape_From_Elba.invitations;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchStatus;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class InvitationControllerTest {

    @Autowired
    private InvitationRepository InvitationRepository;

    private User sender;
    private User receiver;
    private Match match;

    @BeforeEach
    void setUp() {
        sender = new User();
        sender.setId(101);
        sender.setUsername("sender");

        receiver = new User();
        receiver.setId(102);
        receiver.setUsername("receiver");

        match = new Match();
        match.setId(201);
        match.setStatus(MatchStatus.WAITING);
        match.setPlayers(new ArrayList<>());
    }

@Test
void shouldReturnEmptyPendingInvitations() {

    List<InvitationMatch> invitations =
            InvitationRepository.findByReceiverIdAndStatus(
                    receiver.getId(),
                    InvitationStatus.PENDING);

    assertTrue(invitations.isEmpty());
}


@Test
void shouldReturnEmptyWhenInvitationBetweenUsersDoesNotExist() {

    Optional<InvitationMatch> result =
            InvitationRepository.findBySenderIdAndReceiverIdAndMatchIdAndStatus(
                    sender.getId(),
                    receiver.getId(),
                    match.getId(),
                    InvitationStatus.PENDING);

    assertTrue(result.isEmpty());
}


}
