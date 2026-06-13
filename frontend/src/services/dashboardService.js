import { delay } from './api';
import mockStats from '../data/mockStats.json';

export async function getDashboardStats() {
  await delay();
  return mockStats;
}

export async function getHomeStats() {
  await delay(400);
  return {
    totalPlaces: mockStats.totalPlaces,
    accessiblePlaces: mockStats.accessiblePlaces,
    averageScore: mockStats.averageScore,
    totalReports: mockStats.totalReports,
  };
}
