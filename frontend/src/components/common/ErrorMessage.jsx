export default function ErrorMessage({ message, onRetry }) {
  return (
    <div
      className="rounded-xl border border-red-200 bg-red-50 p-6 text-center"
      role="alert"
    >
      <p className="mb-1 text-lg font-semibold text-red-800">Something went wrong</p>
      <p className="mb-4 text-sm text-red-600">{message}</p>
      {onRetry && (
        <button
          onClick={onRetry}
          className="rounded-lg bg-red-600 px-4 py-2 text-sm font-medium text-white transition hover:bg-red-700"
        >
          Try Again
        </button>
      )}
    </div>
  );
}
