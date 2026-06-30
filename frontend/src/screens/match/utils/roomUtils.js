    // Agrupa los corredores duplicados (9/10 y 27/28) para tratarlos como la misma habitación
    export const normalizeRoomId = (roomId) => {
        if (roomId === 10) return 9;
        if (roomId === 28) return 27;
        return roomId;
    };

    export const areRoomsAdjacent = (adjacencies, fromId, toId) => {
    if (!fromId || !toId) return false;
    if (!adjacencies || typeof adjacencies !== "object" || Array.isArray(adjacencies))
        return false;

    const neighbors =
        adjacencies[fromId] ||
        adjacencies[fromId.toString()] ||
        [];

    return neighbors.includes(toId);
};