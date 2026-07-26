import apiClient from '@/api/client';
import type { Transaction, CreateTransactionRequest } from './types';

export async function fetchTransactions(userId: number): Promise<Transaction[]> {
  const res = await apiClient.get<Transaction[]>(`/users/${userId}/transactions`);
  return res.data;
}

export async function createTransaction(
  userId: number,
  data: CreateTransactionRequest
): Promise<Transaction> {
  const res = await apiClient.post<Transaction>(`/users/${userId}/transactions`, data);
  return res.data;
}
