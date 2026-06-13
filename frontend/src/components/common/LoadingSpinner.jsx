export default function LoadingSpinner({ size = 'md', label = 'Loading...' }) {
  const sizes = {
    sm: 'h-5 w-5 border-2',
    md: 'h-8 w-8 border-3',
    lg: 'h-12 w-12 border-4',
  };

  return (
    <div className="flex flex-col items-center justify-center gap-3 py-8" role="status" aria-live="polite">
      <div
        className={`${sizes[size]} animate-spin rounded-full border-primary-200 border-t-primary-600`}
        aria-hidden="true"
      />
      <span className="text-sm text-slate-500">{label}</span>
    </div>
  );
}
