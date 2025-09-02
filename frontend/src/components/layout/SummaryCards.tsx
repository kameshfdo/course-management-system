import React from 'react';
import { Users, BookOpen, ClipboardList, Award } from 'lucide-react';
import { Student, Course, Registration, Result } from '../../types';

interface SummaryCardsProps {
  students: Student[];
  courses: Course[];
  registrations: Registration[];
  results: Result[];
}

interface CardData {
  title: string;
  value: number;
  icon: React.ComponentType<any>;
  color: string;
}

const SummaryCards: React.FC<SummaryCardsProps> = ({ 
  students, 
  courses, 
  registrations, 
  results 
}) => {
  const cards: CardData[] = [
    {
      title: 'Total Students',
      value: students.length,
      icon: Users,
      color: 'text-blue-600'
    },
    {
      title: 'Total Courses',
      value: courses.length,
      icon: BookOpen,
      color: 'text-green-600'
    },
    {
      title: 'Active Registrations',
      value: registrations.filter(r => r.status === 'ENROLLED').length,
      icon: ClipboardList,
      color: 'text-yellow-600'
    },
    {
      title: 'Results Recorded',
      value: results.length,
      icon: Award,
      color: 'text-purple-600'
    }
  ];

  return (
    <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mt-8">
      {cards.map((card, index) => {
        const Icon = card.icon;
        return (
          <div key={index} className="bg-white p-6 rounded-lg shadow">
            <div className="flex items-center">
              <Icon className={`h-8 w-8 ${card.color}`} />
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-500">{card.title}</p>
                <p className="text-2xl font-semibold text-gray-900">{card.value}</p>
              </div>
            </div>
          </div>
        );
      })}
    </div>
  );
};

export default SummaryCards;