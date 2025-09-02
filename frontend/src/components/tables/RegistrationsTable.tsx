import React from 'react';
import { Edit2, Trash2 } from 'lucide-react';
import { Registration } from '../../types';
import { STATUS_COLORS } from '../../utils/constants';

interface RegistrationsTableProps {
  registrations: Registration[];
  onEdit: (registration: Registration) => void;
  onDelete: (id: number) => void;
}

const RegistrationsTable: React.FC<RegistrationsTableProps> = ({ 
  registrations, 
  onEdit, 
  onDelete 
}) => {
  if (registrations.length === 0) {
    return (
      <div className="text-center py-12">
        <div className="text-gray-400 text-lg mb-2">No registrations found</div>
        <p className="text-gray-500">Add your first registration to get started</p>
      </div>
    );
  }

  return (
    <div className="overflow-x-auto">
      <table className="min-w-full divide-y divide-gray-200">
        <thead className="bg-gray-50">
          <tr>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Student
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Course
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Date
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Status
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Actions
            </th>
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-gray-200">
          {registrations.map((registration) => (
            <tr key={registration.id} className="hover:bg-gray-50">
              <td className="px-6 py-4 whitespace-nowrap">
                <div className="text-sm font-medium text-gray-900">
                  {registration.studentName}
                </div>
              </td>
              <td className="px-6 py-4 whitespace-nowrap">
                <div>
                  <div className="text-sm font-medium text-gray-900">{registration.courseTitle}</div>
                  <div className="text-sm text-gray-500">{registration.courseCode}</div>
                </div>
              </td>
              <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                {registration.registrationDate ? 
                  new Date(registration.registrationDate).toLocaleDateString() : 
                  'N/A'
                }
              </td>
              <td className="px-6 py-4 whitespace-nowrap">
                <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                  STATUS_COLORS[registration.status] || 'bg-gray-100 text-gray-800'
                }`}>
                  {registration.status}
                </span>
              </td>
              <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                <div className="flex space-x-2">
                  <button
                    onClick={() => onEdit(registration)}
                    className="text-blue-600 hover:text-blue-900"
                  >
                    <Edit2 size={16} />
                  </button>
                  <button
                    onClick={() => registration.id && onDelete(registration.id)}
                    className="text-red-600 hover:text-red-900"
                  >
                    <Trash2 size={16} />
                  </button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default RegistrationsTable;