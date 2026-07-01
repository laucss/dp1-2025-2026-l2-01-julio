import { normalizeRoomId } from "./roomUtils";

export const ALL_ROOM_IDS = [
    1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
    11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
    21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
    31, 32, 33, 34, 35, 36, 37
];

export function getOccupiedRooms(match) {
    const occupied = new Set();

    (match?.players || []).forEach(player => {
        const roomId =
            player.currentRoom?.id ||
            player.roomId ||
            player.room?.id;

        const normalized = normalizeRoomId(roomId);

        if (normalized) {
            occupied.add(normalized);
        }
    });

    (match?.npcs || []).forEach(npc => {
        const normalized = normalizeRoomId(npc.room?.id);

        if (normalized) {
            occupied.add(normalized);
        }
    });

    return occupied;
}

export function getRandomFreeRoom(match, excludedRoom) {

    const occupied = getOccupiedRooms(match);

    const normalizedExcluded = normalizeRoomId(excludedRoom);

    const candidates = ALL_ROOM_IDS.filter(room =>
        room !== normalizedExcluded &&
        !occupied.has(room)
    );

    if (candidates.length > 0) {
        return candidates[
            Math.floor(Math.random() * candidates.length)
        ];
    }

    const fallback = ALL_ROOM_IDS.filter(
        room => room !== normalizedExcluded
    );

    return fallback[
        Math.floor(Math.random() * fallback.length)
    ];
}