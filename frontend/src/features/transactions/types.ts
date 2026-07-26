export interface Transaction {
  id: number;
  categoryName: string;
  bankAccountName: string;
  type: 'INCOME' | 'EXPENSE' | 'TRANSFER';
  amount: number;
  description: string | null;
  transactionDate: string;
}

export interface CreateTransactionRequest {
  bankAccountId: number;
  categoryId: number;
  type: 'INCOME' | 'EXPENSE';
  amountMinor: number;
  description: string | null;
  transactionDate: string;
}
