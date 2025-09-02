import React from 'react';
import { Search, Filter, Plus } from 'lucide-react';

interface SearchBarProps {
  searchTerm: string;
  setSearchTerm: (term: string) => void;
  activeTab: string;
  showFilters: boolean;
  setShowFilters: (show: boolean) => void;
  onAddClick: () => void;
}

const SearchBar: React.FC<SearchBarProps> = ({ 
  searchTerm, 
  setSearchTerm, 
  activeTab, 
  showFilters, 
  setShowFilters, 
  onAddClick 
}) => {
  return (
    <div className="flex justify-between items-center mb-6">
      <div className="flex items-center space-x-4">
        <div className="relative">
          <Search size={20} className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
          <input
            type="text"
            placeholder={`Search ${activeTab}...`}
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="pl-10 pr-4 py-2 border rounded-lg w-64"
          />
        </div>
        <button
          onClick={() => setShowFilters(!showFilters)}
          className="flex items-center px-3 py-2 border rounded-lg hover:bg-gray-50"
        >
          <Filter size={18} className="mr-2" />
          Filters
        </button>
      </div>
      
      <button
        onClick={onAddClick}
        className="flex items-center px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
      >
        <Plus size={18} className="mr-2" />
        Add {activeTab.slice(0, -1)}
      </button>
    </div>
  );
};

export default SearchBar;