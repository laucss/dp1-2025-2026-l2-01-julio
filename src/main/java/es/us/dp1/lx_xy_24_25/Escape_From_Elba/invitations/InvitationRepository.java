package es.us.dp1.lx_xy_24_25.Escape_From_Elba.invitations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<InvitationMatch, Integer> {
    List<InvitationMatch> findByReceiverIdAndStatus(Integer receiverId, InvitationStatus status);
    Optional<InvitationMatch> findBySenderIdAndReceiverIdAndStatus(Integer senderId, Integer receiverId, InvitationStatus status);
    Optional<InvitationMatch> findBySenderIdAndReceiverIdAndMatchIdAndStatus(Integer senderId, Integer receiverId, Integer matchId, InvitationStatus status);
    List<InvitationMatch> findByReceiverIdAndMatchIdAndStatus(Integer receiverId, Integer matchId, InvitationStatus status);

    // esta función es muy similar o igual a la que hay encima pero como no la hice yo y estamos en la recuperación de julio no la voy a cambiar por si rompo algo
    @Query("SELECT i FROM InvitationMatch i WHERE i.match.id = :matchId AND i.receiver.id = :userId AND i.status = 'PENDING' ")
    Optional<InvitationMatch> findPendingInvitationByUserIdAndMatchId(Integer userId, Integer matchId);  

    void deleteByMatchId(Integer matchId);
}
