export const ACCESSIBILITY_STATUS = {
  ACCESSIBLE: 'accessible',
  PARTIAL: 'partial',
  NOT_ACCESSIBLE: 'not_accessible',
};

export const STATUS_LABELS = {
  [ACCESSIBILITY_STATUS.ACCESSIBLE]: 'Accessible',
  [ACCESSIBILITY_STATUS.PARTIAL]: 'Partially Accessible',
  [ACCESSIBILITY_STATUS.NOT_ACCESSIBLE]: 'Not Accessible',
};

export const STATUS_COLORS = {
  [ACCESSIBILITY_STATUS.ACCESSIBLE]: '#10b981',
  [ACCESSIBILITY_STATUS.PARTIAL]: '#f59e0b',
  [ACCESSIBILITY_STATUS.NOT_ACCESSIBLE]: '#ef4444',
};

export const STATUS_BG = {
  [ACCESSIBILITY_STATUS.ACCESSIBLE]: 'bg-accent-100 text-accent-800',
  [ACCESSIBILITY_STATUS.PARTIAL]: 'bg-amber-100 text-amber-800',
  [ACCESSIBILITY_STATUS.NOT_ACCESSIBLE]: 'bg-red-100 text-red-800',
};

export const FEATURES = [
  { key: 'ramp', label: 'Ramp', icon: '🛤️' },
  { key: 'elevator', label: 'Elevator', icon: '🛗' },
  { key: 'accessibleWashroom', label: 'Accessible Washroom', icon: '🚻' },
  { key: 'parking', label: 'Parking', icon: '🅿️' },
];

export const DEFAULT_MAP_CENTER = [28.6139, 77.209];
export const DEFAULT_MAP_ZOOM = 13;
