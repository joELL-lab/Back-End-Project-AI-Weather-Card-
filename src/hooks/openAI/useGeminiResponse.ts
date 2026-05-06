import { useQuery } from "@tanstack/react-query";
import { ChatPrompt } from "../../types/types";

const BACKEND_URL = import.meta.env.VITE_BACKEND_URL || "http://localhost:8080";

const getGeminiResponse = async (prompt: ChatPrompt) => {
  const response = await fetch(`${BACKEND_URL}/api/ai/suggestions`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      systemMessage: prompt.systemMessage.content,
      userMessage: prompt.userMessage.content,
    }),
  });

  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(
      error.message || `AI request failed with status: ${response.status}`
    );
  }

  return await response.json();
};

export const useGeminiResponse = (prompt: ChatPrompt) => {
  return useQuery({
    queryKey: prompt ? ["gemini", prompt] : [],
    queryFn: () => getGeminiResponse(prompt),
    retry: false,
    enabled: false,
    staleTime: Infinity,
    gcTime: Infinity,
  });
};
