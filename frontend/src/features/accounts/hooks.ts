import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useAuthStore } from '@/features/auth/store';
import { fetchAccounts, createAccount } from './api';
import type { CreateAccountRequest } from './api';

export function useAccounts() {
  const userId = useAuthStore((s) => s.userId);
  return useQuery({
    queryKey: ['accounts', userId],
    queryFn: () => fetchAccounts(userId!),
    enabled: !!userId,
  });
}

export function useCreateAccount() {
  const queryClient = useQueryClient();
  const userId = useAuthStore((s) => s.userId);

  return useMutation({
    mutationFn: (data: CreateAccountRequest) => createAccount(userId!, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['accounts', userId] });
    },
  });
}
