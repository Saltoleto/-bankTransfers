package br.com.saltoleto.service;

import br.com.saltoleto.enumerator.SuggestionStatusEmum;
import br.com.saltoleto.model.BankTransferEntity;
import br.com.saltoleto.model.SuggestionStatus;
import br.com.saltoleto.repository.BankTransferRepository;
import br.com.saltoleto.repository.SuggestionStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
class BankTransferSuggestionManagerProcessorServiceImpl implements BankTransferSuggestionManagerProcessorService {

    public static final int FIRST = 0;
    private BankTransferRepository bankTransferRepository;
    private SuggestionStatusRepository suggestionStatusRepository;

    @Override
    public void process(String counterParty) {

        List<BankTransferEntity> bankTransferEntityList = getBankTransferListByCounterPartyAndSuggestionStatus(
                counterParty, getSuggestionStatusList());

        if (bankTransferEntityList.isEmpty()) return;

        manageUpdateBankTransferSuggestionAndSendMessage(bankTransferEntityList);

    }

    private void manageUpdateBankTransferSuggestionAndSendMessage(List<BankTransferEntity> bankTransferEntityList) {
        bankTransferEntityList.stream().filter(getPredicateForFilter()).findFirst().ifPresentOrElse(bankTransferEntity -> {
            System.out.printf("%s in processing%n", bankTransferEntity);
        }, () -> {
            updateBankTransferSuggestionStatusToProcessing(bankTransferEntityList.get(FIRST));
            sendMessageToSQS();
        });
    }

    private void sendMessageToSQS() {
        //TODO
        System.out.println("Message sent to SQS");
    }

    private void updateBankTransferSuggestionStatusToProcessing(BankTransferEntity bankTransferEntity) {
        bankTransferEntity.setSuggestionStatus(suggestionStatusRepository.findByDescription
                (SuggestionStatusEmum.PROCESSING.name()));
        bankTransferRepository.save(bankTransferEntity);
        System.out.printf("%s status updated%n", bankTransferEntity);
    }

    private Predicate<BankTransferEntity> getPredicateForFilter() {
        return bankTransferEntity ->
                SuggestionStatusEmum.PROCESSING.name().equals(bankTransferEntity.getSuggestionStatus().getDescription());
    }

    private List<BankTransferEntity> getBankTransferListByCounterPartyAndSuggestionStatus
            (String counterParty, List<SuggestionStatus> suggestionStatusList) {
        return bankTransferRepository.findByCounterPartyIdAndSuggestionStatusIdInOrderByDateTransfer
                (counterParty, suggestionStatusList.stream().map(SuggestionStatus::getId).collect(Collectors.toList()));
    }

    private List<SuggestionStatus> getSuggestionStatusList() {
        return suggestionStatusRepository.findByDescriptionIn(
                List.of(SuggestionStatusEmum.PENDING_PROCESSING.name(),
                        SuggestionStatusEmum.PROCESSING.name()));
    }

}
