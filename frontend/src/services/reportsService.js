import { delay } from './api';
import mockReports from '../data/mockReports.json';

let reports = [...mockReports];

export async function getReports() {
  await delay();
  return [...reports].sort(
    (a, b) => new Date(b.submittedAt) - new Date(a.submittedAt)
  );
}

export async function submitReport(reportData) {
  await delay(800);
  const newReport = {
    id: reports.length + 1,
    placeName: reportData.placeName,
    status: reportData.status,
    features: {
      ramp: reportData.ramp,
      elevator: reportData.elevator,
      accessibleWashroom: reportData.accessibleWashroom,
    },
    description: reportData.description,
    submittedAt: new Date().toISOString(),
    resolved: false,
  };
  reports = [newReport, ...reports];
  return newReport;
}
