import React, { useRef } from 'react';
import './PhotoUpload.css';

function PhotoUpload({ photos, setPhotos }) {
  const fileInputRef = useRef(null);

  const handleFileChange = (e) => {
    const files = Array.from(e.target.files);
    const validFiles = files.filter(file => file.type.startsWith('image/'));
    
    if (validFiles.length + photos.length > 5) {
      alert('Maximum 5 photos allowed');
      return;
    }

    setPhotos(prev => [...prev, ...validFiles]);
  };

  const removePhoto = (index) => {
    setPhotos(prev => prev.filter((_, i) => i !== index));
  };

  const triggerFileInput = () => {
    fileInputRef.current?.click();
  };

  return (
    <div className="photo-upload-section">
      <label className="section-label">Photos (Optional)</label>
      <p className="section-description">Add up to 5 photos of the issue</p>
      
      <div className="photo-grid">
        {photos.map((photo, index) => (
          <div key={index} className="photo-preview">
            <img 
              src={URL.createObjectURL(photo)} 
              alt={`Preview ${index + 1}`}
            />
            <button
              type="button"
              className="remove-photo-btn"
              onClick={() => removePhoto(index)}
            >
              ✕
            </button>
          </div>
        ))}
        
        {photos.length < 5 && (
          <button
            type="button"
            className="add-photo-btn"
            onClick={triggerFileInput}
          >
            <span className="icon">📷</span>
            <span>Add Photo</span>
          </button>
        )}
      </div>

      <input
        ref={fileInputRef}
        type="file"
        accept="image/*"
        multiple
        onChange={handleFileChange}
        style={{ display: 'none' }}
        capture="environment"
      />
    </div>
  );
}

export default PhotoUpload;
