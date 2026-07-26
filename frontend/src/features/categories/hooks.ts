import { useQuery } from '@tanstack/react-query';
import { useAuthStore } from '@/features/auth/store';
import { fetchCategories } from './api';

export function useCategories() {
  const userId = useAuthStore((s) => s.userId);
  return useQuery({
    queryKey: ['categories', userId],
    queryFn: () => fetchCategories(userId!),
    enabled: !!userId,
  });
}
