export interface User {
  id: number;
  email: string;
  name: string;
}

export interface AuthResponse {
  accessToken: string;
  tokenType: string;
  userId: number;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  name: string;
}
