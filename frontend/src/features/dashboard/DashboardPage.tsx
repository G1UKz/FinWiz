import { useAuthStore } from '@/features/auth/store';
import { useAccounts } from '@/features/accounts/hooks';
import { useTransactions } from '@/features/transactions/hooks';
import TransactionForm from '@/features/transactions/TransactionForm';
import { useNavigate } from 'react-router-dom';

export default function DashboardPage() {
  const navigate = useNavigate();
  const { logout } = useAuthStore();
  const { data: accounts, isLoading: accountsLoading } = useAccounts();
  const { data: transactions, isLoading: txLoading } = useTransactions();

  const totalBalance = accounts?.reduce((sum, acc) => sum + (acc.balance || 0), 0) ?? 0;

  return (
    <div className="max-w-5xl mx-auto p-6">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-2xl font-bold text-gray-900">FinWiz</h1>
        <button
          onClick={() => {
            logout();
            navigate('/login');
          }}
          className="text-sm text-red-600 hover:text-red-700 font-medium"
        >
          Выйти
        </button>
      </div>

      <div className="bg-white p-6 rounded-xl shadow-sm border mb-6">
        <p className="text-sm text-gray-500 font-medium">Общий баланс</p>
        <p className="text-3xl font-bold text-gray-900 mt-1">
          {totalBalance.toLocaleString('ru-RU')} ₽
        </p>
      </div>

      <h2 className="text-lg font-semibold text-gray-800 mb-4">Мои счета</h2>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 mb-8">
        {accountsLoading && <p className="text-gray-500 col-span-full">Загрузка...</p>}
        {accounts?.map((acc) => (
          <div key={acc.id} className="bg-white p-5 rounded-xl shadow-sm border">
            <div className="flex justify-between items-start mb-2">
              <p className="font-semibold text-gray-900">{acc.name}</p>
              <span className="text-xs px-2 py-1 bg-gray-100 rounded text-gray-600">
                {acc.currencyCode}
              </span>
            </div>
            <p className="text-xs text-gray-500 mb-3">{acc.bankName || 'Без банка'}</p>
            <p className="text-xl font-bold text-gray-900">
              {(acc.balance ?? 0).toLocaleString('ru-RU')} ₽
            </p>
          </div>
        ))}
        {accounts?.length === 0 && (
          <p className="text-gray-400 col-span-full">Нет счетов</p>
        )}
      </div>

      <h2 className="text-lg font-semibold text-gray-800 mb-4">Последние операции</h2>
      <TransactionForm />
      <div className="bg-white rounded-xl shadow-sm border overflow-hidden">
        {txLoading && <p className="p-4 text-gray-500">Загрузка...</p>}
        {transactions && transactions.length > 0 ? (
          <table className="w-full text-left text-sm">
            <thead className="bg-gray-50 border-b">
              <tr>
                <th className="px-4 py-3 font-medium text-gray-600">Дата</th>
                <th className="px-4 py-3 font-medium text-gray-600">Категория</th>
                <th className="px-4 py-3 font-medium text-gray-600">Счёт</th>
                <th className="px-4 py-3 font-medium text-gray-600 text-right">Сумма</th>
              </tr>
            </thead>
            <tbody>
              {transactions.slice(0, 10).map((tx) => (
                <tr key={tx.id} className="border-b last:border-0 hover:bg-gray-50">
                  <td className="px-4 py-3 text-gray-900">{tx.transactionDate}</td>
                  <td className="px-4 py-3 text-gray-900">{tx.categoryName}</td>
                  <td className="px-4 py-3 text-gray-500">{tx.bankAccountName}</td>
                  <td className={`px-4 py-3 text-right font-medium ${
                    tx.type === 'INCOME' ? 'text-green-600' : 'text-red-600'
                  }`}>
                    {tx.type === 'INCOME' ? '+' : '-'}{(tx.amount ?? 0).toLocaleString('ru-RU')} ₽
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <p className="p-4 text-gray-400">Нет операций</p>
        )}
      </div>
    </div>
  );
}
