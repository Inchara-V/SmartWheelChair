import { delay } from './api';
import mockPlaces from '../data/mockPlaces.json';

export async function getPlaces(filters = {}) {
  await delay();
  let results = [...mockPlaces];

  if (filters.search) {
    const q = filters.search.toLowerCase();
    results = results.filter(
      (p) =>
        p.name.toLowerCase().includes(q) ||
        p.address.toLowerCase().includes(q) ||
        p.category.toLowerCase().includes(q)
    );
  }

  if (filters.status) {
    results = results.filter((p) => p.status === filters.status);
  }

  if (filters.features) {
    Object.entries(filters.features).forEach(([key, required]) => {
      if (required) results = results.filter((p) => p.features[key]);
    });
  }

  return results;
}

export async function getPlaceById(id) {
  await delay();
  const place = mockPlaces.find((p) => p.id === Number(id));
  if (!place) throw new Error('Place not found');
  return place;
}

export async function getFeaturedPlaces() {
  await delay(400);
  return mockPlaces.filter((p) => p.accessibilityScore >= 80).slice(0, 4);
}

export async function searchPlaces(query) {
  return getPlaces({ search: query });
}
