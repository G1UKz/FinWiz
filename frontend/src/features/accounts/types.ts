export interface Account {
  id: number;
  name: string;
  bankName: string | null;
  accountType: 'DEBIT' | 'CREDIT' | 'SAVINGS' | 'CASH';
  currencyCode: string;
  balance: number;
  isActive: boolean;
}
