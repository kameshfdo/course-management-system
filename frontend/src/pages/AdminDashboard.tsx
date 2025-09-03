import React, { useState, useEffect } from 'react';
import { Student, Course, Registration, Result } from '../types';
import apiService from '../services/api';
import { useAuth } from '../contexts/AuthContext';
import { LogOut } from 'lucide-react';

// Components
import Header from '../components/layout/Header';
import Navigation from '../components/layout/Navigation';
import SummaryCards from '../components/layout/SummaryCards';
import SearchBar from '../components/common/SearchBar';
import LoadingSpinner from '../components/common/LoadingSpinner';

// Table Components
import StudentsTable from '../components/tables/StudentsTable';
import CoursesTable from '../components/tables/CoursesTable';
import RegistrationsTable from '../components/tables/RegistrationsTable';
import ResultsTable from '../components/tables/ResultsTable';

// Form Components
import StudentForm from '../components/forms/StudentForm';
import CourseForm from '../components/forms/CourseForm';
import RegistrationForm from '../components/forms/RegistrationForm';
import ResultForm from '../components/forms/ResultForm';

const AdminDashboard: React.FC = () => {
  const { logout } = useAuth();
  const [activeTab, setActiveTab] = useState<string>('students');
  const [students, setStudents] = useState<Student[]>([]);
  const [courses, setCourses] = useState<Course[]>([]);
  const [registrations, setRegistrations] = useState<Registration[]>([]);
  const [results, setResults] = useState<Result[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [showForm, setShowForm] = useState<boolean>(false);
  const [editingItem, setEditingItem] = useState<Student | Course | Registration | Result | null>(null);
  const [searchTerm, setSearchTerm] = useState<string>('');
  const [showFilters, setShowFilters] = useState<boolean>(false);

  // Fetch data functions
  const fetchStudents = async (): Promise<void> => {
    try {
      setLoading(true);
      const data = await apiService.getStudents();
      setStudents(data);
    } catch (error) {
      console.error('Error fetching students:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchCourses = async (): Promise<void> => {
    try {
      setLoading(true);
      const data = await apiService.getCourses();
      setCourses(data);
    } catch (error) {
      console.error('Error fetching courses:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchRegistrations = async (): Promise<void> => {
    try {
      setLoading(true);
      const data = await apiService.getRegistrations();
      setRegistrations(data);
    } catch (error) {
      console.error('Error fetching registrations:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchResults = async (): Promise<void> => {
    try {
      setLoading(true);
      const data = await apiService.getResults();
      setResults(data);
    } catch (error) {
      console.error('Error fetching results:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    switch (activeTab) {
      case 'students':
        fetchStudents();
        break;
      case 'courses':
        fetchCourses();
        break;
      case 'registrations':
        fetchRegistrations();
        break;
      case 'results':
        fetchResults();
        break;
    }
  }, [activeTab]);

  // Delete handlers
  const handleDeleteStudent = async (id: number): Promise<void> => {
    if (window.confirm('Are you sure you want to delete this student?')) {
      try {
        await apiService.deleteStudent(id);
        fetchStudents();
      } catch (error) {
        console.error('Error deleting student:', error);
      }
    }
  };

  const handleDeleteCourse = async (id: number): Promise<void> => {
    if (window.confirm('Are you sure you want to delete this course?')) {
      try {
        await apiService.deleteCourse(id);
        fetchCourses();
      } catch (error) {
        console.error('Error deleting course:', error);
      }
    }
  };

  const handleDeleteRegistration = async (id: number): Promise<void> => {
    if (window.confirm('Are you sure you want to delete this registration?')) {
      try {
        await apiService.deleteRegistration(id);
        fetchRegistrations();
      } catch (error) {
        console.error('Error deleting registration:', error);
      }
    }
  };

  const handleDeleteResult = async (id: number): Promise<void> => {
    if (window.confirm('Are you sure you want to delete this result?')) {
      try {
        await apiService.deleteResult(id);
        fetchResults();
      } catch (error) {
        console.error('Error deleting result:', error);
      }
    }
  };

  // Filter data based on search term
  const getFilteredStudents = (): Student[] => {
    if (!searchTerm) return students;
    return students.filter(student => 
      student.firstName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      student.lastName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      student.studentId?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      student.email?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      student.department?.toLowerCase().includes(searchTerm.toLowerCase())
    );
  };

  const getFilteredCourses = (): Course[] => {
    if (!searchTerm) return courses;
    return courses.filter(course => 
      course.title?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      course.code?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      course.department?.toLowerCase().includes(searchTerm.toLowerCase())
    );
  };

  const getFilteredRegistrations = (): Registration[] => {
    if (!searchTerm) return registrations;
    return registrations.filter(registration => 
      registration.studentName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      registration.courseCode?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      registration.courseTitle?.toLowerCase().includes(searchTerm.toLowerCase())
    );
  };

  const getFilteredResults = (): Result[] => {
    if (!searchTerm) return results;
    return results.filter(result => 
      result.studentName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      result.courseCode?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      result.grade?.toLowerCase().includes(searchTerm.toLowerCase())
    );
  };

  const handleFormSave = (): void => {
    setShowForm(false);
    setEditingItem(null);
    // Refresh current tab data
    switch (activeTab) {
      case 'students': fetchStudents(); break;
      case 'courses': fetchCourses(); break;
      case 'registrations': fetchRegistrations(); break;
      case 'results': fetchResults(); break;
    }
  };

  const handleFormCancel = (): void => {
    setShowForm(false);
    setEditingItem(null);
  };

  const handleEdit = (item: Student | Course | Registration | Result): void => {
    setEditingItem(item);
    setShowForm(true);
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <h1 className="text-2xl font-bold text-gray-900">
              University Course Management System
            </h1>
            <div className="flex items-center space-x-4">
              <span className="text-sm text-gray-500">Admin Dashboard</span>
              <button
                onClick={logout}
                className="flex items-center px-3 py-2 text-sm text-gray-700 hover:text-gray-900"
              >
                <LogOut size={18} className="mr-2" />
                Logout
              </button>
            </div>
          </div>
        </div>
      </header>
      
      <Navigation activeTab={activeTab} setActiveTab={setActiveTab} />

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <SearchBar
          searchTerm={searchTerm}
          setSearchTerm={setSearchTerm}
          activeTab={activeTab}
          showFilters={showFilters}
          setShowFilters={setShowFilters}
          onAddClick={() => setShowForm(true)}
        />

        {loading ? (
          <LoadingSpinner />
        ) : (
          <div className="bg-white rounded-lg shadow overflow-hidden">
            {activeTab === 'students' && (
              <StudentsTable
                students={getFilteredStudents()}
                onEdit={(student) => handleEdit(student)}
                onDelete={handleDeleteStudent}
              />
            )}

            {activeTab === 'courses' && (
              <CoursesTable
                courses={getFilteredCourses()}
                onEdit={(course) => handleEdit(course)}
                onDelete={handleDeleteCourse}
              />
            )}

            {activeTab === 'registrations' && (
              <RegistrationsTable
                registrations={getFilteredRegistrations()}
                onEdit={(registration) => handleEdit(registration)}
                onDelete={handleDeleteRegistration}
              />
            )}

            {activeTab === 'results' && (
              <ResultsTable
                results={getFilteredResults()}
                onEdit={(result) => handleEdit(result)}
                onDelete={handleDeleteResult}
              />
            )}
          </div>
        )}

        <SummaryCards
          students={students}
          courses={courses}
          registrations={registrations}
          results={results}
        />
      </main>

      {/* Forms */}
      {showForm && (
        <>
          {activeTab === 'students' && (
            <StudentForm
              student={editingItem as Student}
              onSave={handleFormSave}
              onCancel={handleFormCancel}
            />
          )}
          
          {activeTab === 'courses' && (
            <CourseForm
              course={editingItem as Course}
              onSave={handleFormSave}
              onCancel={handleFormCancel}
            />
          )}
          
          {activeTab === 'registrations' && (
            <RegistrationForm
              registration={editingItem as Registration}
              onSave={handleFormSave}
              onCancel={handleFormCancel}
            />
          )}
          
          {activeTab === 'results' && (
            <ResultForm
              result={editingItem as Result}
              onSave={handleFormSave}
              onCancel={handleFormCancel}
            />
          )}
        </>
      )}
    </div>
  );
};

export default AdminDashboard;
