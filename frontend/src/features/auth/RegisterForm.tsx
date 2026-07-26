import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useMutation } from '@tanstack/react-query';
import { useNavigate, Link } from 'react-router-dom';
import { registerApi } from './api';
import { useAuthStore } from './store';
import type { SubmitHandler } from 'react-hook-form';

const registerSchema = z.object({
  name: z.string().min(2, 'Минимум 2 символа'),
  email: z.string().email('Введите корректный email'),
  password: z.string().min(4, 'Минимум 4 символа'),
});

type RegisterFormData = z.infer<typeof registerSchema>;

export default function RegisterForm() {
  const navigate = useNavigate();
  const login = useAuthStore((s) => s.login);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema),
  });

  const mutation = useMutation({
    mutationFn: registerApi,
    onSuccess: (data) => {
      login(data.userId, data.accessToken);
      navigate('/dashboard');
    },
  });

  const onSubmit: SubmitHandler<RegisterFormData> = (data) => {
    mutation.mutate({ ...data });
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <h1 className="text-2xl font-bold text-center">FinWiz</h1>
      <p className="text-center text-gray-500 text-sm">Создать аккаунт</p>

      <div>
        <label className="block text-sm font-medium mb-1">Имя</label>
        <input
          type="text"
          {...register('name')}
          className="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500"
          placeholder="Иван"
        />
        {errors.name && <p className="text-red-500 text-sm mt-1">{errors.name.message}</p>}
      </div>

      <div>
        <label className="block text-sm font-medium mb-1">Email</label>
        <input
          type="email"
          {...register('email')}
          className="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500"
          placeholder="ivan@finwiz.dev"
        />
        {errors.email && <p className="text-red-500 text-sm mt-1">{errors.email.message}</p>}
      </div>

      <div>
        <label className="block text-sm font-medium mb-1">Пароль</label>
        <input
          type="password"
          {...register('password')}
          className="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500"
          placeholder="••••••"
        />
        {errors.password && <p className="text-red-500 text-sm mt-1">{errors.password.message}</p>}
      </div>

      {mutation.isError && (
        <p className="text-red-500 text-sm text-center">Ошибка регистрации (возможно, email занят)</p>
      )}

      <button
        type="submit"
        disabled={mutation.isPending}
        className="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 disabled:opacity-50"
      >
        {mutation.isPending ? 'Регистрация...' : 'Зарегистрироваться'}
      </button>

      <p className="text-center text-sm text-gray-500">
        Уже есть аккаунт?{' '}
        <Link to="/login" className="text-blue-600 hover:underline">
          Войти
        </Link>
      </p>
    </form>
  );
}
