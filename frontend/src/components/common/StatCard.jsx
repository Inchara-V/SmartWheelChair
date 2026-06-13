export default function StatCard({ icon, label, value, subtext, color = 'primary' }) {
  const colors = {
    primary: 'from-primary-500 to-primary-700',
    accent: 'from-accent-500 to-accent-700',
    amber: 'from-amber-400 to-amber-600',
    slate: 'from-slate-500 to-slate-700',
  };

  return (
    <div className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm transition hover:shadow-md">
      <div className="flex items-start justify-between">
        <div>
          <p className="text-sm font-medium text-slate-500">{label}</p>
          <p className="mt-1 text-3xl font-bold text-slate-900">{value}</p>
          {subtext && <p className="mt-1 text-xs text-slate-400">{subtext}</p>}
        </div>
        <div
          className={`flex h-12 w-12 items-center justify-center rounded-xl bg-gradient-to-br ${colors[color]} text-xl text-white shadow-sm`}
          aria-hidden="true"
        >
          {icon}
        </div>
      </div>
    </div>
  );
}
