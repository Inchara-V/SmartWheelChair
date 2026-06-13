import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import SearchBar from '../components/common/SearchBar';
import StatCard from '../components/common/StatCard';
import PlaceCard from '../components/common/PlaceCard';
import LoadingSpinner from '../components/common/LoadingSpinner';
import ErrorMessage from '../components/common/ErrorMessage';
import { getHomeStats } from '../services/dashboardService';
import { getFeaturedPlaces } from '../services/placesService';

export default function HomePage() {
  const [stats, setStats] = useState(null);
  const [featured, setFeatured] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const loadData = async () => {
    setLoading(true);
    setError(null);
    try {
      const [statsData, featuredData] = await Promise.all([
        getHomeStats(),
        getFeaturedPlaces(),
      ]);
      setStats(statsData);
      setFeatured(featuredData);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  return (
    <div>
      {/* Hero */}
      <section className="relative overflow-hidden bg-gradient-to-br from-primary-700 via-primary-600 to-accent-600 text-white">
        <div className="absolute inset-0 opacity-10">
          <div className="absolute -right-20 -top-20 h-96 w-96 rounded-full bg-white" />
          <div className="absolute -bottom-32 -left-20 h-80 w-80 rounded-full bg-white" />
        </div>
        <div className="relative mx-auto max-w-7xl px-4 py-20 sm:px-6 lg:px-8 lg:py-28">
          <div className="mx-auto max-w-3xl text-center">
            <span className="inline-block rounded-full bg-white/20 px-4 py-1 text-sm font-medium backdrop-blur-sm">
              Smart City Hackathon 2026
            </span>
            <h1 className="mt-6 text-4xl font-extrabold tracking-tight sm:text-5xl lg:text-6xl">
              Smart Accessibility Navigator
            </h1>
            <p className="mt-6 text-lg text-primary-100 sm:text-xl">
              Find accessible places and routes for wheelchair users, elderly people,
              visually impaired users, and parents with strollers.
            </p>
            <div className="mt-10">
              <SearchBar className="mx-auto max-w-xl" />
            </div>
            <div className="mt-8 flex flex-wrap justify-center gap-4">
              <Link
                to="/map"
                className="rounded-xl bg-white px-6 py-3 text-sm font-semibold text-primary-700 shadow-lg transition hover:bg-primary-50"
              >
                Explore Map
              </Link>
              <Link
                to="/route-planner"
                className="rounded-xl border-2 border-white/60 px-6 py-3 text-sm font-semibold text-white transition hover:bg-white/10"
              >
                Plan Accessible Route
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* Stats */}
      <section className="mx-auto max-w-7xl px-4 py-16 sm:px-6 lg:px-8">
        <h2 className="mb-8 text-center text-2xl font-bold text-slate-900">
          Accessibility at a Glance
        </h2>
        {loading ? (
          <LoadingSpinner label="Loading statistics..." />
        ) : error ? (
          <ErrorMessage message={error} onRetry={loadData} />
        ) : (
          <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-4">
            <StatCard icon="📍" label="Total Places" value={stats.totalPlaces.toLocaleString()} color="primary" />
            <StatCard icon="✅" label="Accessible Places" value={stats.accessiblePlaces.toLocaleString()} color="accent" />
            <StatCard icon="📊" label="Average Score" value={`${stats.averageScore}/100`} color="amber" />
            <StatCard icon="📝" label="Community Reports" value={stats.totalReports.toLocaleString()} color="slate" />
          </div>
        )}
      </section>

      {/* Featured Places */}
      <section className="bg-white py-16">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <div className="mb-8 flex flex-col items-start justify-between gap-4 sm:flex-row sm:items-center">
            <div>
              <h2 className="text-2xl font-bold text-slate-900">Featured Accessible Places</h2>
              <p className="mt-1 text-slate-500">Top-rated locations in your city</p>
            </div>
            <Link to="/map" className="text-sm font-semibold text-primary-600 hover:underline">
              View all on map →
            </Link>
          </div>
          {loading ? (
            <LoadingSpinner label="Loading places..." />
          ) : error ? null : (
            <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-4">
              {featured.map((place) => (
                <PlaceCard key={place.id} place={place} />
              ))}
            </div>
          )}
        </div>
      </section>

      {/* CTA */}
      <section className="mx-auto max-w-7xl px-4 py-16 sm:px-6 lg:px-8">
        <div className="rounded-3xl bg-gradient-to-r from-primary-600 to-accent-600 p-8 text-center text-white sm:p-12">
          <h2 className="text-2xl font-bold sm:text-3xl">Help Make Your City More Accessible</h2>
          <p className="mx-auto mt-4 max-w-2xl text-primary-100">
            Spot an accessibility issue? Report it and help others navigate with confidence.
          </p>
          <div className="mt-8 flex flex-wrap justify-center gap-4">
            <Link
              to="/report"
              className="rounded-xl bg-white px-6 py-3 text-sm font-semibold text-primary-700 shadow transition hover:bg-primary-50"
            >
              Report an Issue
            </Link>
            <Link
              to="/dashboard"
              className="rounded-xl border-2 border-white/50 px-6 py-3 text-sm font-semibold transition hover:bg-white/10"
            >
              View Dashboard
            </Link>
          </div>
        </div>
      </section>
    </div>
  );
}
