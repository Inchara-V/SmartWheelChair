import { STATUS_BG, STATUS_LABELS } from '../../utils/constants';

export default function AccessibilityBadge({ status, size = 'sm' }) {
  const sizes = {
    sm: 'px-2.5 py-0.5 text-xs',
    md: 'px-3 py-1 text-sm',
  };

  return (
    <span
      className={`inline-flex items-center rounded-full font-semibold ${STATUS_BG[status]} ${sizes[size]}`}
    >
      {STATUS_LABELS[status]}
    </span>
  );
}
