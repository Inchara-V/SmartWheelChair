import { Link } from 'react-router-dom';
import AccessibilityBadge from './AccessibilityBadge';
import { getScoreColor } from '../../utils/accessibility';

export default function PlaceCard({ place }) {
  return (
    <Link
      to={`/places/${place.id}`}
      className="group overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm transition hover:-translate-y-1 hover:shadow-lg focus-visible:ring-2 focus-visible:ring-primary-500"
    >
      <div className="relative h-44 overflow-hidden">
        <img
          src={place.image}
          alt={place.name}
          className="h-full w-full object-cover transition duration-300 group-hover:scale-105"
          loading="lazy"
        />
        <div className="absolute right-3 top-3">
          <AccessibilityBadge status={place.status} />
        </div>
      </div>
      <div className="p-4">
        <h3 className="font-semibold text-slate-900 group-hover:text-primary-700">{place.name}</h3>
        <p className="mt-1 text-sm text-slate-500">{place.address}</p>
        <div className="mt-3 flex items-center justify-between">
          <span className="text-xs font-medium uppercase tracking-wide text-slate-400">
            {place.category}
          </span>
          <span className={`text-lg font-bold ${getScoreColor(place.accessibilityScore)}`}>
            {place.accessibilityScore}
            <span className="text-xs font-normal text-slate-400">/100</span>
          </span>
        </div>
      </div>
    </Link>
  );
}
