import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useMutation } from '@tanstack/react-query';
import { useState } from 'react';
import { useCreateAccount } from './hooks';

const accountSchema = z.object({
  name: z.string().min(1, 'Введите название'),
  bankName: z.string().optional(),
  accountType: z.enum(['DEBIT', 'CREDIT', 'SAVINGS', 'CASH']),
  currencyCode: z.string().min(1, 'Выберите валюту'),
  initialBalance: z.coerce.number().min(0, 'Не может быть меньше 0'),
});

type AccountFormData = z.infer<typeof accountSchema>;

export default function AccountForm() {
  const [isOpen, setIsOpen] = useState(false);
  const create = useCreateAccount();

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<AccountFormData>({
    resolver: zodResolver(accountSchema),
    defaultValues: {
      accountType: 'DEBIT',
      currencyCode: 'RUB',
      initialBalance: 0,
    },
  });

  const onSubmit = (data: AccountFormData) => {
    create.mutate(
      {
        name: data.name,
        bankName: data.bankName || null,
        accountType: data.accountType,
        currencyCode: data.currencyCode,
        initialBalanceMinor: Math.round(data.initialBalance * 100),
      },
      { onSuccess: () => { reset(); setIsOpen(false); } }
    );
  };

  if (!isOpen) {
    return (
      <button
        onClick={() => setIsOpen(true)}
        className="w-full py-3 border-2 border-dashed border-gray-300 rounded-xl text-gray-500 hover:border-blue-400 hover:text-blue-600 transition-colors"
      >
        + Добавить счёт
      </button>
    );
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="bg-white p-5 rounded-xl shadow-sm border space-y-3">
      <h3 className="font-semibold text-gray-900">Новый счёт</h3>

      <div>
        <label className="block text-sm font-medium mb-1">Название</label>
        <input
          {...register('name')}
          className="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500"
          placeholder="Тинькофф Black"
        />
        {errors.name && <p className="text-red-500 text-xs mt-1">{errors.name.message}</p>}
      </div>

      <div>
        <label className="block text-sm font-medium mb-1">Банк</label>
        <input
          {...register('bankName')}
          className="w-full px-3 py-2 border rounded-lg"
          placeholder="Тинькофф"
        />
      </div>

      <div className="grid grid-cols-2 gap-3">
        <div>
          <label className="block text-sm font-medium mb-1">Тип</label>
          <select {...register('accountType')} className="w-full px-3 py-2 border rounded-lg">
            <option value="DEBIT">Дебетовый</option>
            <option value="CREDIT">Кредитный</option>
            <option value="SAVINGS">Накопительный</option>
            <option value="CASH">Наличные</option>
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium mb-1">Валюта</label>
          <select {...register('currencyCode')} className="w-full px-3 py-2 border rounded-lg">
            <option value="RUB">₽ RUB</option>
            <option value="USD">$ USD</option>
            <option value="EUR">€ EUR</option>
          </select>
        </div>
      </div>

      <div>
        <label className="block text-sm font-medium mb-1">Начальный баланс, ₽</label>
        <input
          type="number"
          step="0.01"
          {...register('initialBalance')}
          className="w-full px-3 py-2 border rounded-lg"
          placeholder="0"
        />
      </div>

      {create.isError && <p className="text-red-500 text-sm">Ошибка создания</p>}

      <div className="flex gap-2">
        <button
          type="button"
          onClick={() => setIsOpen(false)}
          className="flex-1 py-2 border rounded-lg hover:bg-gray-50"
        >
          Отмена
        </button>
        <button
          type="submit"
          disabled={create.isPending}
          className="flex-1 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:opacity-50"
        >
          {create.isPending ? 'Создание...' : 'Создать'}
        </button>
      </div>
    </form>
  );
}
