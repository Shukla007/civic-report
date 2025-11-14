import React, { useState, useRef } from 'react';
import './VoiceRecorder.css';

function VoiceRecorder({ voiceNote, setVoiceNote }) {
  const [isRecording, setIsRecording] = useState(false);
  const [recordingTime, setRecordingTime] = useState(0);
  const mediaRecorderRef = useRef(null);
  const chunksRef = useRef([]);
  const timerRef = useRef(null);

  const startRecording = async () => {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
      mediaRecorderRef.current = new MediaRecorder(stream);
      chunksRef.current = [];

      mediaRecorderRef.current.ondataavailable = (e) => {
        if (e.data.size > 0) {
          chunksRef.current.push(e.data);
        }
      };

      mediaRecorderRef.current.onstop = () => {
        const blob = new Blob(chunksRef.current, { type: 'audio/webm' });
        const file = new File([blob], `voice-note-${Date.now()}.webm`, { type: 'audio/webm' });
        setVoiceNote(file);
        
        stream.getTracks().forEach(track => track.stop());
      };

      mediaRecorderRef.current.start();
      setIsRecording(true);
      setRecordingTime(0);

      timerRef.current = setInterval(() => {
        setRecordingTime(prev => prev + 1);
      }, 1000);
    } catch (err) {
      console.error('Error accessing microphone:', err);
      alert('Could not access microphone. Please check permissions.');
    }
  };

  const stopRecording = () => {
    if (mediaRecorderRef.current && isRecording) {
      mediaRecorderRef.current.stop();
      setIsRecording(false);
      clearInterval(timerRef.current);
    }
  };

  const deleteVoiceNote = () => {
    setVoiceNote(null);
    setRecordingTime(0);
  };

  const formatTime = (seconds) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs.toString().padStart(2, '0')}`;
  };

  return (
    <div className="voice-recorder-section">
      <label className="section-label">Voice Note (Optional)</label>
      <p className="section-description">Record a voice description of the issue</p>
      
      <div className="voice-recorder-container card">
        {!voiceNote ? (
          <div className="recorder-controls">
            {!isRecording ? (
              <button
                type="button"
                className="btn btn-outline record-btn"
                onClick={startRecording}
              >
                <span className="icon">🎤</span>
                <span>Start Recording</span>
              </button>
            ) : (
              <div className="recording-active">
                <div className="recording-indicator">
                  <span className="pulse"></span>
                  <span className="recording-text">Recording...</span>
                </div>
                <div className="recording-time">{formatTime(recordingTime)}</div>
                <button
                  type="button"
                  className="btn btn-danger"
                  onClick={stopRecording}
                >
                  Stop Recording
                </button>
              </div>
            )}
          </div>
        ) : (
          <div className="voice-note-preview">
            <div className="voice-note-info">
              <span className="icon">🎵</span>
              <span>Voice note recorded</span>
            </div>
            <button
              type="button"
              className="btn btn-danger btn-sm"
              onClick={deleteVoiceNote}
            >
              Delete
            </button>
          </div>
        )}
      </div>
    </div>
  );
}

export default VoiceRecorder;
