import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTheme } from '../Utility/ThemeContext';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../CSS/ClydeScreens.css';

interface Location {
  name: string;
  number: number | null;
}

interface LocationWithStatus extends Location {
  status: 'ON' | 'OFF';
}

const ClydeTableScreen: React.FC = () => {
  const navigate = useNavigate();
  const { isDarkMode, toggleTheme } = useTheme();

  const sampleLocations: Location[] = [
    { name: 'OHIO-ARS/ACIS-ACCOUNTS', number: null },
    { name: 'WISC-ARS/ACIS-ACCOUNTS', number: null },
    { name: 'INDI-ARS/ACIS-ACCOUNTS', number: null },
    { name: 'ILLI-ARS/ACIS-ACCOUNTS', number: null },
    { name: 'MICH-ARS/ACIS-ACCOUNTS', number: null },
    { name: 'ACI-LD-ACCOUNTS', number: null },
    { name: 'CABS-ACCOUNTS', number: null },
    { name: 'MLNM-LSB-ILS-RBS', number: null },
  ];

  const [locations, setLocations] = useState<LocationWithStatus[]>(
    sampleLocations.map((loc) => ({ ...loc, status: 'OFF' }))
  );

  const [activeLocations, setActiveLocations] = useState<string[]>([]);

  const toggleStatus = (name: string) => {
    setLocations((prev) =>
      prev.map((loc) =>
        loc.name === name ? { ...loc, status: loc.status === 'ON' ? 'OFF' : 'ON' } : loc
      )
    );

    setActiveLocations((prev) =>
      prev.includes(name) ? prev.filter((loc) => loc !== name) : [...prev, name]
    );
  };

  const handleSubmit = () => {
    navigate('/clyde-menu', { state: { activeLocations } });
  };

  return (
    <div
      className={`container-fluid min-vh-75 d-flex flex-column align-items-center py-4 px-3
        ${isDarkMode ? 'bg-dark text-success' : 'bg-light text-dark'}`}
      style={{ fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif" }}
    >
      {/* Theme toggle button */}
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
        title="Toggle light/dark theme"
      >
        {isDarkMode ? '☀️' : '🌙'}
      </button>

      {/* Header */}
      <div className="w-100 text-center mb-3">
        <h1 className="display-5 fw-bold">🌐 SYS5 2M1 V2.1 - Clyde Table Screen</h1>
        <hr
          className={`border border-success border-2 opacity-100 mx-auto`}
          style={{ maxWidth: 900 }}
        />
      </div>

      {/* Locations Grid */}
      <div className="row row-cols-1 row-cols-md-2 g-3 w-100" style={{ maxWidth: 900 }}>
        {locations.map((item, index) => (
          <div key={index} className="col">
            <div
              className={`btn d-flex justify-content-between align-items-center w-100 shadow-sm rounded-3
                ${item.status === 'ON'
                  ? 'btn-success text-white'
                  : isDarkMode
                  ? 'btn-outline-success text-success'
                  : 'btn-outline-primary text-primary'}`}
              style={{
                fontFamily: 'monospace',
                fontWeight: 600,
                fontSize: '0.95rem',
                padding: '0.4rem 0.8rem',
                cursor: 'pointer',
                transition: 'none',
              }}
              onClick={() => toggleStatus(item.name)}
              onMouseEnter={(e) => (e.currentTarget.style.backgroundColor = '')}
              onMouseLeave={(e) => (e.currentTarget.style.backgroundColor = '')}
            >
              <span>{item.name}</span>
              <div className="d-flex gap-2 align-items-center">
                <span
                  className={`badge rounded-pill fs-6 ${
                    item.status === 'ON' ? 'bg-light text-success' : 'bg-success text-light'
                  }`}
                  style={{ minWidth: 50, textAlign: 'center' }}
                >
                  {item.status}
                </span>
                {item.status === 'ON' && item.number !== null && (
                  <small className="text-muted">Number: {item.number}</small>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Submit Button */}
      <button
        className="btn btn-success btn-lg rounded-pill mt-4 shadow"
        onClick={handleSubmit}
        disabled={activeLocations.length === 0}
        title={
          activeLocations.length === 0
            ? 'Select at least one location'
            : 'Submit selected locations'
        }
        style={{ padding: '0.5rem 2.2rem', fontSize: '1.2rem' }}
      >
        Submit
      </button>
    </div>
  );
};

export default ClydeTableScreen;