import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { authFetch } from "./useAuth";

export interface SearchHistoryItem {
  id: number;
  cityName: string;
  country: string;
  searchedAt: string;
}

export const useSearchHistory = (isLoggedIn: boolean) => {
  return useQuery<SearchHistoryItem[]>({
    queryKey: ["search-history"],
    queryFn: async () => {
      const res = await authFetch("/api/search-history");
      if (!res.ok) throw new Error("Gagal mengambil riwayat pencarian.");
      return res.json();
    },
    enabled: isLoggedIn,
  });
};

export const useAddSearchHistory = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async ({ cityName, country }: { cityName: string; country?: string }) => {
      const res = await authFetch("/api/search-history", {
        method: "POST",
        body: JSON.stringify({ cityName, country: country ?? "" }),
      });
      if (!res.ok) throw new Error("Gagal menambah riwayat.");
      return res.json();
    },
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["search-history"] }),
  });
};

export const useDeleteSearchHistory = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async (id: number) => {
      const res = await authFetch(`/api/search-history/${id}`, { method: "DELETE" });
      if (!res.ok) throw new Error("Gagal menghapus riwayat.");
    },
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["search-history"] }),
  });
};

export const useClearSearchHistory = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async () => {
      const res = await authFetch("/api/search-history", { method: "DELETE" });
      if (!res.ok) throw new Error("Gagal menghapus semua riwayat.");
    },
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["search-history"] }),
  });
};
