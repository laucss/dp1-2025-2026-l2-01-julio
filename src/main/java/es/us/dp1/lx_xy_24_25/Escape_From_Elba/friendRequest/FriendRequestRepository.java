package es.us.dp1.lx_xy_24_25.Escape_From_Elba.friendRequest;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
 
@Repository
public interface FriendRequestRepository extends CrudRepository<FriendRequest, Integer> {

    //Busca una solicitud de amistad concreta, identificada por requestId, solo si está en estado PENDING.
    @Query(value = "SELECT fr FROM FriendRequest fr WHERE fr.status = 'PENDING' AND fr.id = :requestId")
    Optional<FriendRequest> findRequestPendingById(@Param("requestId") Integer requestId);

    //Obtiene todas las solicitudes de amistad enviadas por un usuario, cuyo id es userId, que aún estén PENDING
    @Query(value = "SELECT fr FROM FriendRequest fr WHERE fr.status = 'PENDING' AND fr.sender.id = :userId")
    List<FriendRequest> findAllRequestsByUserId(@Param("userId") Integer userId);

    //Obtiene todas las solicitudes de amistad recibidas por un usuario, userId, que estén PENDING.
    @Query(value = "SELECT fr FROM FriendRequest fr WHERE fr.status = 'PENDING' AND fr.receiver.id = :userId")
    List<FriendRequest> findAllRequestsForUserId(@Param("userId") Integer userId);

    //Devuelve todas las solicitudes de amistad aceptadas (ACCEPTED) donde el usuario es o el sender o el receiver. (Amigos)
    @Query(value = "SELECT fr FROM FriendRequest fr WHERE fr.status = 'ACCEPTED' AND fr.sender.id = :userId OR fr.status = 'ACCEPTED' AND fr.receiver.id = :userId")
    List<FriendRequest> findAllFriendsByUserId(@Param("userId") Integer userId);

    //Busca si dos usuarios concretos (user1Id y user2Id) tienen una solicitud pendiente o ya son amigos (ACCEPTED)
    @Query(value = "SELECT fr FROM FriendRequest fr WHERE ((fr.sender.id = :user1Id AND fr.receiver.id = :user2Id) OR (fr.sender.id = :user2Id AND fr.receiver.id = :user1Id)) AND (fr.status = 'PENDING' OR fr.status = 'ACCEPTED')")
    Optional<FriendRequest> findPendingOrFriendsUsers(@Param("user1Id") Integer user1Id, @Param("user2Id") Integer user2Id);
}