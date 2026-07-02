import React from 'react';
import { useState, useEffect, useRef } from "react";
import '../../App.css';
import '../../static/css/home/home.css';
import titleImage from '../../static/images/title.png';
import { useNavigate } from 'react-router-dom';
import tokenService from "../../services/token.service";
import useFetchState from '../../util/useFetchState';
import { toast } from "react-toastify";

const jwt = tokenService.getLocalAccessToken();

export default function Home(){
    const navigate = useNavigate();
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const user = tokenService.getUser();
    const notifiedInvitationIdsRef = useRef(new Set());
    const notifiedAcceptedFriendsRef = useRef(new Set());
    const notifiedFriendRequestsRef = useRef(new Set());
    const friendStatusMapRef = useRef(new Map());
    const friendStatusInitRef = useRef(false);
    const pollingRef = useRef(null);
    const friendPollingRef = useRef(null);
    const friendRequestPollingRef = useRef(null);
    const friendStatusPollingRef = useRef(null);

    // Cargar set de aceptaciones ya notificadas para evitar repetir al volver a Home
    useEffect(() => {
        const stored = sessionStorage.getItem('notifiedAcceptedFriendIds');
        if (stored) {
            try {
                const arr = JSON.parse(stored);
                if (Array.isArray(arr)) {
                    notifiedAcceptedFriendsRef.current = new Set(arr);
                }
            } catch (e) {
                console.warn('Could not parse notifiedAcceptedFriendIds');
            }
        }
    }, []);
    const currentMatch = useFetchState(
        [],
        user && user.id ? `/api/v1/matches/user/${user.id}/in` : null,
        jwt,
        setMessage,
        setVisible
    )[0];

    // Polling para invitaciones pendientes
    useEffect(() => {
        if (!user || !user.id || !jwt) return;

        const fetchInvitations = async () => {
            try {
                const res = await fetch(`/api/v1/notifications?receiverId=${user.id}`, {
                    headers: { Authorization: `Bearer ${jwt}` },
                });
                if (!res.ok) throw new Error("Failed to fetch invitations");
                
                const data = await res.json();
                const pendingNotifications = Array.isArray(data) 
                    ? data.filter(notif => notif.status === 'PENDING') 
                    : [];
                
                // Encontrar invitaciones nuevas que no hemos notificado aún
                pendingNotifications.forEach(invitation => {
                    if (!notifiedInvitationIdsRef.current.has(invitation.id)) {
                        const senderName = invitation.sender?.username || 'A player';
                        toast.info(`${senderName} has invited you to a match!`, {
                            position: "top-center",
                            autoClose: 5000,
                            hideProgressBar: false,
                            closeOnClick: true,
                            pauseOnHover: true,
                            draggable: true,
                        });
                        // Marcar como notificada
                        notifiedInvitationIdsRef.current.add(invitation.id);
                    }
                });
            } catch (error) {
                console.error("Error fetching invitations:", error);
            }
        };

        // Fetch inicial inmediato
        fetchInvitations();
        
        // Polling cada 3 segundos
        pollingRef.current = setInterval(fetchInvitations, 3000);

        return () => {
            if (pollingRef.current) clearInterval(pollingRef.current);
        };
    }, [user?.id, jwt]);

    // Polling para amistades aceptadas
    useEffect(() => {
        if (!user || !user.id || !jwt) return;

        const fetchSentRequests = async () => {
            try {
                // Solo traer amistades aceptadas
                const acceptedRequests = await fetch(`/api/v1/friendRequests/${user.id}`, {
                    headers: { Authorization: `Bearer ${jwt}` },
                });
                
                if (acceptedRequests.ok) {
                    const friendsData = await acceptedRequests.json();
                    const friends = Array.isArray(friendsData) ? friendsData : [];
                    
                    // Buscar solicitudes aceptadas que no hemos notificado aún
                    friends.forEach(friendRequest => {
                        const requestId = friendRequest.id;
                        // Verificar que el usuario actual es quien envió la solicitud
                        const isSender = friendRequest.sender?.id === user.id;
                        
                        if (isSender && !notifiedAcceptedFriendsRef.current.has(requestId)) {
                            // Obtener el nombre del usuario que aceptó (el receiver de la solicitud)
                            const acceptedUsername = friendRequest.receiver?.username || 'A user';
                            toast.success(`${acceptedUsername} has accepted your friend request!`, {
                                position: "top-center",
                                autoClose: 5000,
                                hideProgressBar: false,
                                closeOnClick: true,
                                pauseOnHover: true,
                                draggable: true,
                            });
                            // Marcar como notificada
                            notifiedAcceptedFriendsRef.current.add(requestId);
                            sessionStorage.setItem(
                                'notifiedAcceptedFriendIds',
                                JSON.stringify(Array.from(notifiedAcceptedFriendsRef.current))
                            );
                        }
                    });
                }
            } catch (error) {
                console.error("Error fetching friend requests:", error);
            }
        };

        // Fetch inicial inmediato
        fetchSentRequests();
        
        // Polling cada 3 segundos
        friendPollingRef.current = setInterval(fetchSentRequests, 3000);

        return () => {
            if (friendPollingRef.current) clearInterval(friendPollingRef.current);
        };
    }, [user?.id, jwt]);

    // Polling para solicitudes de amistad recibidas
    useEffect(() => {
        if (!user || !user.id || !jwt) return;

        const fetchReceivedRequests = async () => {
            try {
                const res = await fetch(`/api/v1/friendRequests/${user.id}/received`, {
                    headers: { Authorization: `Bearer ${jwt}` },
                });
                
                if (res.ok) {
                    const requestsData = await res.json();
                    const requests = Array.isArray(requestsData) ? requestsData : [];
                    
                    // Buscar solicitudes nuevas que no hemos notificado aún
                    requests.forEach(request => {
                        const requestId = request.id;
                        if (!notifiedFriendRequestsRef.current.has(requestId)) {
                            // Obtener el nombre del usuario que envió la solicitud
                            const senderUsername = request.sender?.username || 'A user';
                            toast.info(`${senderUsername} wants to be your friend!`, {
                                position: "top-center",
                                autoClose: 5000,
                                hideProgressBar: false,
                                closeOnClick: true,
                                pauseOnHover: true,
                                draggable: true,
                            });
                            // Marcar como notificada
                            notifiedFriendRequestsRef.current.add(requestId);
                        }
                    });
                }
            } catch (error) {
                console.error("Error fetching friend requests received:", error);
            }
        };

        // Fetch inicial inmediato
        fetchReceivedRequests();
        
        // Polling cada 3 segundos
        friendRequestPollingRef.current = setInterval(fetchReceivedRequests, 3000);

        return () => {
            if (friendRequestPollingRef.current) clearInterval(friendRequestPollingRef.current);
        };
    }, [user?.id, jwt]);

    // Polling para cambios de estado ONLINE de amigos
    useEffect(() => {
        if (!user || !user.id || !jwt) return;

        const fetchFriendsStatus = async () => {
            try {
                const res = await fetch(`/api/v1/friendRequests/${user.id}`, {
                    headers: { Authorization: `Bearer ${jwt}` },
                });

                if (!res.ok) return;

                const friendsData = await res.json();
                const friends = Array.isArray(friendsData) ? friendsData : [];

                friends.forEach(friendRequest => {
                    // Determinar el usuario amigo (el otro extremo de la relación)
                    const friendUser = friendRequest.sender?.id === user.id
                        ? friendRequest.receiver
                        : friendRequest.sender;

                    if (!friendUser || !friendUser.id) return;

                    const prevStatus = friendStatusMapRef.current.get(friendUser.id);
                    const newStatus = friendUser.status;

                    // Sólo notificar transiciones a ONLINE una vez pasado el primer fetch
                    if (friendStatusInitRef.current && newStatus === 'ONLINE' && prevStatus !== 'ONLINE') {
                        const friendName = friendUser.username || 'A user';
                        toast.info(`${friendName} is online!`, {
                            position: "top-center",
                            autoClose: 5000,
                            hideProgressBar: false,
                            closeOnClick: true,
                            pauseOnHover: true,
                            draggable: true,
                        });
                    }

                    friendStatusMapRef.current.set(friendUser.id, newStatus);
                });

                // Marcar que ya hicimos la primera carga para evitar spam inicial
                friendStatusInitRef.current = true;
            } catch (error) {
                console.error("Error fetching friends status:", error);
            }
        };

        // Fetch inicial
        fetchFriendsStatus();

        // Polling cada 5s
        friendStatusPollingRef.current = setInterval(fetchFriendsStatus, 5000);

        return () => {
            if (friendStatusPollingRef.current) clearInterval(friendStatusPollingRef.current);
        };
    }, [user?.id, jwt]);
    const returnToMatch = async () => {
        if (!currentMatch) return;
        try {
            const res = await fetch(`/api/v1/matches/${currentMatch}`, {
                headers: jwt ? { 'Authorization': `Bearer ${jwt}` } : {}
            });
            if (!res.ok) {
                navigate(`/match/${currentMatch}`);
                return;
            }
            const m = await res.json();
            const started = m && (m.startTime != null || m.start_time != null);
            if (started) navigate(`/match/${currentMatch}`);
            else navigate(`/lobby/${currentMatch}`);
        } catch (e) {
            navigate(`/match/${currentMatch}`);
        }
    };

    return(
        <div className="home-page-container">
            <img src={titleImage} alt="Título" className="home-title-image" />
            {jwt ? (
                <div className='home-buttons'>
                {((Array.isArray(currentMatch) && currentMatch.length>0) || (typeof currentMatch === 'number' && currentMatch>0) || (typeof currentMatch === 'string' && currentMatch.length>0)) ? (
                <button className='return-button' onClick={returnToMatch}>
                    Return to Match
                </button>
                ) : (
                <>      
                    <button className='create-button' onClick={() => navigate('/matchs/new')}>
                        Create Match  
                    </button> 
                    <button className='join-button'onClick={() => navigate('/lobbies')}>
                        Join Match  
                    </button>
                </>
                )}
            </div>   
            ) : (
            <div className='home-buttons'>
                <button className='create-button' onClick={() => navigate('/login')}>
                    Login  
                </button> 
                <button className='join-button'onClick={() => navigate('/register')}>
                    Register  
                </button>
            </div>            
            )}
        </div>

    );
}


