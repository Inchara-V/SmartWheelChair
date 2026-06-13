import { useEffect, useState } from 'react';
import { PieChart, Pie, Cell, ResponsiveContainer, Legend, Tooltip } from 'recharts';
import StatCard from '../components/common/StatCard';
import LoadingSpinner from '../components/common/LoadingSpinner';
import ErrorMessage from '../components/common/ErrorMessage';
import AccessibilityBadge from '../components/common/AccessibilityBadge';
import { getDashboardStats } from '../services/dashboardService';
import { getReports } from '../services/reportsService';

export default function DashboardPage() {
  const [stats, setStats] = useState(null);
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const loadData = async () => {
    setLoading(true);
    setError(null);
    try {
      const [statsData, reportsData] = await Promise.all([
        getDashboardStats(),
        getReports(),
      ]);
      setStats(statsData);
      setReports(reportsData);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  if (loading) {
    return (
      <div className="mx-auto max-w-7xl px-4 py-16">
        <LoadingSpinner label="Loading dashboard..." />
      </div>
    );
  }

  if (error) {
    return (
      <div className="mx-auto max-w-7xl px-4 py-16">
        <ErrorMessage message={error} onRetry={loadData} />
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-slate-900">Dashboard</h1>
        <p className="mt-1 text-slate-500">Overview of accessibility data and community reports</p>
      </div>

      <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-4">
        <StatCard icon="📍" label="Total Places" value={stats.totalPlaces.toLocaleString()} color="primary" />
        <StatCard icon="✅" label="Accessible" value={stats.accessiblePlaces.toLocaleString()} color="accent" />
        <StatCard icon="📝" label="Total Reports" value={stats.totalReports.toLocaleString()} color="amber" />
        <StatCard icon="✔️" label="Resolved Reports" value={stats.resolvedReports.toLocaleString()} color="slate" />
      </div>

      <div className="mt-8 grid gap-6 lg:grid-cols-2">
        <div className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
          <h2 className="text-lg font-semibold text-slate-900">Accessibility Distribution</h2>
          <div className="mt-4 h-72">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie
                  data={stats.accessibilityDistribution}
                  cx="50%"
                  cy="50%"
                  innerRadius={60}
                  outerRadius={100}
                  paddingAngle={3}
                  dataKey="value"
                  nameKey="name"
                >
                  {stats.accessibilityDistribution.map((entry, i) => (
                    <Cell key={i} fill={entry.color} />
                  ))}
                </Pie>
                <Tooltip />
                <Legend />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>

        <div className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
          <h2 className="text-lg font-semibold text-slate-900">Recent Reports</h2>
          <div className="mt-4 overflow-x-auto">
            <table className="w-full text-left text-sm">
              <thead>
                <tr className="border-b border-slate-200 text-xs uppercase tracking-wide text-slate-400">
                  <th className="pb-3 pr-4 font-medium">Place</th>
                  <th className="pb-3 pr-4 font-medium">Status</th>
                  <th className="pb-3 pr-4 font-medium">Date</th>
                  <th className="pb-3 font-medium">Resolved</th>
                </tr>
              </thead>
              <tbody>
                {reports.slice(0, 8).map((report) => (
                  <tr key={report.id} className="border-b border-slate-100">
                    <td className="py-3 pr-4 font-medium text-slate-900">{report.placeName}</td>
                    <td className="py-3 pr-4">
                      <AccessibilityBadge status={report.status} />
                    </td>
                    <td className="py-3 pr-4 text-slate-500">
                      {new Date(report.submittedAt).toLocaleDateString()}
                    </td>
                    <td className="py-3">
                      <span
                        className={`inline-flex rounded-full px-2 py-0.5 text-xs font-medium ${
                          report.resolved
                            ? 'bg-accent-100 text-accent-800'
                            : 'bg-amber-100 text-amber-800'
                        }`}
                      >
                        {report.resolved ? 'Yes' : 'Pending'}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}
