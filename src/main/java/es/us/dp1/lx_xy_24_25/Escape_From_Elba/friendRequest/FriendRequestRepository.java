package es.us.dp1.lx_xy_24_25.Escape_From_Elba.friendRequest;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
 
@Repository
public interface FriendRequestRepository extends CrudRepository<FriendRequest, Integer> {

    @Query(value = "SELECT fr FROM FriendRequest fr WHERE fr.status = 'PENDING' AND fr.id = :requestId")
    Optional<FriendRequest> findRequestPendingById(@Param("requestId") Integer requestId);

    @Query(value = "SELECT fr FROM FriendRequest fr WHERE fr.status = 'PENDING' AND fr.sender.id = :userId")
    List<FriendRequest> findAllRequestsByUserId(@Param("userId") Integer userId);

    @Query(value = "SELECT fr FROM FriendRequest fr WHERE fr.status = 'PENDING' AND fr.receiver.id = :userId")
    List<FriendRequest> findAllRequestsForUserId(@Param("userId") Integer userId);

    @Query(value = "SELECT fr FROM FriendRequest fr WHERE fr.status = 'ACCEPTED' AND fr.sender.id = :userId OR fr.status = 'ACCEPTED' AND fr.receiver.id = :userId")
    List<FriendRequest> findAllFriendsByUserId(@Param("userId") Integer userId);

    @Query(value = "SELECT fr FROM FriendRequest fr WHERE ((fr.sender.id = :user1Id AND fr.receiver.id = :user2Id) OR (fr.sender.id = :user2Id AND fr.receiver.id = :user1Id)) AND (fr.status = 'PENDING' OR fr.status = 'ACCEPTED')")
    Optional<FriendRequest> findPendingOrFriendsUsers(@Param("user1Id") Integer user1Id, @Param("user2Id") Integer user2Id);
}