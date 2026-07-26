import { create } from 'zustand';

interface AuthState {
  userId: number | null;
  token: string | null;
  isAuthenticated: boolean;
  login: (userId: number, token: string) => void;
  logout: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  userId: null,
  token: localStorage.getItem('finwiz_token'),
  isAuthenticated: !!localStorage.getItem('finwiz_token'),

  login: (userId, token) => {
    localStorage.setItem('finwiz_token', token);
    set({ userId, token, isAuthenticated: true });
  },

  logout: () => {
    localStorage.removeItem('finwiz_token');
    set({ userId: null, token: null, isAuthenticated: false });
  },
}));
