import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useTheme } from '../Utility/ThemeContext';

const DisplayDetailsScreen: React.FC = () => {

    const location = useLocation();
    const navigate = useNavigate();
    const { isDarkMode, toggleTheme } = useTheme();
    const displayList = location.state?.displayList as string[][];

    const [currentPage, setCurrentPage] = useState(1);
    const [recordsPerPage, setRecordsPerPage] = useState(10);
    const indexOfLastRecord = currentPage * recordsPerPage;
    const indexOfFirstRecord = indexOfLastRecord - recordsPerPage;
    const currentRecords = displayList?.slice(indexOfFirstRecord, indexOfLastRecord) || [];
    const totalPages = Math.ceil((displayList?.length || 0) / recordsPerPage); 

    const paginate = (pageNumber: number) => setCurrentPage(pageNumber);

    useEffect(() => {
        setCurrentPage(1);
    }, [recordsPerPage]);

    const handleRecordsPerPageChange = (event: React.ChangeEvent<HTMLSelectElement>) =>
    {
        setRecordsPerPage(Number(event.target.value));
    };

    return (
        <div className={`container-fluid min-vh-75 d-flex flex-column justify-content-start align-items-center py-5 px-3 fade-slide-in
            ${isDarkMode ? 'bg-dark text-success' : 'bg-light text-dark'}`}
            style={{ fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif" }}>

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
                title="Toggle light/dark theme">
                {isDarkMode ? '☀️' : '🌙'}
            </button>

            <div className="w-100 text-center mb-4">
                <h1 className="display-5 fw-bold">🌐 SYS5 2M1 V2.1 - Display Details</h1>
                <hr className="border border-success border-2 opacity-100 w-100 mx-auto" />
            </div>

            <div className="card bg-light border-success text-success shadow-lg rounded-4 w-100 mb-5 fade-slide-in" style={{ maxWidth: '900px' }}>
                <div className="card-body">
                    <h4 className="fw-bold mb-3 text-center">📌 Matching Records</h4>
                    <br/>
                    {displayList && displayList.length > 0 ? (
                        <>
                            <div className="d-flex justify-content-end mb-3">
                                <label htmlFor="recordsPerPage" className="me-2">Rows per page:</label>
                                <select
                                    id="recordsPerPage"
                                    className="form-select form-select-sm"
                                    style={{ width: 'auto' }}
                                    value={recordsPerPage}
                                    onChange={handleRecordsPerPageChange}
                                >
                                    <option value="5">5</option>
                                    <option value="10">10</option>
                                    <option value="20">20</option>
                                    <option value="50">50</option>
                                </select>
                            </div>
                            <div style={{ maxHeight: '400px', overflowY: 'auto' }}>
                                {currentRecords.map((record, index) => (
                                    <React.Fragment key={index}>
                                        <pre className="text-dark mb-0" style={{
                                            fontFamily: 'Consolas, monospace',
                                            whiteSpace: 'pre',
                                            overflowX: 'auto',
                                            maxWidth: '100%',
                                            fontSize: '0.9rem',
                                        }}>
                                            {record.map((line) => {return line;}).join('\n')}
                                        </pre>
                                        {index < currentRecords.length - 1 && <hr className="border-secondary my-2" />}
                                    </React.Fragment>
                                ))}
                            </div>
                            <nav className="mt-3">
                                <ul className="pagination justify-content-center">
                                    {Array.from({ length: totalPages }, (_, i) => (
                                        <li key={i} className={`page-item ${currentPage === i + 1 ? 'active' : ''}`}>
                                            <button className="page-link" onClick={() => paginate(i + 1)}>
                                                {i + 1}
                                            </button>
                                        </li>
                                    ))}
                                </ul>
                            </nav>
                        </>
                    ) : (
                        <p className="text-center text-muted fst-italic">No records to display...</p>
                    )}
                </div>
            </div>

            <button 
                className="btn btn-success btn-lg rounded-pill mt-4 shadow"
                onClick={() => navigate('/clyde-menu', { 
                    state: { 
                        activeLocations: location.state?.activeLocations || [],
                        wordBuffer: location.state?.wordBuffer || [],
                        count: location.state?.count || 0
                    }
                })}
                style={{ padding: '0.5rem 2.2rem', fontSize: '1.2rem' }}>
                Back to Menu
            </button>
        </div>
    );
};

export default DisplayDetailsScreen;