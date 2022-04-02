package br.com.saltoleto.repository;

import br.com.saltoleto.model.SuggestionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SuggestionStatusRepository extends JpaRepository<SuggestionStatus, Long> {

    List<SuggestionStatus> findByDescriptionIn(List<String> descriptions);
    SuggestionStatus findByDescription(String description);
}
