import apiClient from '@/api/client';
import type { Category } from './types';

export async function fetchCategories(userId: number): Promise<Category[]> {
  const res = await apiClient.get<Category[]>(`/users/${userId}/categories`);
  return res.data;
}
