import { Link } from 'react-router-dom';

export default function Footer() {
  return (
    <footer className="border-t border-slate-200 bg-white">
      <div className="mx-auto max-w-7xl px-4 py-10 sm:px-6 lg:px-8">
        <div className="grid gap-8 md:grid-cols-3">
          <div>
            <h3 className="font-bold text-primary-800">Smart Accessibility Navigator</h3>
            <p className="mt-2 text-sm text-slate-500">
              Helping wheelchair users, elderly people, visually impaired users, and parents with strollers find accessible places and routes.
            </p>
          </div>
          <div>
            <h4 className="text-sm font-semibold text-slate-700">Quick Links</h4>
            <ul className="mt-3 space-y-2 text-sm">
              <li><Link to="/map" className="text-slate-500 hover:text-primary-600">Accessibility Map</Link></li>
              <li><Link to="/route-planner" className="text-slate-500 hover:text-primary-600">Route Planner</Link></li>
              <li><Link to="/report" className="text-slate-500 hover:text-primary-600">Report Issue</Link></li>
            </ul>
          </div>
          <div>
            <h4 className="text-sm font-semibold text-slate-700">Hackathon Project</h4>
            <p className="mt-3 text-sm text-slate-500">
              Built for smart cities — making urban spaces inclusive for everyone.
            </p>
          </div>
        </div>
        <p className="mt-8 border-t border-slate-100 pt-6 text-center text-xs text-slate-400">
          © 2026 Smart Accessibility Navigator. All rights reserved.
        </p>
      </div>
    </footer>
  );
}
