package com.weatherwise.repository;

import com.weatherwise.model.KnowledgeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgeDocumentRepository extends JpaRepository<KnowledgeDocument, Long> {

    List<KnowledgeDocument> findByCategory(String category);

    @Query("SELECT d FROM KnowledgeDocument d WHERE " +
           "LOWER(d.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.keywords) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<KnowledgeDocument> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT d FROM KnowledgeDocument d WHERE " +
           "(LOWER(d.content) LIKE LOWER(CONCAT('%', :kw1, '%')) OR " +
           " LOWER(d.keywords) LIKE LOWER(CONCAT('%', :kw1, '%'))) OR " +
           "(LOWER(d.content) LIKE LOWER(CONCAT('%', :kw2, '%')) OR " +
           " LOWER(d.keywords) LIKE LOWER(CONCAT('%', :kw2, '%')))")
    List<KnowledgeDocument> searchByTwoKeywords(@Param("kw1") String kw1, @Param("kw2") String kw2);
}
