import { useDraggable } from '@dnd-kit/core';

export default function Card({ card }) {
  
  // esto lo he sacado directamente de la documentación de dnd-kit, 
  // que es la librería que estoy usando para lo de arrastrar las cartas
  
  const { attributes, listeners, setNodeRef, isDragging } = useDraggable({
    id: card.id,
  });

  const style = { 
    visibility: isDragging ? 'hidden' : 'visible',
    pointerEvents: isDragging ? 'none' : 'auto',
    opacity: isDragging ? "0 !important" : "1", // se pone el !important para que ignore el css, 
                                                // lo he puesto porque me salía unqa copia de fondo y no me gusta
  }; 

  return (
    <img
      ref={setNodeRef}
      {...listeners}
      {...attributes}
      src={`/resources${card.frontImage}`}
      alt={`Carta ${card.letter}`}
      className="card"
      style={style}
    />
  );
}
