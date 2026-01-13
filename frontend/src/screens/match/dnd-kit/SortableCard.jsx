import { useSortable } from '@dnd-kit/sortable';
import { CSS } from '@dnd-kit/utilities';

export default function SortableCard({ card }) {
  const {
    attributes,
    listeners,
    setNodeRef,
    transform,
    transition,
    isDragging,
  } = useSortable({
    id: card.id,
  });

  const style = {
    transform: CSS.Transform.toString(transform),
    transition,
    opacity: isDragging ? 0 : 1,
    pointerEvents: isDragging ? 'none' : 'auto',
  };

  return (
    <img
      ref={setNodeRef}
      {...attributes}
      {...listeners}
      src={`/resources${card.frontImage}`}
      alt={`Carta ${card.letter}`}
      className="card"
      style={style}
    />
  );
}
