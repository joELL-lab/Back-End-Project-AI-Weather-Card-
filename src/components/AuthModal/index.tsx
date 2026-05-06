import { useState, useEffect, useRef } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { login, register } from "../../hooks/api/useAuth";
import { setUser } from "../../slices/authSlice";
import { useDispatch } from "react-redux";

interface AuthModalProps {
  isOpen: boolean;
  onClose: () => void;
}

const AuthModal: React.FC<AuthModalProps> = ({ isOpen, onClose }) => {
  const dispatch = useDispatch();
  const [mode, setMode] = useState<"login" | "register">("login");
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const overlayRef = useRef<HTMLDivElement>(null);

  // Reset form on open/mode change
  useEffect(() => {
    setUsername("");
    setEmail("");
    setPassword("");
    setError(null);
  }, [isOpen, mode]);

  // Close on Escape
  useEffect(() => {
    const handler = (e: KeyboardEvent) => {
      if (e.key === "Escape") onClose();
    };
    window.addEventListener("keydown", handler);
    return () => window.removeEventListener("keydown", handler);
  }, [onClose]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setIsLoading(true);
    try {
      const data =
        mode === "login"
          ? await login(username, password)
          : await register(username, email, password);
      dispatch(setUser({ username: data.username, email: data.email }));
      onClose();
    } catch (err: any) {
      setError(err.message || "Something went wrong.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <AnimatePresence>
      {isOpen && (
        <motion.div
          ref={overlayRef}
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          transition={{ duration: 0.2 }}
          className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm px-4"
          onClick={(e) => e.target === overlayRef.current && onClose()}
        >
          <motion.div
            initial={{ opacity: 0, y: -30, scale: 0.95 }}
            animate={{ opacity: 1, y: 0, scale: 1 }}
            exit={{ opacity: 0, y: -30, scale: 0.95 }}
            transition={{ duration: 0.25, ease: "easeOut" }}
            className="bg-white dark:bg-sky-900 rounded-3xl shadow-2xl w-full max-w-md p-8 flex flex-col gap-6"
          >
            {/* Header */}
            <div className="flex items-center justify-between">
              <h2 className="text-2xl font-bold text-sky-800 dark:text-sky-100">
                {mode === "login" ? "👋 Welcome back!" : "🌟 Create account"}
              </h2>
              <button
                onClick={onClose}
                aria-label="Close auth modal"
                className="text-sky-400 hover:text-sky-600 dark:hover:text-sky-200 text-2xl leading-none transition-colors"
              >
                ✕
              </button>
            </div>

            {/* Tab switcher */}
            <div className="flex rounded-xl overflow-hidden border border-sky-200 dark:border-sky-700">
              {(["login", "register"] as const).map((m) => (
                <button
                  key={m}
                  onClick={() => setMode(m)}
                  className={`flex-1 py-2.5 text-sm font-semibold transition-colors ${
                    mode === m
                      ? "bg-sky-600 text-white"
                      : "bg-transparent text-sky-600 dark:text-sky-300 hover:bg-sky-50 dark:hover:bg-sky-800"
                  }`}
                >
                  {m === "login" ? "Login" : "Register"}
                </button>
              ))}
            </div>

            {/* Form */}
            <form onSubmit={handleSubmit} className="flex flex-col gap-4">
              <div className="flex flex-col gap-1.5">
                <label
                  htmlFor="auth-username"
                  className="text-sm font-semibold text-sky-700 dark:text-sky-300"
                >
                  Username
                </label>
                <input
                  id="auth-username"
                  type="text"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  placeholder="Enter your username"
                  required
                  autoComplete="username"
                  className="w-full px-4 py-2.5 rounded-xl border border-sky-200 dark:border-sky-600 bg-sky-50 dark:bg-sky-800 text-sky-800 dark:text-sky-100 placeholder:text-sky-400 dark:placeholder:text-sky-500 focus:outline-none focus:ring-2 focus:ring-sky-400 transition-all text-sm"
                />
              </div>

              {mode === "register" && (
                <div className="flex flex-col gap-1.5">
                  <label
                    htmlFor="auth-email"
                    className="text-sm font-semibold text-sky-700 dark:text-sky-300"
                  >
                    Email
                  </label>
                  <input
                    id="auth-email"
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="Enter your email"
                    required
                    autoComplete="email"
                    className="w-full px-4 py-2.5 rounded-xl border border-sky-200 dark:border-sky-600 bg-sky-50 dark:bg-sky-800 text-sky-800 dark:text-sky-100 placeholder:text-sky-400 dark:placeholder:text-sky-500 focus:outline-none focus:ring-2 focus:ring-sky-400 transition-all text-sm"
                  />
                </div>
              )}

              <div className="flex flex-col gap-1.5">
                <label
                  htmlFor="auth-password"
                  className="text-sm font-semibold text-sky-700 dark:text-sky-300"
                >
                  Password
                </label>
                <input
                  id="auth-password"
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="Enter your password"
                  required
                  autoComplete={mode === "login" ? "current-password" : "new-password"}
                  className="w-full px-4 py-2.5 rounded-xl border border-sky-200 dark:border-sky-600 bg-sky-50 dark:bg-sky-800 text-sky-800 dark:text-sky-100 placeholder:text-sky-400 dark:placeholder:text-sky-500 focus:outline-none focus:ring-2 focus:ring-sky-400 transition-all text-sm"
                />
              </div>

              {error && (
                <motion.p
                  initial={{ opacity: 0 }}
                  animate={{ opacity: 1 }}
                  className="text-sm text-red-500 dark:text-red-400 bg-red-50 dark:bg-red-900/30 px-3 py-2 rounded-lg"
                >
                  ⚠️ {error}
                </motion.p>
              )}

              <button
                type="submit"
                disabled={isLoading}
                id="auth-submit-btn"
                className="mt-1 w-full py-3 bg-sky-600 hover:bg-sky-700 disabled:opacity-60 disabled:cursor-not-allowed text-white font-bold rounded-xl shadow-md hover:shadow-lg transition-all text-sm"
              >
                {isLoading
                  ? "Please wait..."
                  : mode === "login"
                  ? "Login"
                  : "Create Account"}
              </button>
            </form>

            <p className="text-center text-xs text-sky-500 dark:text-sky-400">
              {mode === "login"
                ? "Don't have an account? "
                : "Already have an account? "}
              <button
                onClick={() => setMode(mode === "login" ? "register" : "login")}
                className="font-semibold text-sky-600 dark:text-sky-300 hover:underline"
              >
                {mode === "login" ? "Register" : "Login"}
              </button>
            </p>
          </motion.div>
        </motion.div>
      )}
    </AnimatePresence>
  );
};

export default AuthModal;
