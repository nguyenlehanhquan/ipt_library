import { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { User, LoginCredentials, RegisterData, AuthContextType } from '../types/auth';

const AuthContext = createContext<AuthContextType | undefined>(undefined);

// CHANGED: base URL cho API
const API_BASE =
  window.location.hostname === 'localhost'
    ? 'http://localhost:8080'
    : ''; // khi deploy, dùng cùng domain (https://printphuthinh.com)

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider = ({ children }: AuthProviderProps) => {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const storedToken = localStorage.getItem('token');
    const rawUser = localStorage.getItem('user');

    const safeParse = (value: string | null) => {
      if (!value) return null;
      const trim = value.trim();
      if (trim === 'undefined' || trim === 'null' || trim === '') return null;
      try { return JSON.parse(trim); } catch { return null; }
    };

    const parsedUser = safeParse(rawUser);

    if (storedToken && parsedUser) {
      setToken(storedToken);
      setUser(parsedUser);
    } else {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    }

    setIsLoading(false);
  }, []);


  const login = async ({ username, password }: LoginCredentials) => {
    setIsLoading(true);
    try {
      const res = await fetch(`${API_BASE}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password }),
      });

      if (!res.ok) {
        const text = await res.text().catch(() => '');
        throw new Error(`Login failed ${res.status} ${text}`);
      }

      const data: any = await res.json(); // token, username, tokenType (từ Postman)

      const tokenType = (data.tokenType || 'Bearer').trim();
      const accessToken = data.token;
      if (!accessToken) {
        throw new Error('Missing token in login response');
      }

      // tạo user tạm vì backend không trả object user
      const loggedInUser: User = {
        id: (data.id ?? '').toString(),   // phòng null/undefined
        email: '',                        // chưa có thì để trống
        name: data.username ?? username,
      };

      setToken(accessToken);
      setUser(loggedInUser);

      localStorage.setItem('token', accessToken);
      localStorage.setItem('tokenType', tokenType);
      localStorage.setItem('user', JSON.stringify(loggedInUser));
    } finally {
      setIsLoading(false);
    }
  };


  const register = async (data: RegisterData) => {
    setIsLoading(true);
    try {
      const response = await fetch(`${API_BASE}/users`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
      });

      if (!response.ok) {
        throw new Error('Registration failed');
      }

      const result = await response.json();

      setToken(result.token);
      setUser(result.user);

      localStorage.setItem('token', result.token);
      localStorage.setItem('user', JSON.stringify(result.user));
    } finally {
      setIsLoading(false);
    }
  };

  const logout = () => {
    setUser(null);
    setToken(null);
    localStorage.removeItem('token');
    localStorage.removeItem('tokenType'); // thêm dòng này
    localStorage.removeItem('user');
  };


  return (
    <AuthContext.Provider value={{ user, token, login, register, logout, isLoading }}>
      {children}
    </AuthContext.Provider>
  );
};

