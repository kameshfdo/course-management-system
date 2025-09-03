import { AuthResponse, LoginRequest, RegisterRequest, AuthUser } from '../types';

const API_BASE_URL = 'http://localhost:8080/api';

class AuthService {
  private async request<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
    const url = `${API_BASE_URL}${endpoint}`;
    const token = localStorage.getItem('token');
    
    const config: RequestInit = {
      headers: {
        'Content-Type': 'application/json',
        ...(token && { 'Authorization': `Bearer ${token}` }),
        ...options.headers,
      },
      ...options,
    };

    try {
      const response = await fetch(url, config);
      
      if (!response.ok) {
        if (response.status === 401) {
          localStorage.removeItem('token');
          window.location.href = '/login';
        }
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      return await response.json();
    } catch (error) {
      console.error(`API request failed: ${endpoint}`, error);
      throw error;
    }
  }

  async login(username: string, password: string): Promise<AuthResponse> {
    const loginRequest: LoginRequest = { username, password };
    return this.request<AuthResponse>('/auth/login', {
      method: 'POST',
      body: JSON.stringify(loginRequest),
    });
  }

  async register(data: RegisterRequest): Promise<AuthResponse> {
    return this.request<AuthResponse>('/auth/register', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  async getCurrentUser(): Promise<AuthUser> {
    return this.request<AuthUser>('/auth/me');
  }

  async validateToken(token: string): Promise<boolean> {
    return this.request<boolean>(`/auth/validate?token=${token}`);
  }
}

const authService = new AuthService();
export default authService;