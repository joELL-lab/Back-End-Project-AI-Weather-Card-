import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { clearStoredUser, getStoredUser } from "../hooks/api/useAuth";

interface AuthState {
  user: { username: string; email: string } | null;
  isLoggedIn: boolean;
}

const stored = getStoredUser();

const initialState: AuthState = {
  user: stored,
  isLoggedIn: !!stored,
};

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setUser(state, action: PayloadAction<{ username: string; email: string }>) {
      state.user = action.payload;
      state.isLoggedIn = true;
    },
    logout(state) {
      clearStoredUser();
      state.user = null;
      state.isLoggedIn = false;
    },
  },
});

export const { setUser, logout } = authSlice.actions;
export const selectUser = (state: any) => state.auth.user;
export const selectIsLoggedIn = (state: any) => state.auth.isLoggedIn;
export default authSlice.reducer;
