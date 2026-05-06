import { useState, useRef, useEffect } from "react";
import { useRagChat, RagMessage } from "../../hooks/api/useRagChat";
import { motion, AnimatePresence } from "framer-motion";

interface RagChatProps {
  city?: string;
  weatherCondition?: string;
}

const RagChat: React.FC<RagChatProps> = ({ city, weatherCondition }) => {
  const [isOpen, setIsOpen] = useState(false);
  const [input, setInput] = useState("");
  const { messages, isLoading, askQuestion, clearMessages } = useRagChat();
  const messagesEndRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (input.trim() && !isLoading) {
      askQuestion(input.trim(), city, weatherCondition);
      setInput("");
    }
  };

  const quickQuestions = [
    "Tips kesehatan saat cuaca panas?",
    "Aktivitas apa yang cocok saat hujan?",
    "Bagaimana cara membaca AQI?",
    "Rekomendasi pakaian hari ini?",
  ];

  return (
    <>
      {/* FAB Button */}
      <button
        onClick={() => setIsOpen(!isOpen)}
        className="fixed bottom-6 right-6 z-50 w-14 h-14 rounded-full shadow-xl flex items-center justify-center transition-all duration-300 hover:scale-110"
        style={{
          background: "linear-gradient(135deg, #0ea5e9, #6366f1)",
          color: "white",
        }}
        title="WeatherWise AI Chat"
      >
        {isOpen ? (
          <svg width="24" height="24" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
            <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
          </svg>
        ) : (
          <svg width="24" height="24" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
            <path strokeLinecap="round" strokeLinejoin="round" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
          </svg>
        )}
      </button>

      {/* Chat Window */}
      <AnimatePresence>
        {isOpen && (
          <motion.div
            initial={{ opacity: 0, y: 20, scale: 0.95 }}
            animate={{ opacity: 1, y: 0, scale: 1 }}
            exit={{ opacity: 0, y: 20, scale: 0.95 }}
            transition={{ duration: 0.2 }}
            className="fixed bottom-24 right-6 z-50 w-[380px] max-w-[calc(100vw-2rem)] rounded-2xl shadow-2xl overflow-hidden flex flex-col"
            style={{
              height: "520px",
              background: "linear-gradient(180deg, #0c1929 0%, #0f2744 100%)",
              border: "1px solid rgba(14, 165, 233, 0.3)",
            }}
          >
            {/* Header */}
            <div
              className="flex items-center justify-between px-4 py-3"
              style={{
                background: "linear-gradient(135deg, #0ea5e9, #6366f1)",
              }}
            >
              <div className="flex items-center gap-2">
                <span className="text-lg">🤖</span>
                <div>
                  <h3 className="text-white font-bold text-sm">WeatherWise AI</h3>
                  <p className="text-sky-100 text-xs">RAG-powered assistant</p>
                </div>
              </div>
              <button
                onClick={clearMessages}
                className="text-white/70 hover:text-white text-xs px-2 py-1 rounded hover:bg-white/10 transition-colors"
                title="Clear chat"
              >
                Clear
              </button>
            </div>

            {/* Messages */}
            <div className="flex-1 overflow-y-auto px-4 py-3 space-y-3">
              {messages.length === 0 && (
                <div className="text-center py-6">
                  <p className="text-sky-300 text-sm mb-4">
                    👋 Halo! Tanya apa saja tentang cuaca, kesehatan, atau aktivitas!
                  </p>
                  <div className="flex flex-wrap gap-2 justify-center">
                    {quickQuestions.map((q, i) => (
                      <button
                        key={i}
                        onClick={() => {
                          askQuestion(q, city, weatherCondition);
                        }}
                        className="text-xs px-3 py-1.5 rounded-full transition-colors"
                        style={{
                          background: "rgba(14, 165, 233, 0.15)",
                          color: "#7dd3fc",
                          border: "1px solid rgba(14, 165, 233, 0.3)",
                        }}
                      >
                        {q}
                      </button>
                    ))}
                  </div>
                </div>
              )}

              {messages.map((msg, index) => (
                <MessageBubble key={index} message={msg} />
              ))}

              {isLoading && (
                <div className="flex gap-2 items-center px-3 py-2">
                  <div className="flex gap-1">
                    <span className="w-2 h-2 bg-sky-400 rounded-full animate-bounce" style={{ animationDelay: "0ms" }} />
                    <span className="w-2 h-2 bg-sky-400 rounded-full animate-bounce" style={{ animationDelay: "150ms" }} />
                    <span className="w-2 h-2 bg-sky-400 rounded-full animate-bounce" style={{ animationDelay: "300ms" }} />
                  </div>
                  <span className="text-sky-400 text-xs">Mencari dan memproses...</span>
                </div>
              )}

              <div ref={messagesEndRef} />
            </div>

            {/* Input */}
            <form onSubmit={handleSubmit} className="px-4 py-3 border-t border-sky-800/50">
              <div className="flex gap-2">
                <input
                  type="text"
                  value={input}
                  onChange={(e) => setInput(e.target.value)}
                  placeholder="Tanya tentang cuaca..."
                  disabled={isLoading}
                  className="flex-1 text-sm px-3 py-2 rounded-lg outline-none transition-colors"
                  style={{
                    background: "rgba(14, 165, 233, 0.1)",
                    border: "1px solid rgba(14, 165, 233, 0.3)",
                    color: "#e0f2fe",
                  }}
                />
                <button
                  type="submit"
                  disabled={isLoading || !input.trim()}
                  className="px-3 py-2 rounded-lg text-white text-sm font-semibold transition-all disabled:opacity-40"
                  style={{
                    background: "linear-gradient(135deg, #0ea5e9, #6366f1)",
                  }}
                >
                  Kirim
                </button>
              </div>
            </form>
          </motion.div>
        )}
      </AnimatePresence>
    </>
  );
};

const MessageBubble: React.FC<{ message: RagMessage }> = ({ message }) => {
  const isUser = message.role === "user";

  return (
    <div className={`flex ${isUser ? "justify-end" : "justify-start"}`}>
      <div
        className={`max-w-[85%] px-3 py-2 rounded-2xl text-sm leading-relaxed ${
          isUser
            ? "rounded-br-md"
            : "rounded-bl-md"
        }`}
        style={{
          background: isUser
            ? "linear-gradient(135deg, #0ea5e9, #0284c7)"
            : "rgba(14, 165, 233, 0.12)",
          color: isUser ? "white" : "#bae6fd",
          border: isUser ? "none" : "1px solid rgba(14, 165, 233, 0.2)",
        }}
      >
        <p className="whitespace-pre-wrap">{message.content}</p>

        {message.sources && message.sources.length > 0 && (
          <div className="mt-2 pt-2 border-t border-sky-700/30">
            <p className="text-[10px] text-sky-400/70 mb-1">
              📚 Sumber ({message.documentsUsed} dokumen):
            </p>
            {message.sources.map((source, i) => (
              <p key={i} className="text-[10px] text-sky-400/60">
                • {source}
              </p>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default RagChat;
