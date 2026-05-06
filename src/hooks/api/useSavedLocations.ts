import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { authFetch } from "./useAuth";

export interface SavedLocationItem {
  id: number;
  name: string;
  country: string;
  lat: number;
  lon: number;
  createdAt: string;
}

export const useSavedLocations = (isLoggedIn: boolean) => {
  return useQuery<SavedLocationItem[]>({
    queryKey: ["saved-locations"],
    queryFn: async () => {
      const res = await authFetch("/api/locations");
      if (!res.ok) throw new Error("Gagal mengambil lokasi tersimpan.");
      return res.json();
    },
    enabled: isLoggedIn,
    staleTime: 1000 * 60 * 5,
  });
};

export const useSaveLocation = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async (location: Omit<SavedLocationItem, "id" | "createdAt">) => {
      const res = await authFetch("/api/locations", {
        method: "POST",
        body: JSON.stringify(location),
      });
      const data = await res.json();
      if (!res.ok) throw new Error(data.message || "Gagal menyimpan lokasi.");
      return data;
    },
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["saved-locations"] }),
  });
};

export const useDeleteLocation = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async (id: number) => {
      const res = await authFetch(`/api/locations/${id}`, { method: "DELETE" });
      if (!res.ok) throw new Error("Gagal menghapus lokasi.");
    },
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["saved-locations"] }),
  });
};
