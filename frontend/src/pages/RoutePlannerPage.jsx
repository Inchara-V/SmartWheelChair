import { useState } from 'react';
import MapView from '../components/map/MapView';
import LoadingSpinner from '../components/common/LoadingSpinner';
import ErrorMessage from '../components/common/ErrorMessage';
import { planRoute } from '../services/routesService';
import { formatDistance, formatDuration, getScoreColor } from '../utils/accessibility';

export default function RoutePlannerPage() {
  const [source, setSource] = useState('');
  const [destination, setDestination] = useState('');
  const [route, setRoute] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setRoute(null);
    try {
      const data = await planRoute(source, destination);
      setRoute(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-slate-900">Accessible Route Planner</h1>
        <p className="mt-1 text-slate-500">
          Plan a route optimized for accessibility needs
        </p>
      </div>

      <div className="grid gap-6 lg:grid-cols-[360px_1fr]">
        <div className="space-y-6">
          <form onSubmit={handleSubmit} className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
            <div className="space-y-4">
              <div>
                <label htmlFor="source" className="mb-1.5 block text-sm font-medium text-slate-700">
                  Source
                </label>
                <input
                  id="source"
                  type="text"
                  value={source}
                  onChange={(e) => setSource(e.target.value)}
                  placeholder="Starting location..."
                  className="w-full rounded-lg border border-slate-200 px-3 py-2.5 text-sm focus:border-primary-400 focus:ring-2 focus:ring-primary-100"
                  required
                />
              </div>
              <div>
                <label htmlFor="destination" className="mb-1.5 block text-sm font-medium text-slate-700">
                  Destination
                </label>
                <input
                  id="destination"
                  type="text"
                  value={destination}
                  onChange={(e) => setDestination(e.target.value)}
                  placeholder="Where do you want to go?"
                  className="w-full rounded-lg border border-slate-200 px-3 py-2.5 text-sm focus:border-primary-400 focus:ring-2 focus:ring-primary-100"
                  required
                />
              </div>
            </div>
            <button
              type="submit"
              disabled={loading}
              className="mt-5 w-full rounded-xl bg-primary-600 py-3 text-sm font-semibold text-white transition hover:bg-primary-700 disabled:opacity-60"
            >
              {loading ? 'Planning Route...' : 'Plan Route'}
            </button>
          </form>

          {loading && <LoadingSpinner label="Calculating accessible route..." />}

          {error && <ErrorMessage message={error} />}

          {route && (
            <div className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
              <h2 className="text-lg font-semibold text-slate-900">Route Summary</h2>
              <div className="mt-4 grid grid-cols-3 gap-4 text-center">
                <div className="rounded-xl bg-slate-50 p-3">
                  <p className="text-xs text-slate-500">Distance</p>
                  <p className="mt-1 font-bold text-slate-900">{formatDistance(route.distance)}</p>
                </div>
                <div className="rounded-xl bg-slate-50 p-3">
                  <p className="text-xs text-slate-500">Est. Time</p>
                  <p className="mt-1 font-bold text-slate-900">{formatDuration(route.duration)}</p>
                </div>
                <div className="rounded-xl bg-slate-50 p-3">
                  <p className="text-xs text-slate-500">Score</p>
                  <p className={`mt-1 font-bold ${getScoreColor(route.accessibilityScore)}`}>
                    {route.accessibilityScore}
                  </p>
                </div>
              </div>

              {route.alerts?.length > 0 && (
                <div className="mt-5">
                  <h3 className="text-sm font-semibold text-slate-700">Accessibility Alerts</h3>
                  <ul className="mt-3 space-y-2">
                    {route.alerts.map((alert, i) => (
                      <li
                        key={i}
                        className={`rounded-lg px-3 py-2 text-sm ${
                          alert.type === 'warning'
                            ? 'bg-amber-50 text-amber-800'
                            : 'bg-primary-50 text-primary-800'
                        }`}
                      >
                        {alert.type === 'warning' ? '⚠️' : 'ℹ️'} {alert.message}
                      </li>
                    ))}
                  </ul>
                </div>
              )}
            </div>
          )}
        </div>

        <div>
          {route ? (
            <MapView routeCoordinates={route.coordinates} height="calc(100vh - 180px)" />
          ) : (
            <div className="flex h-[500px] items-center justify-center rounded-xl border-2 border-dashed border-slate-200 bg-slate-50">
              <div className="text-center text-slate-400">
                <p className="text-4xl">🗺️</p>
                <p className="mt-2 text-sm">Enter source and destination to see your route</p>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
