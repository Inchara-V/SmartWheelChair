import { ACCESSIBILITY_STATUS, STATUS_LABELS } from './constants';

export function getScoreColor(score) {
  if (score >= 80) return 'text-accent-600';
  if (score >= 50) return 'text-amber-600';
  return 'text-red-600';
}

export function getScoreRingColor(score) {
  if (score >= 80) return 'stroke-accent-500';
  if (score >= 50) return 'stroke-amber-500';
  return 'stroke-red-500';
}

export function getStatusFromScore(score) {
  if (score >= 80) return ACCESSIBILITY_STATUS.ACCESSIBLE;
  if (score >= 50) return ACCESSIBILITY_STATUS.PARTIAL;
  return ACCESSIBILITY_STATUS.NOT_ACCESSIBLE;
}

export function formatDistance(meters) {
  if (meters >= 1000) return `${(meters / 1000).toFixed(1)} km`;
  return `${Math.round(meters)} m`;
}

export function formatDuration(minutes) {
  if (minutes >= 60) {
    const h = Math.floor(minutes / 60);
    const m = minutes % 60;
    return m > 0 ? `${h}h ${m}min` : `${h}h`;
  }
  return `${minutes} min`;
}

export function getStatusLabel(status) {
  return STATUS_LABELS[status] || status;
}
