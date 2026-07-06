import React from 'react';
import OtherPlayersPanel from './OthersPlayersSection';
import { roomPositions } from '../utils/roomPositions';
import { getPlayerColor } from '../utils/playersUtil';
import { normalizeRoomId } from '../utils/roomUtils';

const BOARD_AREAS = [
  { roomId: 37, alt: 'Safe Area', title: 'Safe Area', coords: '321,251,84', shape: 'circle' },
  { roomId: 31, alt: 'West Tower', title: 'West Tower', coords: '13,489,98,388', shape: 'rect' },
  { roomId: 36, alt: 'South Tower', title: 'South Tower', coords: '541,389,628,488', shape: 'rect' },
  { roomId: 1, alt: 'North Tower', title: 'North Tower', coords: '13,12,99,113', shape: 'rect' },
  { roomId: 6, alt: 'East Tower', title: 'East Tower', coords: '542,11,626,111', shape: 'rect' },
  { roomId: 2, alt: 'Caesar Room', title: 'Caesar Room', coords: '110,40,210,114', shape: 'rect' },
  { roomId: 3, alt: 'Opal Room', title: 'Opal Room', coords: '220,10,292,69', shape: 'rect' },
  { roomId: 4, alt: 'Coral Room', title: 'Coral Room', coords: '345,11,418,69', shape: 'rect' },
  { roomId: 5, alt: 'Roof', title: 'Roof', coords: '429,38,530,112', shape: 'rect' },
  { roomId: 8, alt: 'Cafe', title: 'Cafe', coords: '293,154,221,80', shape: 'rect' },
  { roomId: 11, alt: 'Parlor', title: 'Parlor', coords: '345,81,417,152', shape: 'rect' },
  { roomId: 16, alt: 'Pool', title: 'Pool', coords: '369,165,488,166,488,251,419,251,407,206', shape: 'poly' },
  { roomId: 15, alt: 'SPA', title: 'SPA', coords: '271,166,237,197,221,236,153,237,152,165', shape: 'poly' },
  { roomId: 21, alt: 'Arbor', title: 'Arbor', coords: '151,248,151,334,266,334,236,299,221,250', shape: 'poly' },
  { roomId: 22, alt: 'Farm', title: 'Farm', coords: '488,264,488,334,371,334,403,296,416,265', shape: 'poly' },
  { roomId: 13, alt: 'Ball Room', title: 'Ball Room', coords: '25,166,98,251', shape: 'rect' },
  { roomId: 18, alt: 'Sleep Room', title: 'Sleep Room', coords: '540,165,614,238', shape: 'rect' },
  { roomId: 19, alt: 'Class Room', title: 'Class Room', coords: '25,263,97,334', shape: 'rect' },
  { roomId: 24, alt: 'Meal Room', title: 'Meal Room', coords: '541,249,613,335', shape: 'rect' },
  { roomId: 26, alt: 'Bar', title: 'Bar', coords: '221,346,292,417', shape: 'rect' },
  { roomId: 29, alt: 'Lab', title: 'Lab', coords: '346,346,418,418', shape: 'rect' },
  { roomId: 32, alt: 'Cellar', title: 'Cellar', coords: '109,387,209,460', shape: 'rect' },
  { roomId: 33, alt: 'Apple Room', title: 'Apple Room', coords: '221,430,293,488', shape: 'rect' },
  { roomId: 35, alt: 'Parole Room', title: 'Parole Room', coords: '429,387,529,459', shape: 'rect' },
  { roomId: 34, alt: 'Map Room', title: 'Map Room', coords: '345,430,419,490', shape: 'rect' },
  { roomId: 7, alt: 'Corridor 1', title: 'Corridor 1', coords: '25,123,209,153', shape: 'rect' },
  { roomId: 9, alt: 'Corridor 2', title: 'Corridor 2', coords: '304,57,335,155', shape: 'rect' },
  { roomId: 12, alt: 'Corridor 3', title: 'Corridor 3', coords: '430,122,613,154', shape: 'rect' },
  { roomId: 14, alt: 'Corridor 4', title: 'Corridor 4', coords: '109,164,141,250', shape: 'rect' },
  { roomId: 17, alt: 'Corridor 5', title: 'Corridor 5', coords: '500,164,529,238', shape: 'rect' },
  { roomId: 20, alt: 'Corridor 6', title: 'Corridor 6', coords: '109,262,141,334', shape: 'rect' },
  { roomId: 23, alt: 'Corridor 7', title: 'Corridor 7', coords: '500,248,529,333', shape: 'rect' },
  { roomId: 25, alt: 'Corridor 8', title: 'Corridor 8', coords: '25,345,209,376', shape: 'rect' },
  { roomId: 27, alt: 'Corridor 9', title: 'Corridor 9', coords: '304,345,335,441', shape: 'rect' },
  { roomId: 30, alt: 'Corridor 10', title: 'Corridor 10', coords: '429,346,613,376', shape: 'rect' },
];

