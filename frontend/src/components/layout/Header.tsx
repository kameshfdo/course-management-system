import React from 'react';

const Header: React.FC = () => {
  return (
    <header className="bg-white shadow-sm border-b">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          <h1 className="text-2xl font-bold text-gray-900">
            University Course Management System
          </h1>
          <div className="text-sm text-gray-500">
            Modern Enterprise Application
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;