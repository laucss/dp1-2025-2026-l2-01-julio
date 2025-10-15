import React, { useState } from 'react';
import Avatar1 from '../../static/images/Avatares/Avatar1.jpg'; 
import Avatar2 from '../../static/images/Avatares/Avatar2.jpg';


const images = [
  { id: 1, src: Avatar1, alt: 'Avatar 1' },
  { id: 2, src: Avatar2, alt: 'Avatar 2' },
 
];

function SimpleImageList({ onImageSelect }) {
  const [selectedImageId, setSelectedImageId] = useState(null);

  const handleClick = (image) => {
    setSelectedImageId(image.id);
    if (onImageSelect) {
      onImageSelect(image.src); 
    }
  };

  return (
    <div style={{ display: 'flex', flexWrap: 'wrap', gap: '15px', justifyContent: 'center' }}>
      {images.map((image) => (
        <img
          key={image.id}
          src={image.src}
          alt={image.alt}
          onClick={() => handleClick(image)}
          style={{
            cursor: 'pointer',
            border: selectedImageId === image.id ? '3px solid #007bff' : '3px solid transparent', 
            borderRadius: '8px',
            width: '100px',
            height: '100px',
            objectFit: 'cover',
            transition: 'border-color 0.2s',
          }}
        />
      ))}
    </div>
  );
}

export default SimpleImageList;
