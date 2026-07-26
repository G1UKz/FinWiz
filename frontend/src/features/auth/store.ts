import { create } from 'zustand';

interface AuthState {
  userId: number | null;
  token: string | null;
  isAuthenticated: boolean;
  login: (userId: number, token: string) => void;
  logout: () => void;
}

const storedToken = localStorage.getItem('finwiz_token');
const storedUserId = localStorage.getItem('finwiz_user_id');

export const useAuthStore = create<AuthState>((set) => ({
  userId: storedUserId ? Number(storedUserId) : null,
  token: storedToken,
  isAuthenticated: !!storedToken,

  login: (userId, token) => {
    localStorage.setItem('finwiz_token', token);
    localStorage.setItem('finwiz_user_id', String(userId));
    set({ userId, token, isAuthenticated: true });
  },

  logout: () => {
    localStorage.removeItem('finwiz_token');
    localStorage.removeItem('finwiz_user_id');
    set({ userId: null, token: null, isAuthenticated: false });
  },
}));
