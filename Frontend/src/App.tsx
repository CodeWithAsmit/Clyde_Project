import { Routes, Route } from 'react-router-dom'
import SelectLocationScreen from './components/SelectLocationScreen'
import ClydeTableScreen from './components/ClydeTableScreen'
import ClydeMenuScreen from './components/ClydeMenu'
import DisplayDetailsScreen from './components/DisplayDetailsScreen'
import './App.css'
function App() {
  return (
    <div className="container mt-5">
      <Routes>
        <Route path="/" element={<SelectLocationScreen />} />
        <Route path="/clyde-table" element={<ClydeTableScreen />} />
        <Route path="/clyde-menu" element={<ClydeMenuScreen />} />
        <Route path="/display-details" element={<DisplayDetailsScreen />} />
      </Routes>
    </div>
  )
}

export default App