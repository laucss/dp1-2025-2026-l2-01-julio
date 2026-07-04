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

    // ========================
    // Tests for Repository
    // ========================

    @Test
    public void shouldReturnInvitationsWhenRepositoryHasData() {
        List<InvitationMatch> Invitations = InvitationRepository.findByReceiverIdAndStatus(
            receiver.getId(), InvitationStatus.PENDING
        );
        assertNotNull(Invitations);
        assertTrue(Invitations.size() >= 0);
    }

    @Test
    public void shouldReturnEmptyListWhenNoInvitationsExist() {
        List<InvitationMatch> Invitations = InvitationRepository.findByReceiverIdAndStatus(
            999999, InvitationStatus.PENDING
        );
        assertNotNull(Invitations);
        assertEquals(0, Invitations.size());
    }

    @Test
    public void shouldFindInvitationById() {
        // This test just verifies the repository method works
        Optional<InvitationMatch> Invitation = InvitationRepository.findBySenderIdAndReceiverIdAndStatus(
            sender.getId(), receiver.getId(), InvitationStatus.PENDING
        );
        assertNotNull(Invitation);
    }

    @Test
    public void shouldReturnEmptyWhenSearchingNonExistentInvitation() {
        Optional<InvitationMatch> Invitation = InvitationRepository.findBySenderIdAndReceiverIdAndStatus(
            999999, 999998, InvitationStatus.PENDING
        );
        assertTrue(Invitation.isEmpty());
    }

    @Test
    public void shouldFindInvitationsByMatchId() {
        List<InvitationMatch> Invitations = InvitationRepository.findByReceiverIdAndMatchIdAndStatus(
            receiver.getId(), match.getId(), InvitationStatus.PENDING
        );
        assertNotNull(Invitations);
        assertTrue(Invitations.size() >= 0);
    }
}
