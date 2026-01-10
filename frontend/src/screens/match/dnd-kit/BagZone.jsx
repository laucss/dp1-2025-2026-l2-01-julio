import { useDroppable } from '@dnd-kit/core';

export default function  BagZone({ children }) {
  const { setNodeRef, isOver } = useDroppable({
    id: 'bag',
  });

  return (
    <div
      ref={setNodeRef}
      className={`cards-grid ${isOver ? 'over' : ''}`}
    >
      {children}
    </div>
  );
}
