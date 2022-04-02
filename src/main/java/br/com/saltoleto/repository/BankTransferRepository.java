package br.com.saltoleto.repository;

import br.com.saltoleto.model.BankTransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankTransferRepository extends JpaRepository<BankTransferEntity, Long> {

    List<BankTransferEntity> findByCounterPartyIdAndSuggestionStatusIdInOrderByDateTransfer(String counterPartyId, List<Long> suggestionStatus);

}
