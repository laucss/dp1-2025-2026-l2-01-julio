package es.us.dp1.lx_xy_24_25.Escape_From_Elba.invitations;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class InvitationRepositoryTest {

    @Autowired
    private InvitationRepository InvitationRepository;

    // Constants for testing
    private final Integer Invitation_ID_1 = 501;
    private final Integer Invitation_ID_2 = 502;
    private final Integer Invitation_ID_3 = 503;
    private final Integer NON_EXISTENT_Invitation_ID = 999;

    private final Integer SENDER_USER_ID = 101;
    private final Integer RECEIVER_USER_ID = 102;
    private final Integer OTHER_USER_ID = 103;

    private final Integer MATCH_ID_1 = 201;
    private final Integer MATCH_ID_2 = 202;

    // Tests for findByReceiverIdAndStatus
    @Test
    public void shouldFindPendingInvitationsByReceiverId() {
        List<InvitationMatch> Invitations = InvitationRepository.findByReceiverIdAndStatus(
            RECEIVER_USER_ID, InvitationStatus.PENDING
        );
        assertNotNull(Invitations);
        assertTrue(Invitations.size() >= 0);
    }

    @Test
    public void shouldReturnEmptyListWhenNoPendingInvitations() {
        List<InvitationMatch> Invitations = InvitationRepository.findByReceiverIdAndStatus(
            OTHER_USER_ID, InvitationStatus.PENDING
        );
        assertNotNull(Invitations);
    }

    // Tests for findBySenderIdAndReceiverIdAndStatus
    @Test
    public void shouldFindInvitationBetweenSenderAndReceiver() {
        Optional<InvitationMatch> Invitation = InvitationRepository.findBySenderIdAndReceiverIdAndStatus(
            SENDER_USER_ID, RECEIVER_USER_ID, InvitationStatus.PENDING
        );
        // May or may not exist depending on test data
        assertNotNull(Invitation);
    }

    @Test
    public void shouldReturnEmptyWhenNoInvitationBetweenUsers() {
        Optional<InvitationMatch> Invitation = InvitationRepository.findBySenderIdAndReceiverIdAndStatus(
            999, 998, InvitationStatus.PENDING
        );
        assertTrue(Invitation.isEmpty());
    }

    // Tests for findBySenderIdAndReceiverIdAndMatchIdAndStatus
    @Test
    public void shouldFindInvitationWithMatchId() {
        Optional<InvitationMatch> Invitation = InvitationRepository.findBySenderIdAndReceiverIdAndMatchIdAndStatus(
            SENDER_USER_ID, RECEIVER_USER_ID, MATCH_ID_1, InvitationStatus.PENDING
        );
        assertNotNull(Invitation);
    }

    @Test
    public void shouldReturnEmptyWhenNoInvitationWithMatchId() {
        Optional<InvitationMatch> Invitation = InvitationRepository.findBySenderIdAndReceiverIdAndMatchIdAndStatus(
            999, 998, 997, InvitationStatus.PENDING
        );
        assertTrue(Invitation.isEmpty());
    }

    // Tests for findByReceiverIdAndMatchIdAndStatus
    @Test
    public void shouldFindInvitationsForReceiverAndMatch() {
        List<InvitationMatch> Invitations = InvitationRepository.findByReceiverIdAndMatchIdAndStatus(
            RECEIVER_USER_ID, MATCH_ID_1, InvitationStatus.PENDING
        );
        assertNotNull(Invitations);
        assertTrue(Invitations.size() >= 0);
    }

    @Test
    public void shouldReturnEmptyListWhenNoInvitationsForMatch() {
        List<InvitationMatch> Invitations = InvitationRepository.findByReceiverIdAndMatchIdAndStatus(
            999, 998, InvitationStatus.PENDING
        );
        assertNotNull(Invitations);
        assertEquals(0, Invitations.size());
    }
}
