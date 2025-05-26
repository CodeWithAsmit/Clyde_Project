import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useLocation } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../CSS/ClydeScreens.css'
import { useTheme } from '../Utility/ThemeContext';

const ClydeMenuScreen: React.FC = () => {

  const location = useLocation();
  const navigate = useNavigate();

  const [inputWord, setInputWord] = useState<string>('');
  const [warningMessage, setWarningMessage] = useState<string>('');

  const activeLocations = location.state?.activeLocations || [];  
  const [count, setCount] = useState<number>(location.state?.count || 0);
  const [wordBuffer, setWordBuffer] = useState<string[]>(location.state?.wordBuffer || []);
  const [showBuffer, setShowBuffer] = useState<boolean>(location.state?.wordBuffer?.length > 0);
  
  const { isDarkMode, toggleTheme } = useTheme();

  const sendWordToBackend = async (action: 'add' | 'restrict') => {
    
    if (!inputWord.trim())
    {
      setWarningMessage(`Please enter a word before ${action === 'add' ? 'adding' : 'restricting'}.`);
      return;
    }
    if (action === 'restrict' && wordBuffer.length === 0)
    {
      setWarningMessage('The word buffer is empty. Add a word first.');
      return;
    }

    setWarningMessage('');
    
    const currentBuffer = action === 'add' ? [] : wordBuffer;

    try
    {
      const response = await fetch('http://localhost:8080/get_count_for_words', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ word: inputWord, buffer: currentBuffer, locations: activeLocations })
      });
      const data = await response.json();
      setCount(data.count);
      setWordBuffer([...currentBuffer, inputWord]);
      setInputWord('');
      setShowBuffer(true);
    }
    catch (error)
    {
      console.error(`Error during ${action}:`, error);
      setWarningMessage(`An error occurred while ${action === 'add' ? 'adding' : 'restricting'} the word.`);
    }
  };

  const displayTheList = async () => {

    if (wordBuffer.length === 0)
    {
      setWarningMessage('The word buffer is empty. Add at least one word before displaying the list.');
      return;
    }
  
    if (activeLocations.length === 0)
    {
      setWarningMessage('No locations selected. Please select at least one location before displaying the list.');
      return;
    }
    
    try
    {
      const response = await fetch('http://localhost:8080/display_records', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ buffer: wordBuffer, locations: activeLocations })
      });

      const data = await response.json();
      
      if (Array.isArray(data))
      {
        navigate('/display-details', {state: {displayList: data, activeLocations, wordBuffer, count}});
      }
      else
      {
        console.error('Invalid data format:', data);
        setWarningMessage('Failed to display the list. Invalid data format received.');
      }
    }
    catch (error)
    {
      console.error('Error displaying list:', error);
      setWarningMessage('An error occurred while displaying the list.');
    }
  };

  return (
  
    <div className={`container-fluid min-vh-75 d-flex flex-column justify-content-start align-items-center py-5 px-3 fade-slide-in
      ${isDarkMode ? 'bg-dark text-success' : 'bg-light text-dark'}`}
      style={{ fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif" }}>
  
    <button
      onClick={toggleTheme}
       className={`btn btn-${isDarkMode ? 'light' : 'dark'} rounded-circle position-fixed top-0 end-0 m-4 shadow-lg`}
      style={{
        width: 38,
        height: 38,
        fontSize: 18,
        lineHeight: 1,
        userSelect: 'none',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        padding: 0,
      }}
      aria-label="Toggle theme"
      title="Toggle light/dark theme">
      {isDarkMode ? '☀️' : '🌙'}
    </button>

      <div className="w-100 text-center mb-4">
        <h1 className="display-5 fw-bold">🌐 SYS5 2M1 V2.1 - Clyde Menu</h1>
        <hr className="border border-success border-2 opacity-100 w-100 mx-auto" />
      </div>

      <div className="card bg-gradient bg-opacity-10 border border-success rounded-4 shadow-lg w-100 mb-4 fade-slide-in" style={{ maxWidth: '900px' }}>
        <div className="card-body">
          <div className="mb-4">
            <input
              type="text"
              className="form-control bg-dark text-success border-success shadow-sm rounded-pill px-4 py-2"
              value={inputWord}
              onChange={(e) => setInputWord(e.target.value)}
              placeholder="🔤 Enter a keyword to add or restrict"
            />
          </div>

          {warningMessage && (
            <div className="alert alert-warning text-center fw-semibold rounded-pill">
              ⚠️ {warningMessage}
            </div>
          )}

          <div className="row text-center g-3">
            <div className="col-md-3">
              <button className="btn btn-success w-100 py-2 rounded-3 fw-bold" onClick={() => sendWordToBackend('add')}>
                Add Word
              </button>
            </div>
            <div className="col-md-3">
              <button className="btn btn-outline-warning w-100 py-2 rounded-3 fw-bold" onClick={() => sendWordToBackend('restrict')}>
                Restrict Word
              </button>
            </div>
            <div className="col-md-3">
              <button className="btn btn-info w-100 py-2 rounded-3 fw-bold text-dark" onClick={displayTheList}>
                Display List
              </button>
            </div>
            <div className="col-md-3">
              <button className="btn btn-secondary w-100 py-2 rounded-3 fw-bold" onClick={() => navigate('/clyde-table')}>
                Change Filter
              </button>
            </div>
          </div>
        </div>
      </div>

      <div className="card bg-white border-success text-dark shadow-sm rounded-4 w-100 mb-4 fade-slide-in" style={{ maxWidth: '900px' }}>
        <div className="card-body">
          <p className="mb-1">Total records : <span className="fw-bold">{count}</span></p>
          {showBuffer && (<p className="mb-0">Current word buffer : <strong>{wordBuffer.join(', ')}</strong></p>)}
        </div>
      </div>
    </div>
  );
};

export default ClydeMenuScreen;