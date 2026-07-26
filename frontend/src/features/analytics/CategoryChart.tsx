import { useTransactions } from '@/features/transactions/hooks';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, Cell } from 'recharts';

export default function CategoryChart() {
  const { data: transactions } = useTransactions();

  const data = Object.entries(
    (transactions ?? [])
      .filter((t) => t.type === 'EXPENSE')
      .reduce<Record<string, number>>((acc, t) => {
        const name = t.categoryName || 'Прочее';
        acc[name] = (acc[name] || 0) + (t.amount || 0);
        return acc;
      }, {})
  )
    .map(([name, value]) => ({ name, value }))
    .sort((a, b) => b.value - a.value);

  const COLORS = ['#F44336', '#FF9800', '#795548', '#673AB7', '#E91E63', '#2196F3', '#4CAF50'];

  if (data.length === 0) return null;

  return (
    <div className="bg-white p-6 rounded-xl shadow-sm border mb-6">
      <h3 className="font-semibold text-gray-900 mb-4">Расходы по категориям</h3>
      <div className="h-64">
        <ResponsiveContainer width="100%" height="100%">
          <BarChart data={data} margin={{ top: 0, right: 0, left: 0, bottom: 0 }}>
            <XAxis dataKey="name" tick={{ fontSize: 12 }} angle={-30} textAnchor="end" height={60} />
            <YAxis tick={{ fontSize: 12 }} tickFormatter={(v) => `${v.toLocaleString('ru-RU')} ₽`} />
            <Tooltip formatter={(value: number) => `${value.toLocaleString('ru-RU')} ₽`} />
            <Bar dataKey="value" radius={[4, 4, 0, 0]}>
              {data.map((_, i) => (
                <Cell key={i} fill={COLORS[i % COLORS.length]} />
              ))}
            </Bar>
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}
