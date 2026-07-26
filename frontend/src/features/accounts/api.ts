import apiClient from '@/api/client';
import type { Account } from './types';

export async function fetchAccounts(userId: number): Promise<Account[]> {
  const res = await apiClient.get<Account[]>(`/users/${userId}/accounts`);
  return res.data;
}

export interface CreateAccountRequest {
  name: string;
  bankName: string | null;
  accountType: 'DEBIT' | 'CREDIT' | 'SAVINGS' | 'CASH';
  currencyCode: string;
  initialBalanceMinor: number;
}

export async function createAccount(userId: number, data: CreateAccountRequest): Promise<Account> {
  const res = await apiClient.post<Account>(`/users/${userId}/accounts`, data);
  return res.data;
}
