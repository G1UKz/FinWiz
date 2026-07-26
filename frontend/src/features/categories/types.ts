export interface Category {
  id: number;
  name: string;
  type: 'INCOME' | 'EXPENSE';
  color: string;
  icon: string | null;
  isSystem: boolean;
}
