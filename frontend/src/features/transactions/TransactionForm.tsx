import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useMutation } from '@tanstack/react-query';
import { useAccounts } from '@/features/accounts/hooks';
import { useCategories } from '@/features/categories/hooks';
import { useCreateTransaction } from './hooks';

const transactionSchema = z.object({
  amount: z.coerce.number().positive('Сумма должна быть больше 0'),
  type: z.enum(['INCOME', 'EXPENSE']),
  categoryId: z.coerce.number().positive('Выберите категорию'),
  bankAccountId: z.coerce.number().positive('Выберите счёт'),
  description: z.string().optional(),
  transactionDate: z.string().min(1, 'Выберите дату'),
});

type TransactionFormData = z.infer<typeof transactionSchema>;

export default function TransactionForm() {
  const { data: accounts } = useAccounts();
  const { data: categories } = useCategories();
  const createTx = useCreateTransaction();

  const {
    register,
    handleSubmit,
    reset,
    watch,
    formState: { errors },
  } = useForm<TransactionFormData>({
    resolver: zodResolver(transactionSchema),
    defaultValues: {
      type: 'EXPENSE',
      transactionDate: new Date().toISOString().split('T')[0],
    },
  });

  const txType = watch('type');

  const filteredCategories = categories?.filter((c) => c.type === txType) ?? [];

  const onSubmit = (data: TransactionFormData) => {
    createTx.mutate(
      {
        bankAccountId: data.bankAccountId,
        categoryId: data.categoryId,
        type: data.type,
        amountMinor: Math.round(data.amount * 100), // рубли → копейки
        description: data.description || null,
        transactionDate: data.transactionDate,
      },
      { onSuccess: () => reset() }
    );
  };

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="bg-white p-5 rounded-xl shadow-sm border mb-6 space-y-3"
    >
      <h3 className="font-semibold text-gray-900">Новая операция</h3>

      <div className="grid grid-cols-2 gap-3">
        <div>
          <label className="block text-sm font-medium mb-1">Сумма, ₽</label>
          <input
            type="number"
            step="0.01"
            {...register('amount')}
            className="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500"
            placeholder="1000"
          />
          {errors.amount && <p className="text-red-500 text-xs mt-1">{errors.amount.message}</p>}
        </div>

        <div>
          <label className="block text-sm font-medium mb-1">Тип</label>
          <select {...register('type')} className="w-full px-3 py-2 border rounded-lg">
            <option value="EXPENSE">Расход</option>
            <option value="INCOME">Доход</option>
          </select>
        </div>
      </div>

      <div className="grid grid-cols-2 gap-3">
        <div>
          <label className="block text-sm font-medium mb-1">Категория</label>
          <select {...register('categoryId')} className="w-full px-3 py-2 border rounded-lg">
            <option value="">—</option>
            {filteredCategories.map((c) => (
              <option key={c.id} value={c.id}>
                {c.name}
              </option>
            ))}
          </select>
          {errors.categoryId && (
            <p className="text-red-500 text-xs mt-1">{errors.categoryId.message}</p>
          )}
        </div>

        <div>
          <label className="block text-sm font-medium mb-1">Счёт</label>
          <select {...register('bankAccountId')} className="w-full px-3 py-2 border rounded-lg">
            <option value="">—</option>
            {accounts?.map((a) => (
              <option key={a.id} value={a.id}>
                {a.name}
              </option>
            ))}
          </select>
          {errors.bankAccountId && (
            <p className="text-red-500 text-xs mt-1">{errors.bankAccountId.message}</p>
          )}
        </div>
      </div>

      <div>
        <label className="block text-sm font-medium mb-1">Описание</label>
        <input
          type="text"
          {...register('description')}
          className="w-full px-3 py-2 border rounded-lg"
          placeholder="Пятёрочка"
        />
      </div>

      <div>
        <label className="block text-sm font-medium mb-1">Дата</label>
        <input
          type="date"
          {...register('transactionDate')}
          className="w-full px-3 py-2 border rounded-lg"
        />
        {errors.transactionDate && (
          <p className="text-red-500 text-xs mt-1">{errors.transactionDate.message}</p>
        )}
      </div>

      {createTx.isError && (
        <p className="text-red-500 text-sm">Ошибка при создании</p>
      )}

      <button
        type="submit"
        disabled={createTx.isPending}
        className="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 disabled:opacity-50"
      >
        {createTx.isPending ? 'Сохранение...' : 'Добавить'}
      </button>
    </form>
  );
}
