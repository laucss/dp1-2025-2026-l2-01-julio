import { useDroppable } from '@dnd-kit/core';

export default function DiscardZone({ children }) {
  const { setNodeRef, isOver } = useDroppable({
    id: 'discard',
  });

  return (
    <div
      ref={setNodeRef}
      className={`cards-grid discard-zone ${isOver ? 'over' : ''}`}
    >
      {children}
    </div>
  );
}
