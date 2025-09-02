import React, { useState, useEffect } from 'react';
import { X } from 'lucide-react';
import { Result, Registration } from '../../types';
import apiService from '../../services/api';

interface ResultFormProps {
  result?: Result;
  onSave: () => void;
  onCancel: () => void;
}

const ResultForm: React.FC<ResultFormProps> = ({ result, onSave, onCancel }) => {
  const [formData, setFormData] = useState<Result>({
    registrationId: result?.registrationId || 0,
    marks: result?.marks || 0,
    feedback: result?.feedback || ''
  });

  const [registrations, setRegistrations] = useState<Registration[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchRegistrations = async () => {
      try {
        const registrationsData = await apiService.getRegistrations();
        setRegistrations(registrationsData);
      } catch (error) {
        console.error('Error fetching registrations:', error);
      }
    };

    fetchRegistrations();
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      if (result?.id) {
        await apiService.updateResult(result.id, formData);
      } else {
        await apiService.createResult(formData);
      }
      onSave();
    } catch (error) {
      setError('Failed to save result. Please try again.');
      console.error('Error saving result:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (field: keyof Result, value: string | number) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg p-6 w-full max-w-md">
        <div className="flex justify-between items-center mb-4">
          <h3 className="text-lg font-semibold">
            {result ? 'Edit Result' : 'Add New Result'}
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
            <label className="block text-sm font-medium mb-1">Registration</label>
            <select
              value={formData.registrationId}
              onChange={(e) => handleChange('registrationId', parseInt(e.target.value))}
              className="w-full p-2 border rounded-md"
              required
            >
              <option value={0}>Select Registration</option>
              {registrations.map(reg => (
                <option key={reg.id} value={reg.id}>
                  {reg.studentName} - {reg.courseCode}
                </option>
              ))}
            </select>
          </div>
          
          <div>
            <label className="block text-sm font-medium mb-1">Marks (0-100)</label>
            <input
              type="number"
              value={formData.marks}
              onChange={(e) => handleChange('marks', parseFloat(e.target.value))}
              className="w-full p-2 border rounded-md"
              min="0"
              max="100"
              step="0.01"
              required
            />
          </div>
          
          <div>
            <label className="block text-sm font-medium mb-1">Feedback</label>
            <textarea
              value={formData.feedback || ''}
              onChange={(e) => handleChange('feedback', e.target.value)}
              className="w-full p-2 border rounded-md h-20"
            />
          </div>
          
          <div className="flex gap-2 pt-4">
            <button
              type="submit"
              disabled={loading}
              className="flex-1 bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 disabled:opacity-50"
            >
              {loading ? 'Saving...' : (result ? 'Update' : 'Create')}
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

export default ResultForm;