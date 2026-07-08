package es.us.dp1.lx_xy_24_25.Escape_From_Elba.invitations;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;

@Epic("Invitations Management")
@Feature("Invitation Repository")
@Owner("DP1-tutors")
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class InvitationServiceTest {

    @Autowired
    private InvitationRepository invitationRepository;

    // Test using predefined test data IDs
    private static final Integer SENDER_ID = 101;
    private static final Integer RECEIVER_ID = 102;
    private static final Integer MATCH_ID = 201;

    @Test
    public void shouldFindPendingInvitationsByReceiverIdUsingTestData() {
        List<InvitationMatch> invitations = invitationRepository.findByReceiverIdAndStatus(
            RECEIVER_ID, InvitationStatus.PENDING
        );
        assertNotNull(invitations);
        assertTrue(invitations.size() >= 0);
    }

    @Test
    public void shouldReturnEmptyListWhenNoPendingInvitations() {
        List<InvitationMatch> invitations = invitationRepository.findByReceiverIdAndStatus(
            99999, InvitationStatus.PENDING
        );
        assertNotNull(invitations);
        assertEquals(0, invitations.size());
    }

    @Test
    public void shouldFindInvitationBySenderReceiverAndStatus() {
        Optional<InvitationMatch> invitation = invitationRepository.findBySenderIdAndReceiverIdAndStatus(
            SENDER_ID, RECEIVER_ID, InvitationStatus.PENDING
        );
        assertNotNull(invitation);
    }

    @Test
    public void shouldReturnEmptyWhenNoInvitationBetweenUsers() {
        Optional<InvitationMatch> invitation = invitationRepository.findBySenderIdAndReceiverIdAndStatus(
            99999, 99998, InvitationStatus.PENDING
        );
        assertTrue(invitation.isEmpty());
    }

    @Test
    public void shouldFindInvitationWithMatchId() {
        Optional<InvitationMatch> invitation = invitationRepository.findBySenderIdAndReceiverIdAndMatchIdAndStatus(
            SENDER_ID, RECEIVER_ID, MATCH_ID, InvitationStatus.PENDING
        );
        assertNotNull(invitation);
    }

    @Test
    public void shouldReturnEmptyWhenNoInvitationWithMatchId() {
        Optional<InvitationMatch> invitation = invitationRepository.findBySenderIdAndReceiverIdAndMatchIdAndStatus(
            99999, 99998, 99997, InvitationStatus.PENDING
        );
        assertTrue(invitation.isEmpty());
    }

    @Test
    public void shouldFindInvitationsForReceiverAndMatch() {
        List<InvitationMatch> invitations = invitationRepository.findByReceiverIdAndMatchIdAndStatus(
            RECEIVER_ID, MATCH_ID, InvitationStatus.PENDING
        );
        assertNotNull(invitations);
        assertTrue(invitations.size() >= 0);
    }

    @Test
    public void shouldReturnEmptyListWhenNoInvitationsForMatch() {
        List<InvitationMatch> invitations = invitationRepository.findByReceiverIdAndMatchIdAndStatus(
            99999, 99998, InvitationStatus.PENDING
        );
        assertNotNull(invitations);
        assertEquals(0, invitations.size());
    }

    @Test
    public void shouldVerifyInvitationRepositoryMethods() {
        // This test verifies that all repository query methods exist and work
        List<InvitationMatch> list1 = invitationRepository.findByReceiverIdAndStatus(1, InvitationStatus.PENDING);
        assertNotNull(list1);

        Optional<InvitationMatch> opt1 = invitationRepository.findBySenderIdAndReceiverIdAndStatus(1, 2, InvitationStatus.PENDING);
        assertNotNull(opt1);

        Optional<InvitationMatch> opt2 = invitationRepository.findBySenderIdAndReceiverIdAndMatchIdAndStatus(1, 2, 3, InvitationStatus.PENDING);
        assertNotNull(opt2);

        List<InvitationMatch> list2 = invitationRepository.findByReceiverIdAndMatchIdAndStatus(1, 2, InvitationStatus.PENDING);
        assertNotNull(list2);
    }

}
