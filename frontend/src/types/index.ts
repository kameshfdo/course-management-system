export interface Student {
  id?: number;
  studentId: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  dateOfBirth?: string;
  department: string;
  enrollmentYear?: number;
  createdAt?: string;
  updatedAt?: string;
}

export interface Course {
  id?: number;
  code: string;
  title: string;
  description?: string;
  credits: number;
  department: string;
  maxEnrollment?: number;
  currentEnrollment?: number;
  createdAt?: string;
  updatedAt?: string;
}

export interface Registration {
  id?: number;
  studentId: number;
  studentName?: string;
  courseId: number;
  courseCode?: string;
  courseTitle?: string;
  registrationDate?: string;
  status: RegistrationStatus;
  remarks?: string;
}

export interface Result {
  id?: number;
  registrationId: number;
  studentId?: number;
  studentName?: string;
  courseId?: number;
  courseCode?: string;
  courseTitle?: string;
  marks: number;
  grade?: string;
  gpaPoints?: number;
  feedback?: string;
  resultDate?: string;
  createdAt?: string;
  updatedAt?: string;
}

export enum RegistrationStatus {
  ENROLLED = 'ENROLLED',
  DROPPED = 'DROPPED',
  COMPLETED = 'COMPLETED'
}

export interface Tab {
  id: string;
  label: string;
  icon: any;
}

export interface ApiResponse<T> {
  data: T;
  message?: string;
  status?: number;
}

export interface SearchParams {
  page?: number;
  size?: number;
  sortBy?: string;
  sortDir?: string;
}