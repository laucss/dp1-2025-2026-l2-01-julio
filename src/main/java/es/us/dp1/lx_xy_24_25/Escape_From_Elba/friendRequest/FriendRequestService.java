package es.us.dp1.lx_xy_24_25.Escape_From_Elba.friendRequest;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.AlreadyCreatedException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchStatus;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class FriendRequestService {

    FriendRequestRepository friendRequestRepository;
    UserRepository userRepository;
    FriendWebsocketController friendWebsocketController;
    MatchRepository matchRepository; 

    @Autowired
    public FriendRequestService(FriendRequestRepository friendRequestRepository, UserRepository userRepository, 
        FriendWebsocketController friendWebsocketController,  MatchRepository matchRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
        this.friendWebsocketController = friendWebsocketController;
        this.matchRepository = matchRepository; 
    }

    @Transactional(readOnly = true)
    public FriendRequest findById(Integer FriendRequestId) throws ResourceNotFoundException {
        return friendRequestRepository.findById(FriendRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("FriendRequest", "id", FriendRequestId));
    }

    @Transactional(readOnly = true)
    public FriendRequest findFriendRequestPendingById(Integer FriendRequestId) throws ResourceNotFoundException {
        return friendRequestRepository.findRequestPendingById(FriendRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("FriendRequest", "id", FriendRequestId));
    }

    @Transactional(readOnly = true)
    public List<FriendRequest> findFriendRequestsByUserId(Integer userId) {
        return friendRequestRepository.findAllRequestsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<FriendRequest> findFriendRequestsForUserId(Integer userId) {
        return friendRequestRepository.findAllRequestsForUserId(userId);
    }

    @Transactional(readOnly = true)
    public Boolean isPendingOrFriendsUsers(Integer user1Id, Integer user2Id) {
        return friendRequestRepository.findPendingOrFriendsUsers(user1Id, user2Id).isPresent();
    }

    @Transactional(readOnly = true)
    public List<User> findFriendsByUserId(Integer userId) {
        return friendRequestRepository.findAllFriendsByUserId(userId).stream()
                .map(f -> {
                    User receiver = f.getReceiver();
                    if (receiver.getId() == userId) {
                        return f.getSender();
                    }
                    return receiver;
                }).toList();
    }

    // TODO: mirar si quitamos esta funcion pq es ridicula se repite vaya
    @Transactional(readOnly = true)
    public List<FriendRequest> findAcceptedFriendRequestsByUserId(Integer userId) {
        return friendRequestRepository.findAllFriendsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public User findPlayerById(Integer playerId) throws ResourceNotFoundException {
        return userRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player", "id", playerId));
    }

    @Transactional(rollbackFor = AlreadyCreatedException.class)
    public FriendRequest sendRequest(Integer senderId, Integer receiverId) throws AlreadyCreatedException {
        if (isPendingOrFriendsUsers(senderId, receiverId)) {
            log.error("Friend request already exists: senderId={}, receiverId={}", senderId, receiverId);
            throw new AlreadyCreatedException("Friend request");
        }

        log.info("Creating a new friend request: senderId={}, receiverId={}", senderId, receiverId);

        FriendRequest newFriendRequest = new FriendRequest();
        User sender = findPlayerById(senderId);
        newFriendRequest.setSender(sender);
        User receiver = findPlayerById(receiverId);
        newFriendRequest.setReceiver(receiver);
        newFriendRequest.setStatus(StatusType.PENDING);
        FriendRequest savedFriendRequest = friendRequestRepository.save(newFriendRequest);

        log.info("Friend request created successfully: requestId={}, senderId={}, receiverId={}", savedFriendRequest.getId(), senderId, receiverId);
        
        // Notificar al receptor sobre la nueva solicitud de amistad
        friendWebsocketController.notifyNewFriendRequest(receiverId, savedFriendRequest);
        
        return newFriendRequest;
    }

    @Transactional
    public FriendRequest acceptRequest(FriendRequest toUpdate) {
        log.info("Accepting friend request: requestId={}", toUpdate.getId());
        toUpdate.setStatus(StatusType.ACCEPTED);
        friendRequestRepository.save(toUpdate);
        log.info("Friend request accepted: requestId={}", toUpdate.getId());
        
        // Notificar al remitente que su solicitud fue aceptada
        friendWebsocketController.notifyRequestAccepted(toUpdate.getSender().getId(), toUpdate);
        // Notificar al receptor de la actualización
        friendWebsocketController.notifyFriendRequestUpdate(toUpdate.getReceiver().getId(), toUpdate);
        
        return toUpdate;
    }

    @Transactional
    public FriendRequest rejectRequest(FriendRequest toUpdate) {
        log.info("Rejecting friend request: requestId={}", toUpdate.getId());
        toUpdate.setStatus(StatusType.REJECTED);
        friendRequestRepository.save(toUpdate);
        log.info("Friend request rejected: requestId={}", toUpdate.getId());
        
        // Notificar al remitente que su solicitud fue rechazada
        friendWebsocketController.notifyRequestRejected(toUpdate.getSender().getId(), toUpdate);
        // Notificar al receptor de la actualización
        friendWebsocketController.notifyFriendRequestUpdate(toUpdate.getReceiver().getId(), toUpdate);
        
        return toUpdate;
    }

    @Transactional
    public void deleteFriend(FriendRequest toDelete) {
        Integer user1Id = toDelete.getSender().getId();
        Integer user2Id = toDelete.getReceiver().getId();
        Integer friendRequestId = toDelete.getId();
        
        friendRequestRepository.deleteById(friendRequestId);
        
        // Notificar a ambos usuarios que la amistad fue eliminada
        friendWebsocketController.notifyFriendRequestDeleted(user1Id, user2Id, friendRequestId);
    }

    @Transactional
    public List<FriendsInvitationDTO> getFriendsByUserIdToInvite(Integer userId, Integer matchId) {

        Match match = matchRepository.findById(matchId)
            .orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        List<User> userFriends = findFriendsByUserId(userId);
        List<User> players = match.getPlayers().stream().map(Player::getUser).toList();

        List<Integer> playerIds = players.stream().map(User::getId).toList();

        EnumSet<MatchStatus> activeStatuses = EnumSet.of(
            MatchStatus.WAITING,
            MatchStatus.PLAYING,
            MatchStatus.VOTING
        );

        List<FriendsInvitationDTO> result = new ArrayList<>();

        for (User friend : userFriends) {
            boolean isPlaying = friend.getPlayers().stream().map(Player::getMatch).map(Match::getStatus).anyMatch(activeStatuses::contains);
            boolean isSpectating = friend.getSpectatingMatches().stream().map(Match::getStatus).anyMatch(activeStatuses::contains);

            boolean isInLobby = isPlaying || isSpectating;

            if (match.getIsPrivate()) {
                boolean isFriendOfAllPlayers = friendRequestRepository.countFriendsAmongPlayers(friend.getId(), playerIds) == playerIds.size();
                result.add(new FriendsInvitationDTO(friend, isFriendOfAllPlayers, isInLobby));
            } else {
                result.add(new FriendsInvitationDTO(friend, isInLobby));
            }
        }

        return result;
    }
    
}