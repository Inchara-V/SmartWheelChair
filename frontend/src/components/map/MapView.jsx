import { Link } from 'react-router-dom';
import { MapContainer, TileLayer, Marker, Popup, Polyline, useMap } from 'react-leaflet';
import { useEffect } from 'react';
import { DEFAULT_MAP_CENTER, DEFAULT_MAP_ZOOM } from '../../utils/constants';
import { createColoredIcon, createRouteIcon } from './markerIcons';
import AccessibilityBadge from '../common/AccessibilityBadge';

function FitBounds({ places, routeCoordinates }) {
  const map = useMap();

  useEffect(() => {
    if (routeCoordinates?.length > 1) {
      map.fitBounds(routeCoordinates, { padding: [40, 40] });
    } else if (places?.length > 0) {
      const bounds = places.map((p) => [p.lat, p.lng]);
      map.fitBounds(bounds, { padding: [40, 40] });
    }
  }, [map, places, routeCoordinates]);

  return null;
}

export default function MapView({
  places = [],
  routeCoordinates = null,
  height = '500px',
  showPopups = true,
  center = DEFAULT_MAP_CENTER,
  zoom = DEFAULT_MAP_ZOOM,
}) {
  return (
    <div style={{ height }} className="overflow-hidden rounded-xl border border-slate-200 shadow-sm">
      <MapContainer center={center} zoom={zoom} scrollWheelZoom className="h-full w-full">
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />

        {places.map((place) => (
          <Marker
            key={place.id}
            position={[place.lat, place.lng]}
            icon={createColoredIcon(place.status)}
          >
            {showPopups && (
              <Popup>
                <div className="min-w-[180px]">
                  <p className="font-semibold text-slate-900">{place.name}</p>
                  <p className="text-xs text-slate-500">{place.address}</p>
                  <div className="mt-2">
                    <AccessibilityBadge status={place.status} />
                  </div>
                  <Link
                    to={`/places/${place.id}`}
                    className="mt-2 inline-block text-xs font-medium text-primary-600 hover:underline"
                  >
                    View Details →
                  </Link>
                </div>
              </Popup>
            )}
          </Marker>
        ))}

        {routeCoordinates?.length > 1 && (
          <>
            <Polyline
              positions={routeCoordinates}
              pathOptions={{ color: '#2563eb', weight: 5, opacity: 0.8 }}
            />
            <Marker
              position={routeCoordinates[0]}
              icon={createRouteIcon('#10b981', 'A')}
            />
            <Marker
              position={routeCoordinates[routeCoordinates.length - 1]}
              icon={createRouteIcon('#ef4444', 'B')}
            />
          </>
        )}

        <FitBounds places={places} routeCoordinates={routeCoordinates} />
      </MapContainer>
    </div>
  );
}
