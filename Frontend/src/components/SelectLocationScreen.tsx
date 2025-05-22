import 'bootstrap/dist/css/bootstrap.min.css';
import '../CSS/ClydeScreens.css';
import { useNavigate } from 'react-router';
import { useTheme } from '../Utility/ThemeContext';

function SelectLocationScreen()
{
  const navigate = useNavigate();
  const { isDarkMode, toggleTheme } = useTheme();

  const handleSelect = () => {
    navigate('/clyde-table');
  };

  const locations = [
    'Just investigating; no batch or write up activity will be done',
    'Illi CPPO (prt L43SH29)',
    'Mich CPPO (prt xxxxxxxx)',
    'Error Processing only',
    'CABS.1 (prt R9514)',
    'CABS.2 (prt R9515)',
    'Paging Cellular',
    'Not used',
  ];

  return (
    <div
      className={`container-fluid min-vh-75 d-flex flex-column align-items-center py-4 px-3 fade-slide-in
        ${isDarkMode ? 'bg-dark text-success' : 'bg-light text-dark'}`}
      style={{ fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif" }}
    >
      {/* Theme toggle */}
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
        <h2 className="display-5 fw-bold">
          🌐 Clyde Select Location
        </h2>
        <hr
          className={`border ${isDarkMode ? 'border-success' : 'border-success'} border-2 opacity-100 mx-auto`}
          style={{ maxWidth: 600 }}
        />
      </div>

      {/* Locations Grid */}
      <div className="row row-cols-1 row-cols-md-2 g-3 w-100 mb-3" style={{ maxWidth: '900px' }}>
        {locations.map((item, index) => (
          <div className="col" key={index}>
            <button
              className={`btn d-flex justify-content-between align-items-center w-100 shadow-sm rounded-3
                ${item === 'Just investigating; no batch or write up activity will be done'
                  ? isDarkMode
                    ? 'btn-success text-white'
                    : 'btn-primary text-white'
                  : isDarkMode
                  ? 'btn-outline-success text-success'
                  : 'btn-outline-primary text-primary'
                }`}
              style={{
                fontFamily: 'monospace',
                fontWeight: 600,
                fontSize: '0.95rem',
                padding: '0.4rem 0.8rem',
                cursor: 'pointer',
                transition: 'none',
              }}
              onClick={() => {
                handleSelect();
              }}
              onMouseEnter={e => e.currentTarget.style.backgroundColor = ''}
              onMouseLeave={e => e.currentTarget.style.backgroundColor = ''}
            >
              <span>{item}</span>
            </button>
          </div>
        ))}
      </div>
    </div>
  ); 
}

export default SelectLocationScreen;
