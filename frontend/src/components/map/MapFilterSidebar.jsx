import { FEATURES, STATUS_LABELS } from '../../utils/constants';

export default function MapFilterSidebar({ filters, onChange, onClear, resultCount }) {
  const handleFeatureChange = (key, checked) => {
    onChange({
      ...filters,
      features: { ...filters.features, [key]: checked },
    });
  };

  return (
    <aside className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
      <div className="mb-4 flex items-center justify-between">
        <h2 className="text-lg font-semibold text-slate-900">Filters</h2>
        <button
          onClick={onClear}
          className="text-xs font-medium text-primary-600 hover:underline"
        >
          Clear all
        </button>
      </div>

      <div className="space-y-5">
        <div>
          <label htmlFor="filter-search" className="mb-1.5 block text-sm font-medium text-slate-700">
            Search
          </label>
          <input
            id="filter-search"
            type="search"
            value={filters.search}
            onChange={(e) => onChange({ ...filters, search: e.target.value })}
            placeholder="Place name or address..."
            className="w-full rounded-lg border border-slate-200 px-3 py-2 text-sm focus:border-primary-400 focus:ring-2 focus:ring-primary-100"
          />
        </div>

        <div>
          <p className="mb-2 text-sm font-medium text-slate-700">Accessibility Status</p>
          <div className="space-y-2">
            <label className="flex items-center gap-2 text-sm">
              <input
                type="radio"
                name="status"
                checked={!filters.status}
                onChange={() => onChange({ ...filters, status: '' })}
                className="text-primary-600"
              />
              All
            </label>
            {Object.entries(STATUS_LABELS).map(([value, label]) => (
              <label key={value} className="flex items-center gap-2 text-sm">
                <input
                  type="radio"
                  name="status"
                  checked={filters.status === value}
                  onChange={() => onChange({ ...filters, status: value })}
                  className="text-primary-600"
                />
                {label}
              </label>
            ))}
          </div>
        </div>

        <div>
          <p className="mb-2 text-sm font-medium text-slate-700">Required Features</p>
          <div className="space-y-2">
            {FEATURES.map(({ key, label }) => (
              <label key={key} className="flex items-center gap-2 text-sm">
                <input
                  type="checkbox"
                  checked={filters.features[key] || false}
                  onChange={(e) => handleFeatureChange(key, e.target.checked)}
                  className="rounded text-primary-600"
                />
                {label}
              </label>
            ))}
          </div>
        </div>

        <div className="rounded-lg bg-slate-50 p-3">
          <p className="text-sm text-slate-600">
            <span className="font-semibold text-slate-900">{resultCount}</span> places found
          </p>
        </div>

        <div className="space-y-2 border-t border-slate-100 pt-4">
          <p className="text-xs font-medium uppercase tracking-wide text-slate-400">Legend</p>
          {[
            { color: 'bg-accent-500', label: 'Accessible' },
            { color: 'bg-amber-400', label: 'Partially Accessible' },
            { color: 'bg-red-500', label: 'Not Accessible' },
          ].map(({ color, label }) => (
            <div key={label} className="flex items-center gap-2 text-xs text-slate-600">
              <span className={`h-3 w-3 rounded-full ${color}`} />
              {label}
            </div>
          ))}
        </div>
      </div>
    </aside>
  );
}

export const defaultFilters = {
  search: '',
  status: '',
  features: {},
};
