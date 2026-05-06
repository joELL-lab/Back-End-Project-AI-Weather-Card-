import { useState } from "react";

const BACKEND_URL = import.meta.env.VITE_BACKEND_URL || "http://localhost:8080";

export interface RagMessage {
  role: "user" | "assistant";
  content: string;
  sources?: string[];
  documentsUsed?: number;
}

export const useRagChat = () => {
  const [messages, setMessages] = useState<RagMessage[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  const askQuestion = async (
    question: string,
    city?: string,
    weatherCondition?: string
  ) => {
    const userMessage: RagMessage = { role: "user", content: question };
    setMessages((prev) => [...prev, userMessage]);
    setIsLoading(true);

    try {
      const response = await fetch(`${BACKEND_URL}/api/rag/ask`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          question,
          city: city ?? "",
          weatherCondition: weatherCondition ?? "",
        }),
      });

      if (!response.ok) {
        throw new Error("Gagal mendapatkan jawaban");
      }

      const data = await response.json();
      const assistantMessage: RagMessage = {
        role: "assistant",
        content: data.answer,
        sources: data.sources,
        documentsUsed: data.documentsUsed,
      };
      setMessages((prev) => [...prev, assistantMessage]);
    } catch (error) {
      const errorMessage: RagMessage = {
        role: "assistant",
        content: "Maaf, terjadi kesalahan. Coba lagi nanti ya! 😔",
      };
      setMessages((prev) => [...prev, errorMessage]);
    } finally {
      setIsLoading(false);
    }
  };

  const clearMessages = () => setMessages([]);

  return { messages, isLoading, askQuestion, clearMessages };
};
