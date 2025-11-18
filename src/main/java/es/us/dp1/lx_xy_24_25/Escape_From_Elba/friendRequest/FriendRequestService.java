package es.us.dp1.lx_xy_24_25.Escape_From_Elba.friendRequest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.AlreadyCreatedException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class FriendRequestService {

    FriendRequestRepository friendRequestRepository;
    UserRepository userRepository;

    @Autowired
    public FriendRequestService(FriendRequestRepository friendRequestRepository, UserRepository userRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
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
    public List<FriendRequest> findFriendsByUserId(Integer userId) {
        return friendRequestRepository.findAllFriendsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Boolean isPendingOrFriendsUsers(Integer user1Id, Integer user2Id) {
        return friendRequestRepository.findPendingOrFriendsUsers(user1Id, user2Id).isPresent();
    }

    @Transactional(readOnly = true)
    public List<User> findFriendsByPlayerId(Integer userId) {
        return friendRequestRepository.findAllFriendsByUserId(userId).stream()
                .map(f -> {
                    User receiver = f.getReceiver();
                    if (receiver.getId() == userId) {
                        return f.getSender();
                    }
                    return receiver;
                }).toList();
    }

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
        return newFriendRequest;
    }

    @Transactional
    public FriendRequest acceptRequest(FriendRequest toUpdate) {
        log.info("Accepting friend request: requestId={}", toUpdate.getId());
        toUpdate.setStatus(StatusType.ACCEPTED);
        friendRequestRepository.save(toUpdate);
        log.info("Friend request accepted: requestId={}", toUpdate.getId());
        return toUpdate;
    }

    @Transactional
    public FriendRequest rejectRequest(FriendRequest toUpdate) {
        log.info("Rejecting friend request: requestId={}", toUpdate.getId());
        toUpdate.setStatus(StatusType.REJECTED);
        friendRequestRepository.save(toUpdate);
        log.info("Friend request rejected: requestId={}", toUpdate.getId());
        return toUpdate;
    }

    @Transactional
    public void deleteFriend(FriendRequest toDelete) {
        friendRequestRepository.deleteById(toDelete.getId());
    }
}