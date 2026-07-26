import apiClient from '@/api/client';
import type { Account } from './types';

export async function fetchAccounts(userId: number): Promise<Account[]> {
  const res = await apiClient.get<Account[]>(`/users/${userId}/accounts`);
  return res.data;
}
