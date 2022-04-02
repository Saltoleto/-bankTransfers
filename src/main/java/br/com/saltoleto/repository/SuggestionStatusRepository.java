package br.com.saltoleto.repository;

import br.com.saltoleto.model.SuggestionStatus;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SuggestionStatusRepository extends JpaRepository<SuggestionStatus, Long> {

    @Cacheable(value = "suggestionStatusList")
    List<SuggestionStatus> findByDescriptionIn(List<String> descriptions);
    @Cacheable(value = "suggestionStatus")
    SuggestionStatus findByDescription(String description);
}
