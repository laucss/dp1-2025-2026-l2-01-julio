package es.us.dp1.lx_xy_24_25.Escape_From_Elba.invitations;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<InvitationMatch, Integer> {
    List<InvitationMatch> findByReceiverIdAndStatus(Integer receiverId, InvitationStatus status);
    Optional<InvitationMatch> findBySenderIdAndReceiverIdAndStatus(Integer senderId, Integer receiverId, InvitationStatus status);
    Optional<InvitationMatch> findBySenderIdAndReceiverIdAndMatchIdAndStatus(Integer senderId, Integer receiverId, Integer matchId, InvitationStatus status);
    List<InvitationMatch> findByReceiverIdAndMatchIdAndStatus(Integer receiverId, Integer matchId, InvitationStatus status);
}
