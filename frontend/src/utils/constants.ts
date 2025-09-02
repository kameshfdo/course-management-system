export const API_BASE_URL = 'http://localhost:8080/api';

export const REGISTRATION_STATUS = {
  ENROLLED: 'ENROLLED',
  DROPPED: 'DROPPED',
  COMPLETED: 'COMPLETED'
} as const;

export const GRADE_COLORS: Record<string, string> = {
  'A+': 'bg-green-100 text-green-800',
  'A': 'bg-green-100 text-green-800',
  'A-': 'bg-green-100 text-green-800',
  'B+': 'bg-blue-100 text-blue-800',
  'B': 'bg-blue-100 text-blue-800',
  'B-': 'bg-blue-100 text-blue-800',
  'C+': 'bg-yellow-100 text-yellow-800',
  'C': 'bg-yellow-100 text-yellow-800',
  'C-': 'bg-yellow-100 text-yellow-800',
  'F': 'bg-red-100 text-red-800'
};

export const STATUS_COLORS: Record<string, string> = {
  ENROLLED: 'bg-green-100 text-green-800',
  DROPPED: 'bg-red-100 text-red-800',
  COMPLETED: 'bg-blue-100 text-blue-800'
};

export const DEFAULT_PAGE_SIZE = 10;
export const DEFAULT_SORT_BY = 'id';
export const DEFAULT_SORT_DIR = 'asc';