// Utilidad para obtener el estado de invitación de cada amigo
export async function fetchInviteStatuses(friends, matchId, jwt, currentUserId) {
  const statuses = {};
  for (const friend of friends) {
    try {
      const res = await fetch(`/api/v1/invitations?receiverId=${friend.userId}&matchId=${matchId}&senderId=${currentUserId}`, {
        headers: { Authorization: `Bearer ${jwt}` },
      });
      const data = await res.json();
      // Si hay notificación pendiente, estado 'success'. Si la última fue rechazada, estado 'none'.
      if (Array.isArray(data) && data.length > 0) {
        const lastNotif = data[data.length - 1];
        if (lastNotif.status === 'PENDING') {
          statuses[friend.id] = 'success';
        } else if (lastNotif.status === 'REJECTED') {
          statuses[friend.id] = 'none';
        } else {
          statuses[friend.id] = 'none';
        }
      } else {
        statuses[friend.id] = 'none';
      }
    } catch {
      statuses[friend.id] = 'none';
    }
  }
  return statuses;
}
