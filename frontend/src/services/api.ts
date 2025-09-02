import { Student, Course, Registration, Result, SearchParams } from '../types';

const API_BASE_URL = 'http://localhost:8080/api';

class ApiService {
  private async request<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
    const url = `${API_BASE_URL}${endpoint}`;
    const config: RequestInit = {
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
      ...options,
    };

    try {
      const response = await fetch(url, config);
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      if (response.status === 204) {
        return null as T; // No content for DELETE operations
      }
      
      return await response.json();
    } catch (error) {
      console.error(`API request failed: ${endpoint}`, error);
      throw error;
    }
  }

  // Students API
  async getStudents(params: SearchParams = {}): Promise<Student[]> {
    const queryString = new URLSearchParams(params as any).toString();
    return this.request<Student[]>(`/students${queryString ? `?${queryString}` : ''}`);
  }

  async getStudentById(id: number): Promise<Student> {
    return this.request<Student>(`/students/${id}`);
  }

  async createStudent(student: Student): Promise<Student> {
    return this.request<Student>('/students', {
      method: 'POST',
      body: JSON.stringify(student),
    });
  }

  async updateStudent(id: number, student: Student): Promise<Student> {
    return this.request<Student>(`/students/${id}`, {
      method: 'PUT',
      body: JSON.stringify(student),
    });
  }

  async deleteStudent(id: number): Promise<void> {
    return this.request<void>(`/students/${id}`, {
      method: 'DELETE',
    });
  }

  // Courses API
  async getCourses(params: SearchParams = {}): Promise<Course[]> {
    const queryString = new URLSearchParams(params as any).toString();
    return this.request<Course[]>(`/courses${queryString ? `?${queryString}` : ''}`);
  }

  async getCourseById(id: number): Promise<Course> {
    return this.request<Course>(`/courses/${id}`);
  }

  async createCourse(course: Course): Promise<Course> {
    return this.request<Course>('/courses', {
      method: 'POST',
      body: JSON.stringify(course),
    });
  }

  async updateCourse(id: number, course: Course): Promise<Course> {
    return this.request<Course>(`/courses/${id}`, {
      method: 'PUT',
      body: JSON.stringify(course),
    });
  }

  async deleteCourse(id: number): Promise<void> {
    return this.request<void>(`/courses/${id}`, {
      method: 'DELETE',
    });
  }

  // Registrations API
  async getRegistrations(params: SearchParams = {}): Promise<Registration[]> {
    const queryString = new URLSearchParams(params as any).toString();
    return this.request<Registration[]>(`/registrations${queryString ? `?${queryString}` : ''}`);
  }

  async getRegistrationById(id: number): Promise<Registration> {
    return this.request<Registration>(`/registrations/${id}`);
  }

  async createRegistration(registration: Registration): Promise<Registration> {
    return this.request<Registration>('/registrations', {
      method: 'POST',
      body: JSON.stringify(registration),
    });
  }

  async updateRegistration(id: number, registration: Registration): Promise<Registration> {
    return this.request<Registration>(`/registrations/${id}`, {
      method: 'PUT',
      body: JSON.stringify(registration),
    });
  }

  async deleteRegistration(id: number): Promise<void> {
    return this.request<void>(`/registrations/${id}`, {
      method: 'DELETE',
    });
  }

  // Results API
  async getResults(params: SearchParams = {}): Promise<Result[]> {
    const queryString = new URLSearchParams(params as any).toString();
    return this.request<Result[]>(`/results${queryString ? `?${queryString}` : ''}`);
  }

  async getResultById(id: number): Promise<Result> {
    return this.request<Result>(`/results/${id}`);
  }

  async createResult(result: Result): Promise<Result> {
    return this.request<Result>('/results', {
      method: 'POST',
      body: JSON.stringify(result),
    });
  }

  async updateResult(id: number, result: Result): Promise<Result> {
    return this.request<Result>(`/results/${id}`, {
      method: 'PUT',
      body: JSON.stringify(result),
    });
  }

  async deleteResult(id: number): Promise<void> {
    return this.request<void>(`/results/${id}`, {
      method: 'DELETE',
    });
  }

  async getStudentGPA(studentId: number): Promise<number> {
    return this.request<number>(`/results/student/${studentId}/gpa`);
  }

  async getCourseAverageMarks(courseId: number): Promise<number> {
    return this.request<number>(`/results/course/${courseId}/average`);
  }
}

export default new ApiService();