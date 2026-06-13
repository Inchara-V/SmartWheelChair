import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import AccessibilityBadge from '../components/common/AccessibilityBadge';
import FeatureIcon from '../components/common/FeatureIcon';
import LoadingSpinner from '../components/common/LoadingSpinner';
import ErrorMessage from '../components/common/ErrorMessage';
import MapView from '../components/map/MapView';
import { getPlaceById } from '../services/placesService';
import { FEATURES } from '../utils/constants';
import { getScoreColor, getScoreRingColor } from '../utils/accessibility';

function ScoreRing({ score }) {
  const radius = 40;
  const circumference = 2 * Math.PI * radius;
  const offset = circumference - (score / 100) * circumference;

  return (
    <div className="relative flex h-28 w-28 items-center justify-center">
      <svg className="-rotate-90" width="112" height="112" aria-hidden="true">
        <circle cx="56" cy="56" r={radius} fill="none" stroke="#e2e8f0" strokeWidth="8" />
        <circle
          cx="56"
          cy="56"
          r={radius}
          fill="none"
          className={getScoreRingColor(score)}
          strokeWidth="8"
          strokeLinecap="round"
          strokeDasharray={circumference}
          strokeDashoffset={offset}
        />
      </svg>
      <div className="absolute text-center">
        <span className={`text-2xl font-bold ${getScoreColor(score)}`}>{score}</span>
        <span className="block text-xs text-slate-400">/100</span>
      </div>
    </div>
  );
}

export default function PlaceDetailsPage() {
  const { id } = useParams();
  const [place, setPlace] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const loadPlace = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await getPlaceById(id);
      setPlace(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadPlace();
  }, [id]);

  if (loading) {
    return (
      <div className="mx-auto max-w-7xl px-4 py-16">
        <LoadingSpinner label="Loading place details..." />
      </div>
    );
  }

  if (error) {
    return (
      <div className="mx-auto max-w-7xl px-4 py-16">
        <ErrorMessage message={error} onRetry={loadPlace} />
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
      <Link to="/map" className="text-sm font-medium text-primary-600 hover:underline">
        ← Back to Map
      </Link>

      <div className="mt-6 grid gap-8 lg:grid-cols-2">
        <div>
          <img
            src={place.image}
            alt={place.name}
            className="h-64 w-full rounded-2xl object-cover shadow-md sm:h-80"
          />

          <div className="mt-6 flex flex-wrap items-start justify-between gap-4">
            <div>
              <h1 className="text-3xl font-bold text-slate-900">{place.name}</h1>
              <p className="mt-2 text-slate-500">{place.address}</p>
              <div className="mt-3">
                <AccessibilityBadge status={place.status} size="md" />
              </div>
            </div>
            <ScoreRing score={place.accessibilityScore} />
          </div>

          <div className="mt-8">
            <h2 className="text-lg font-semibold text-slate-900">Accessibility Features</h2>
            <div className="mt-4 grid grid-cols-2 gap-3 sm:grid-cols-4">
              {FEATURES.map((feature) => (
                <FeatureIcon
                  key={feature.key}
                  feature={feature}
                  available={place.features[feature.key]}
                />
              ))}
            </div>
          </div>

          {place.photos?.length > 0 && (
            <div className="mt-8">
              <h2 className="text-lg font-semibold text-slate-900">User Photos</h2>
              <div className="mt-4 flex gap-3 overflow-x-auto pb-2">
                {place.photos.map((photo, i) => (
                  <img
                    key={i}
                    src={photo}
                    alt={`${place.name} photo ${i + 1}`}
                    className="h-32 w-44 shrink-0 rounded-xl object-cover"
                    loading="lazy"
                  />
                ))}
              </div>
            </div>
          )}

          <div className="mt-8">
            <h2 className="text-lg font-semibold text-slate-900">Reviews</h2>
            <div className="mt-4 space-y-4">
              {place.reviews.map((review) => (
                <div key={review.id} className="rounded-xl border border-slate-200 bg-white p-4">
                  <div className="flex items-center justify-between">
                    <span className="font-medium text-slate-900">{review.author}</span>
                    <span className="text-amber-500" aria-label={`${review.rating} out of 5 stars`}>
                      {'★'.repeat(review.rating)}{'☆'.repeat(5 - review.rating)}
                    </span>
                  </div>
                  <p className="mt-2 text-sm text-slate-600">{review.text}</p>
                  <p className="mt-2 text-xs text-slate-400">{review.date}</p>
                </div>
              ))}
            </div>
          </div>
        </div>

        <div>
          <h2 className="mb-4 text-lg font-semibold text-slate-900">Location</h2>
          <MapView places={[place]} height="400px" center={[place.lat, place.lng]} zoom={15} />
          <Link
            to="/report"
            state={{ placeName: place.name }}
            className="mt-4 inline-flex w-full items-center justify-center rounded-xl bg-accent-600 py-3 text-sm font-semibold text-white transition hover:bg-accent-700"
          >
            Report an Issue at This Place
          </Link>
        </div>
      </div>
    </div>
  );
}
