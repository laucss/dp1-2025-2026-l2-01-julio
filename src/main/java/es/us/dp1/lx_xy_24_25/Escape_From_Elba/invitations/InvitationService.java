package es.us.dp1.lx_xy_24_25.Escape_From_Elba.invitations;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.util.Checkers;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InvitationService {

    private InvitationRepository InvitationRepository;
    private UserRepository userRepository;
    private MatchRepository matchRepository; 
    private MatchService matchService; 
    private LobbyService lobbyService;
    public Checkers checkers; 

    @Autowired
    public InvitationService ( InvitationRepository InvitationRepository, UserRepository userRepository, MatchRepository matchRepository,
            Checkers checkers, MatchService matchService, LobbyService lobbyService){
        this.InvitationRepository = InvitationRepository;
        this.matchRepository = matchRepository; 
        this.userRepository = userRepository; 
        this.checkers = checkers; 
        this.matchService = matchService; 
        this.lobbyService = lobbyService;
    }

    public InvitationMatch sendInvite( InviteRequest inviteRequest) throws IllegalArgumentException {

        User sender = userRepository.findById(inviteRequest.getSenderId())
            .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        User receiver = userRepository.findById(inviteRequest.getReceiverId())
            .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));
        Match match = matchRepository.findById(inviteRequest.getMatchId())
            .orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        // Verificar si ya existe una invitación pendiente entre estos dos jugadores específicos
        Optional<InvitationMatch> existingInvite = InvitationRepository.findBySenderIdAndReceiverIdAndStatus(
            inviteRequest.getSenderId(), inviteRequest.getReceiverId(), InvitationStatus.PENDING
        );
        
        if (existingInvite.isPresent()) {
            throw new IllegalArgumentException("Ya existe una invitación pendiente entre estos jugadores");
        }
        
        InvitationMatch invitation = new InvitationMatch();
        invitation.setSender(sender);
        invitation.setReceiver(receiver);
        invitation.setMatch(match);
        invitation.setType(InvitationType.INVITE);
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setSpectator(inviteRequest.isSpectator());
        invitation.setCreatedAt(LocalDateTime.now());
        return InvitationRepository.save(invitation);
    }

    public List<InvitationMatch> getPendingInvitations(Integer receiverId) {
        return InvitationRepository.findByReceiverIdAndStatus(receiverId, InvitationStatus.PENDING);
    }

    public Optional<InvitationMatch> getInvitationBetweenUsers(Integer senderId, Integer receiverId, Integer matchId) {
        return InvitationRepository.findBySenderIdAndReceiverIdAndMatchIdAndStatus(senderId, receiverId, matchId, InvitationStatus.PENDING);
    }

    public InvitationMatch getInvitation(Integer id) {
        return InvitationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));
    }

    @Transactional
    public InvitationStatus acceptInvite(Integer invitationId) {
        InvitationMatch invitation = getInvitation(invitationId);
        
        Match match = invitation.getMatch();
        User receiver = invitation.getReceiver();
        
        checkers.checkGameIsNotPlaying(match); 
        checkers.checkNumberOfPlayers(match);

        // Verificar si el usuario ya está en la partida
        boolean alreadyInMatch = match.getPlayers().stream()
            .anyMatch(p -> p.getUser().getId().equals(receiver.getId()));

        if (alreadyInMatch) {
            // Ya estaba unido: marcar esta invitación como aceptada y limpiar el resto pero responder 200 con la notificación
            invitation.setStatus(InvitationStatus.ACCEPTED);
            rejectOtherInvitesForMatch(receiver.getId(), match.getId(), invitation.getId());
        }

        // en los demás casos aceptamos y rechazamos las demás invitaciones
        rejectOtherInvitesForMatch(receiver.getId(), match.getId(), invitation.getId());

        invitation.setStatus(InvitationStatus.ACCEPTED);

        if (invitation.isSpectator()) { // lo metemos como espectador
            matchService.spectateGame(match.getId()); 
        } else { // lo metemos como player
            lobbyService.joinLobby(match.getId()); 
        }



        InvitationRepository.save(invitation); 
        return invitation.getStatus();
    }

    public InvitationMatch rejectInvite(InvitationMatch Invitation) {
        Invitation.setStatus(InvitationStatus.REJECTED);
        return InvitationRepository.save(Invitation);
    }

    public void rejectOtherInvitesForMatch(Integer receiverId, Integer matchId, Integer acceptedInvitationId) {
        // Obtener todas las invitaciones pendientes para este receiver en esta partida
        List<InvitationMatch> pendingInvites = InvitationRepository.findByReceiverIdAndMatchIdAndStatus(
            receiverId, matchId, InvitationStatus.PENDING
        );
        
        // Rechazar todas excepto la que acabamos de aceptar
        for (InvitationMatch invite : pendingInvites) {
            if (!invite.getId().equals(acceptedInvitationId)) {
                rejectInvite(invite);
            }
        }
    }
}
