export default function FeatureIcon({ feature, available }) {
  return (
    <div
      className={`flex flex-col items-center rounded-xl border p-4 text-center transition ${
        available
          ? 'border-accent-200 bg-accent-50 text-accent-800'
          : 'border-slate-200 bg-slate-50 text-slate-400'
      }`}
      aria-label={`${feature.label}: ${available ? 'Available' : 'Not available'}`}
    >
      <span className="text-2xl" aria-hidden="true">
        {feature.icon}
      </span>
      <span className="mt-2 text-xs font-medium">{feature.label}</span>
      <span className="mt-1 text-xs">{available ? '✓ Yes' : '✗ No'}</span>
    </div>
  );
}
