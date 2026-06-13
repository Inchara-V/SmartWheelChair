# Smart Accessibility Navigator

A modern, responsive frontend MVP for a hackathon project that helps wheelchair users, elderly people, visually impaired users, and parents with strollers find accessible places and routes.

## Tech Stack

- **React.js** (Vite)
- **Tailwind CSS**
- **React Router**
- **Axios** (mock API services)
- **Leaflet** + **react-leaflet** (maps)
- **Recharts** (dashboard pie chart)

## Getting Started

```bash
npm install
npm run dev
```

Open [http://localhost:5173](http://localhost:5173) in your browser.

## Pages

| Route | Page |
|-------|------|
| `/` | Home — hero, search, stats, featured places |
| `/map` | Accessibility Map — Leaflet map with color-coded markers & filters |
| `/places/:id` | Place Details — score, features, photos, reviews |
| `/route-planner` | Route Planner — source/destination, route on map, alerts |
| `/report` | Report Issue — submit accessibility reports |
| `/dashboard` | Dashboard — stats, pie chart, recent reports table |

## Project Structure

```
src/
├── components/
│   ├── common/       # Reusable UI (StatCard, SearchBar, PlaceCard, etc.)
│   ├── layout/       # Navbar, Footer, Layout
│   └── map/          # MapView, MapFilterSidebar, marker icons
├── data/             # Mock JSON data
├── pages/            # Route pages
├── services/         # Axios-based mock API services
└── utils/            # Constants & accessibility helpers
```

## Mock API

All data is served from local JSON files with simulated delays via the service layer in `src/services/`. Replace these with real API endpoints when connecting to a backend.

## Build

```bash
npm run build
npm run preview
```
