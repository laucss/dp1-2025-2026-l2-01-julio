import React, { useState } from 'react';
import '../../static/css/Profile/ImageList.css';


const images = [
  { id: 1, src: "/Avatar1.jpg", alt: 'Avatar 1' },
  { id: 2, src: "/Avatar2.jpg", alt: 'Avatar 2' },
  { id: 3, src: "/Avatar_default.png", alt: "Avatar por defecto" },
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
        className='selectAvatar'
          key={image.id}
          src={image.src}
          alt={image.alt}
          onClick={() => handleClick(image)}
        />
      ))}
    </div>
  );
}

export default SimpleImageList;
