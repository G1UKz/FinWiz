import { useQuery } from '@tanstack/react-query';
import { useAuthStore } from '@/features/auth/store';
import { fetchAccounts } from './api';

export function useAccounts() {
  const userId = useAuthStore((s) => s.userId);
  return useQuery({
    queryKey: ['accounts', userId],
    queryFn: () => fetchAccounts(userId!),
    enabled: !!userId,
  });
}
