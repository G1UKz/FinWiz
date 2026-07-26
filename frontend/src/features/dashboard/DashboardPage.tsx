import { useAuthStore } from '@/features/auth/store';
import { useNavigate } from 'react-router-dom';

export default function DashboardPage() {
  const { user, logout } = useAuthStore();
  const navigate = useNavigate();

  return (
    <div className="max-w-4xl mx-auto p-6">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-2xl font-bold">FinWiz</h1>
        <div className="flex items-center gap-4">
          <span className="text-sm text-gray-600">{user?.email}</span>
          <button
            onClick={() => {
              logout();
              navigate('/login');
            }}
            className="text-sm text-red-600 hover:underline"
          >
            Выйти
          </button>
        </div>
      </div>

      <div className="grid grid-cols-3 gap-4">
        <div className="bg-white p-6 rounded-xl shadow-sm border">
          <p className="text-sm text-gray-500">Баланс</p>
          <p className="text-2xl font-bold mt-1">0 ₽</p>
        </div>
        <div className="bg-white p-6 rounded-xl shadow-sm border">
          <p className="text-sm text-gray-500">Доходы</p>
          <p className="text-2xl font-bold text-green-600 mt-1">0 ₽</p>
        </div>
        <div className="bg-white p-6 rounded-xl shadow-sm border">
          <p className="text-sm text-gray-500">Расходы</p>
          <p className="text-2xl font-bold text-red-600 mt-1">0 ₽</p>
        </div>
      </div>
    </div>
  );
}
