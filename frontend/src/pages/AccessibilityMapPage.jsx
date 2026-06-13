import { useEffect, useState, useCallback } from 'react';
import { useSearchParams } from 'react-router-dom';
import MapView from '../components/map/MapView';
import MapFilterSidebar, { defaultFilters } from '../components/map/MapFilterSidebar';
import LoadingSpinner from '../components/common/LoadingSpinner';
import ErrorMessage from '../components/common/ErrorMessage';
import { getPlaces } from '../services/placesService';

export default function AccessibilityMapPage() {
  const [searchParams] = useSearchParams();
  const [places, setPlaces] = useState([]);
  const [filters, setFilters] = useState({
    ...defaultFilters,
    search: searchParams.get('search') || '',
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const loadPlaces = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await getPlaces(filters);
      setPlaces(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [filters]);

  useEffect(() => {
    loadPlaces();
  }, [loadPlaces]);

  const handleClear = () => setFilters({ ...defaultFilters });

  return (
    <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-slate-900">Accessibility Map</h1>
        <p className="mt-1 text-slate-500">
          Explore places color-coded by accessibility status
        </p>
      </div>

      <div className="grid gap-6 lg:grid-cols-[280px_1fr]">
        <MapFilterSidebar
          filters={filters}
          onChange={setFilters}
          onClear={handleClear}
          resultCount={places.length}
        />

        <div>
          {loading ? (
            <div className="flex h-[500px] items-center justify-center rounded-xl border border-slate-200 bg-white">
              <LoadingSpinner label="Loading map..." />
            </div>
          ) : error ? (
            <ErrorMessage message={error} onRetry={loadPlaces} />
          ) : (
            <MapView places={places} height="calc(100vh - 220px)" />
          )}
        </div>
      </div>
    </div>
  );
}