export default function MatchBoardMap({
  match,
  currentUser,
  move,
  moveNpcMode,
  selectedNpcIndex,
  selectedNpcId,
  setSelectedNpcIndex,
  setSelectedNpcId,
  isSpectator,
  playersList,
  otherPlayersBags,
  otherPlayersHands
}) {
  const renderBoardAreas = () =>
    BOARD_AREAS.map(({ roomId, alt, ...area }) => (
      <area
        key={roomId}
        className="Area"
        href="#"
        target=""
        alt={alt}
        aria-label={alt}
        onClick={(e) => {
          e.preventDefault();
          move(roomId);
        }}
        {...area}
      />
    ));

  const renderPlayerMarkers = () =>
    match?.players?.map((player) => {
      const roomId = normalizeRoomId(player.currentRoom?.id ?? player.roomId ?? player.room?.id);
      if (!roomId) return null;

      const position = roomPositions[roomId];
      if (!position) return null;

      return (
        <img
          key={player.id}
          src={player.user?.avatar || '/Avatar_default.png'}
          alt={player.user?.username || 'Player'}
          title={player.user?.username || 'Player'}
          style={{
            aspectRatio: '1 / 1',
            position: 'absolute',
            left: `${position.x}px`,
            top: `${position.y}px`,
            transform: 'translate(-50%, -50%)',
            width: '30px',
            height: '30px',
            borderRadius: '50%',
            border: `3px solid ${getPlayerColor(match?.players || [], player.id)}`,
            boxShadow: '0 2px 4px rgba(0,0,0,0.5)',
            zIndex: 10,
            pointerEvents: 'none',
            transition: 'left 0.5s ease, top 0.5s ease',
          }}
        />
      );
    });

  const renderNpcMarkers = () =>
    match?.npcs?.map((npc, index) => {
      const roomId = normalizeRoomId(npc.room?.id);
      if (!roomId) return null;

      const position = roomPositions[roomId];
      if (!position) return null;
      const isSelectable = moveNpcMode && !isSpectator && match?.currentTurnUserId === currentUser?.id;
      const isSelected = selectedNpcIndex === index || (selectedNpcId != null && selectedNpcId === npc.id);

      return (
        <div
          key={`npc-${index}`}
          title={npc.name || `NPC ${index + 1}`}
          onClick={(e) => {
            if (!isSelectable) return;
            e.stopPropagation();
            setSelectedNpcIndex(index);
            setSelectedNpcId(npc.id ?? null);
          }}
          style={{
            position: 'absolute',
            left: `${position.x}px`,
            top: `${position.y}px`,
            transform: 'translate(-50%, -50%)',
            width: '25px',
            height: '25px',
            borderRadius: '50%',
            backgroundColor: npc.isNiallCampbell ? '#ff0000' : '#666',
            border: isSelected ? '3px solid yellow' : '2px solid white',
            boxShadow: '0 2px 4px rgba(0,0,0,0.5)',
            zIndex: 20,
            pointerEvents: isSelectable ? 'auto' : 'none',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: 'white',
            fontSize: '10px',
            fontWeight: 'bold',
            cursor: isSelectable ? 'pointer' : 'default',
          }}
        >
          {npc.isNiallCampbell ? 'N' : 'X'}
        </div>
      );
    });

  // console.log(otherPlayersHands)
  return (
    <div className="map-container">
      <div className="board-wrapper">
        <map name="Map">{renderBoardAreas()}</map>
        <img src="/ElbaBoard.png" useMap="#Map" className="Map" alt="Elba board" />
        {renderPlayerMarkers()}
        {renderNpcMarkers()}
      </div>

      <div style={{ position: 'absolute', right: 20, top: 20, width: '450px', maxHeight: '600px', zIndex: 15 }}>
        <OtherPlayersPanel
          playersList={playersList}
          otherPlayersHands={otherPlayersHands}
          otherPlayersBags={otherPlayersBags}
          getPlayerColor={getPlayerColor}
          players={match?.players}
          npcs={match.npcs}
        />
      </div>
    </div>
  );
}
