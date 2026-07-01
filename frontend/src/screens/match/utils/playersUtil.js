const COLORS = [
    '#ff5353ff',
    '#4ECDC4',
    '#ffac3fff',
    '#e5541aff',
    '#52a852ff',
    '#2a15ceff'
];

export const getPlayerColor = (players, playerId) => {
    const playerIndex = players.findIndex(p => p.id === playerId);
    return COLORS[playerIndex % COLORS.length];
};