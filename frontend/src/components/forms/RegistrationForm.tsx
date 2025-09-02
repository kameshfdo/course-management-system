import React, { useState, useEffect } from 'react';
import { X } from 'lucide-react';
import { Registration, Student, Course, RegistrationStatus } from '../../types';
import apiService from '../../services/api';

interface RegistrationFormProps {
  registration?: Registration;
  onSave: () => void;
  onCancel: () => void;
}

const RegistrationForm: React.FC<RegistrationFormProps> = ({ registration, onSave, onCancel }) => {
  const [formData, setFormData] = useState<Registration>({
    studentId: registration?.studentId || 0,
    courseId: registration?.courseId || 0,
    status: registration?.status || RegistrationStatus.ENROLLED,
    remarks: registration?.remarks || ''
  });

  const [students, setStudents] = useState<Student[]>([]);
  const [courses, setCourses] = useState<Course[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [studentsData, coursesData] = await Promise.all([
          apiService.getStudents(),
          apiService.getCourses()
        ]);
        setStudents(studentsData);
        setCourses(coursesData);
      } catch (error) {
        console.error('Error fetching form data:', error);
      }
    };

    fetchData();
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      if (registration?.id) {
        await apiService.updateRegistration(registration.id, formData);
      } else {
        await apiService.createRegistration(formData);
      }
      onSave();
    } catch (error) {
      setError('Failed to save registration. Please try again.');
      console.error('Error saving registration:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (field: keyof Registration, value: string | number | RegistrationStatus) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg p-6 w-full max-w-md">
        <div className="flex justify-between items-center mb-4">
          <h3 className="text-lg font-semibold">
            {registration ? 'Edit Registration' : 'New Registration'}
          </h3>
          <button onClick={onCancel} className="text-gray-500 hover:text-gray-700">
            <X size={20} />
          </button>
        </div>
        
        {error && (
          <div className="mb-4 p-3 bg-red-100 border border-red-400 text-red-700 rounded">
            {error}
          </div>
        )}
        
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium mb-1">Student</label>
            <select
              value={formData.studentId}
              onChange={(e) => handleChange('studentId', parseInt(e.target.value))}
              className="w-full p-2 border rounded-md"
              required
            >
              <option value={0}>Select Student</option>
              {students.map(student => (
                <option key={student.id} value={student.id}>
                  {student.studentId} - {student.firstName} {student.lastName}
                </option>
              ))}
            </select>
          </div>
          
          <div>
            <label className="block text-sm font-medium mb-1">Course</label>
            <select
              value={formData.courseId}
              onChange={(e) => handleChange('courseId', parseInt(e.target.value))}
              className="w-full p-2 border rounded-md"
              required
            >
              <option value={0}>Select Course</option>
              {courses.map(course => (
                <option key={course.id} value={course.id}>
                  {course.code} - {course.title}
                </option>
              ))}
            </select>
          </div>
          
          <div>
            <label className="block text-sm font-medium mb-1">Status</label>
            <select
              value={formData.status}
              onChange={(e) => handleChange('status', e.target.value as RegistrationStatus)}
              className="w-full p-2 border rounded-md"
              required
            >
              <option value={RegistrationStatus.ENROLLED}>Enrolled</option>
              <option value={RegistrationStatus.DROPPED}>Dropped</option>
              <option value={RegistrationStatus.COMPLETED}>Completed</option>
            </select>
          </div>
          
          <div>
            <label className="block text-sm font-medium mb-1">Remarks</label>
            <textarea
              value={formData.remarks || ''}
              onChange={(e) => handleChange('remarks', e.target.value)}
              className="w-full p-2 border rounded-md h-20"
            />
          </div>
          
          <div className="flex gap-2 pt-4">
            <button
              type="submit"
              disabled={loading}
              className="flex-1 bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 disabled:opacity-50"
            >
              {loading ? 'Saving...' : (registration ? 'Update' : 'Create')}
            </button>
            <button
              type="button"
              onClick={onCancel}
              className="flex-1 bg-gray-300 text-gray-700 py-2 px-4 rounded-md hover:bg-gray-400"
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default RegistrationForm;