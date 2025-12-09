export interface User {
  id: string;
  fullName: string;
  username?: string;
}

export interface LoginCredentials {
  username: string;
  password: string;
}

export interface RegisterData {
  fullName: string;
  username: string;
  password: string;
}

export interface AuthContextType {
  user: User | null;
  token: string | null;
  login: (credentials: LoginCredentials) => Promise<void>;
  register: (data: RegisterData) => Promise<void>;
  logout: () => void;
  isLoading: boolean;
}
