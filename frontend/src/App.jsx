import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Layout from './components/layout/Layout';
import HomePage from './pages/HomePage';
import AccessibilityMapPage from './pages/AccessibilityMapPage';
import PlaceDetailsPage from './pages/PlaceDetailsPage';
import RoutePlannerPage from './pages/RoutePlannerPage';
import ReportIssuePage from './pages/ReportIssuePage';
import DashboardPage from './pages/DashboardPage';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route index element={<HomePage />} />
          <Route path="map" element={<AccessibilityMapPage />} />
          <Route path="places/:id" element={<PlaceDetailsPage />} />
          <Route path="route-planner" element={<RoutePlannerPage />} />
          <Route path="report" element={<ReportIssuePage />} />
          <Route path="dashboard" element={<DashboardPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
