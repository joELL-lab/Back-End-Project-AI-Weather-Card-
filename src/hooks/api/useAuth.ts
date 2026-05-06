const BACKEND_URL = import.meta.env.VITE_BACKEND_URL || "http://localhost:8080";

const getToken = () => localStorage.getItem("ww_token");

export interface AuthUser {
  username: string;
  email: string;
  token: string;
}

const saveUser = (data: AuthUser) => {
  localStorage.setItem("ww_token", data.token);
  localStorage.setItem("ww_user", JSON.stringify({ username: data.username, email: data.email }));
};

export const getStoredUser = (): Omit<AuthUser, "token"> | null => {
  const raw = localStorage.getItem("ww_user");
  return raw ? JSON.parse(raw) : null;
};

export const clearStoredUser = () => {
  localStorage.removeItem("ww_token");
  localStorage.removeItem("ww_user");
};

export const register = async (username: string, email: string, password: string): Promise<AuthUser> => {
  const res = await fetch(`${BACKEND_URL}/api/auth/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, email, password }),
  });
  const data = await res.json();
  if (!res.ok) throw new Error(data.message || "Registrasi gagal.");
  saveUser(data);
  return data;
};

export const login = async (username: string, password: string): Promise<AuthUser> => {
  const res = await fetch(`${BACKEND_URL}/api/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password }),
  });
  const data = await res.json();
  if (!res.ok) throw new Error(data.message || "Login gagal.");
  saveUser(data);
  return data;
};

export const authFetch = (url: string, options: RequestInit = {}) => {
  const token = getToken();
  return fetch(`${BACKEND_URL}${url}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...options.headers,
    },
  });
};
