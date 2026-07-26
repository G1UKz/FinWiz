import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useAuthStore } from '@/features/auth/store';
import { fetchTransactions, createTransaction } from './api';
import type { CreateTransactionRequest } from './types';

export function useTransactions() {
  const userId = useAuthStore((s) => s.userId);
  return useQuery({
    queryKey: ['transactions', userId],
    queryFn: () => fetchTransactions(userId!),
    enabled: !!userId,
  });
}

export function useCreateTransaction() {
  const queryClient = useQueryClient();
  const userId = useAuthStore((s) => s.userId);

  return useMutation({
    mutationFn: (data: CreateTransactionRequest) => createTransaction(userId!, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['transactions', userId] });
      queryClient.invalidateQueries({ queryKey: ['accounts', userId] });
    },
  });
}
