import React, { useState } from 'react';
import { X } from 'lucide-react';
import { Course } from '../../types';
import apiService from '../../services/api';

interface CourseFormProps {
  course?: Course;
  onSave: () => void;
  onCancel: () => void;
}

const CourseForm: React.FC<CourseFormProps> = ({ course, onSave, onCancel }) => {
  const [formData, setFormData] = useState<Course>({
    code: course?.code || '',
    title: course?.title || '',
    description: course?.description || '',
    credits: course?.credits || 3,
    department: course?.department || '',
    maxEnrollment: course?.maxEnrollment || 50
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      if (course?.id) {
        await apiService.updateCourse(course.id, formData);
      } else {
        await apiService.createCourse(formData);
      }
      onSave();
    } catch (error) {
      setError('Failed to save course. Please try again.');
      console.error('Error saving course:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (field: keyof Course, value: string | number) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg p-6 w-full max-w-md">
        <div className="flex justify-between items-center mb-4">
          <h3 className="text-lg font-semibold">
            {course ? 'Edit Course' : 'Add New Course'}
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
            <label className="block text-sm font-medium mb-1">Course Code</label>
            <input
              type="text"
              value={formData.code}
              onChange={(e) => handleChange('code', e.target.value)}
              className="w-full p-2 border rounded-md"
              required
            />
          </div>
          
          <div>
            <label className="block text-sm font-medium mb-1">Title</label>
            <input
              type="text"
              value={formData.title}
              onChange={(e) => handleChange('title', e.target.value)}
              className="w-full p-2 border rounded-md"
              required
            />
          </div>
          
          <div>
            <label className="block text-sm font-medium mb-1">Description</label>
            <textarea
              value={formData.description || ''}
              onChange={(e) => handleChange('description', e.target.value)}
              className="w-full p-2 border rounded-md h-20"
            />
          </div>
          
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium mb-1">Credits</label>
              <input
                type="number"
                value={formData.credits}
                onChange={(e) => handleChange('credits', parseInt(e.target.value))}
                className="w-full p-2 border rounded-md"
                min="1"
                max="6"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">Max Enrollment</label>
              <input
                type="number"
                value={formData.maxEnrollment || ''}
                onChange={(e) => handleChange('maxEnrollment', parseInt(e.target.value))}
                className="w-full p-2 border rounded-md"
                min="1"
              />
            </div>
          </div>
          
          <div>
            <label className="block text-sm font-medium mb-1">Department</label>
            <input
              type="text"
              value={formData.department}
              onChange={(e) => handleChange('department', e.target.value)}
              className="w-full p-2 border rounded-md"
              required
            />
          </div>
          
          <div className="flex gap-2 pt-4">
            <button
              type="submit"
              disabled={loading}
              className="flex-1 bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 disabled:opacity-50"
            >
              {loading ? 'Saving...' : (course ? 'Update' : 'Create')}
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

export default CourseForm;