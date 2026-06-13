import { delay } from './api';

const MOCK_ROUTES = {
  default: {
    distance: 1850,
    duration: 24,
    accessibilityScore: 82,
    alerts: [
      { type: 'warning', message: 'Sidewalk narrows near Green Park intersection' },
      { type: 'info', message: 'Elevator available at Metro Station checkpoint' },
    ],
    coordinates: [
      [28.6139, 77.209],
      [28.608, 77.212],
      [28.6012, 77.205],
      [28.5921, 77.210],
      [28.585, 77.215],
    ],
  },
};

export async function planRoute(source, destination) {
  await delay(1000);

  if (!source.trim() || !destination.trim()) {
    throw new Error('Please enter both source and destination');
  }

  return {
    source,
    destination,
    ...MOCK_ROUTES.default,
  };
}
