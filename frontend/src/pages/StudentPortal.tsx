import React, { useState, useEffect } from 'react';
import { BookOpen, ClipboardList, Award, LogOut, User } from 'lucide-react';
import { Course, Registration, Result } from '../types';
import { useAuth } from '../contexts/AuthContext';
import apiService from '../services/api';
import LoadingSpinner from '../components/common/LoadingSpinner';

const StudentPortal: React.FC = () => {
  const [activeTab, setActiveTab] = useState<'available' | 'enrolled' | 'results'>('enrolled');
  const [availableCourses, setAvailableCourses] = useState<Course[]>([]);
  const [enrolledCourses, setEnrolledCourses] = useState<Course[]>([]);
  const [results, setResults] = useState<Result[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  
  const { user, logout } = useAuth();

  useEffect(() => {
    fetchData();
  }, [activeTab]);

  const fetchData = async () => {
    setLoading(true);
    setError(null);
    
    try {
      switch (activeTab) {
        case 'available':
          const available = await apiService.getAvailableCoursesForStudent();
          setAvailableCourses(available);
          break;
        case 'enrolled':
          const enrolled = await apiService.getEnrolledCoursesForStudent();
          setEnrolledCourses(enrolled);
          break;
        case 'results':
          const studentResults = await apiService.getStudentResults();
          setResults(studentResults);
          break;
      }
    } catch (err) {
      setError('Failed to fetch data');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleEnroll = async (courseId: number) => {
    try {
      await apiService.enrollInCourse(courseId);
      await fetchData();
      setActiveTab('enrolled');
    } catch (err) {
      setError('Failed to enroll in course');
    }
  };

  const handleUnenroll = async (courseId: number) => {
    if (window.confirm('Are you sure you want to unenroll from this course?')) {
      try {
        await apiService.unenrollFromCourse(courseId);
        await fetchData();
      } catch (err) {
        setError('Failed to unenroll from course');
      }
    }
  };

  const calculateGPA = () => {
    if (results.length === 0) return 'N/A';
    const totalPoints = results.reduce((sum, result) => sum + (result.gpaPoints || 0), 0);
    return (totalPoints / results.length).toFixed(2);
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center">
              <h1 className="text-2xl font-bold text-gray-900">Student Portal</h1>
              <span className="ml-4 text-sm text-gray-500">
                Welcome, {user?.username}
              </span>
            </div>
            <button
              onClick={logout}
              className="flex items-center px-4 py-2 text-sm text-gray-700 hover:text-gray-900"
            >
              <LogOut size={18} className="mr-2" />
              Logout
            </button>
          </div>
        </div>
      </header>

      {/* Student Info Card */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 mt-6">
        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center">
            <User size={48} className="text-blue-600 mr-4" />
            <div>
              <h2 className="text-xl font-semibold">{user?.username}</h2>
              <p className="text-gray-600">{user?.email}</p>
              <p className="text-sm text-gray-500">Current GPA: {calculateGPA()}</p>
            </div>
          </div>
        </div>
      </div>

      {/* Navigation Tabs */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 mt-6">
        <div className="border-b border-gray-200">
          <nav className="-mb-px flex space-x-8">
            <button
              onClick={() => setActiveTab('enrolled')}
              className={`py-2 px-1 border-b-2 font-medium text-sm ${
                activeTab === 'enrolled'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              <BookOpen size={18} className="inline mr-2" />
              My Courses
            </button>
            <button
              onClick={() => setActiveTab('available')}
              className={`py-2 px-1 border-b-2 font-medium text-sm ${
                activeTab === 'available'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              <ClipboardList size={18} className="inline mr-2" />
              Available Courses
            </button>
            <button
              onClick={() => setActiveTab('results')}
              className={`py-2 px-1 border-b-2 font-medium text-sm ${
                activeTab === 'results'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              <Award size={18} className="inline mr-2" />
              My Results
            </button>
          </nav>
        </div>
      </div>

      {/* Content */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 mt-6">
        {error && (
          <div className="mb-4 p-3 bg-red-100 border border-red-400 text-red-700 rounded">
            {error}
          </div>
        )}

        {loading ? (
          <LoadingSpinner />
        ) : (
          <div className="bg-white rounded-lg shadow">
            {activeTab === 'enrolled' && (
              <div className="p-6">
                <h3 className="text-lg font-semibold mb-4">My Enrolled Courses</h3>
                {enrolledCourses.length === 0 ? (
                  <p className="text-gray-500">You are not enrolled in any courses.</p>
                ) : (
                  <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                    {enrolledCourses.map(course => (
                      <div key={course.id} className="border rounded-lg p-4">
                        <h4 className="font-semibold">{course.title}</h4>
                        <p className="text-sm text-gray-600">{course.code}</p>
                        <p className="text-sm text-gray-500 mt-2">{course.description}</p>
                        <div className="mt-4 flex justify-between items-center">
                          <span className="text-sm text-gray-600">
                            Credits: {course.credits}
                          </span>
                          <button
                            onClick={() => course.id && handleUnenroll(course.id)}
                            className="text-red-600 hover:text-red-700 text-sm"
                          >
                            Unenroll
                          </button>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            )}

            {activeTab === 'available' && (
              <div className="p-6">
                <h3 className="text-lg font-semibold mb-4">Available Courses</h3>
                {availableCourses.length === 0 ? (
                  <p className="text-gray-500">No courses available for enrollment.</p>
                ) : (
                  <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                    {availableCourses.map(course => (
                      <div key={course.id} className="border rounded-lg p-4">
                        <h4 className="font-semibold">{course.title}</h4>
                        <p className="text-sm text-gray-600">{course.code}</p>
                        <p className="text-sm text-gray-500 mt-2">{course.description}</p>
                        <div className="mt-4 flex justify-between items-center">
                          <span className="text-sm text-gray-600">
                            Credits: {course.credits}
                          </span>
                          <button
                            onClick={() => course.id && handleEnroll(course.id)}
                            className="bg-blue-600 text-white px-3 py-1 rounded text-sm hover:bg-blue-700"
                          >
                            Enroll
                          </button>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            )}

            {activeTab === 'results' && (
              <div className="p-6">
                <h3 className="text-lg font-semibold mb-4">My Results</h3>
                {results.length === 0 ? (
                  <p className="text-gray-500">No results available.</p>
                ) : (
                  <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                      <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                          Course
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                          Marks
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                          Grade
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                          GPA Points
                        </th>
                      </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                      {results.map(result => (
                        <tr key={result.id}>
                          <td className="px-6 py-4 whitespace-nowrap">
                            <div>
                              <div className="text-sm font-medium text-gray-900">
                                {result.courseTitle}
                              </div>
                              <div className="text-sm text-gray-500">{result.courseCode}</div>
                            </div>
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                            {result.marks}%
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap">
                            <span className="px-2 py-1 text-xs font-semibold rounded-full bg-green-100 text-green-800">
                              {result.grade}
                            </span>
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                            {result.gpaPoints}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                )}
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default StudentPortal;