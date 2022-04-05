package br.com.saltoleto.service;

import br.com.saltoleto.enumerator.SuggestionStatusEmum;
import br.com.saltoleto.model.BankTransferEntity;
import br.com.saltoleto.model.SuggestionStatus;
import br.com.saltoleto.repository.BankTransferRepository;
import br.com.saltoleto.repository.SuggestionStatusRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankTransferSuggestionManagerProcessorServiceImplTest {

    @InjectMocks
    private BankTransferSuggestionManagerProcessorServiceImpl service;

    @Mock
    private BankTransferRepository bankTransferRepository;

    @Mock
    private SuggestionStatusRepository suggestionStatusRepository;

    private static List<BankTransferEntity> bankTransferEntityList;

    private static SuggestionStatus suggestionStatus;

    public static final int FIRST = 0;
    public static final int ONE_DAY = 1;
    public static final int TWO_DAYS = 2;

    @BeforeAll
    static void setup() {
        bankTransferEntityList = List.of(BankTransferEntity.builder()
                .dateTransfer(LocalDate.now())
                .suggestionStatus(SuggestionStatus.builder()
                        .description(SuggestionStatusEmum.PENDING_PROCESSING.name())
                        .build())
                .counterPartyId(UUID.randomUUID().toString())
                .build());
        suggestionStatus = SuggestionStatus.builder()
                .description(SuggestionStatusEmum.PROCESSING.name())
                .build();
    }

    @Test
    void mustBeChangeSuggestionStatusToProcessingAndSendMessageSQS() {

        //ARRANGE
        when(suggestionStatusRepository.findByDescription(Mockito.anyString())).thenReturn(suggestionStatus);

        when(bankTransferRepository.findByCounterPartyIdAndSuggestionStatusIdInOrderByDateTransfer(
                Mockito.anyString(), Mockito.anyList())).thenReturn(bankTransferEntityList);

        //ACT
        service.process(Mockito.anyString());

        //ASSERT
        assertSame(SuggestionStatusEmum.PROCESSING.name(), bankTransferEntityList.get(FIRST).getSuggestionStatus().getDescription());

    }

    @Test
    void mustNotChangeTheStatusOfTheSuggestionOrSendSQSMessage() {

        //ARRANGE
        bankTransferEntityList.get(FIRST).setSuggestionStatus(SuggestionStatus.builder()
                .description(SuggestionStatusEmum.PROCESSING.name()).build());

        when(bankTransferRepository.findByCounterPartyIdAndSuggestionStatusIdInOrderByDateTransfer(
                Mockito.anyString(), Mockito.anyList())).thenReturn(bankTransferEntityList);

        //ACT
        service.process(Mockito.anyString());

        //ASSERT
        assertSame(SuggestionStatusEmum.PROCESSING.name(), bankTransferEntityList.get(FIRST).getSuggestionStatus().getDescription());

    }

    @Test
    void mustRetrieveTheOldestTransferChangeSuggestionStatusToProcessAndSendSQSMessage() {

        //ARRANGE
        List<BankTransferEntity> bankTransferEntityListSorted =
                addMoreTwoBankTransfersAndSortDateTransfer(bankTransferEntityList);

        when(suggestionStatusRepository.findByDescription(Mockito.anyString())).thenReturn(suggestionStatus);

        when(bankTransferRepository.findByCounterPartyIdAndSuggestionStatusIdInOrderByDateTransfer(
                Mockito.anyString(), Mockito.anyList())).thenReturn(bankTransferEntityListSorted);

        //ACT
        service.process(Mockito.anyString());

        //ASSERT
        BankTransferEntity bankTransferEntity = bankTransferEntityListSorted.get(FIRST);
        assertSame(bankTransferEntity.getSuggestionStatus().getDescription(), SuggestionStatusEmum.PROCESSING.name());
        assertEquals(bankTransferEntity.getDateTransfer(), LocalDate.now().minusDays(TWO_DAYS));

    }

    private List<BankTransferEntity> addMoreTwoBankTransfersAndSortDateTransfer(List<BankTransferEntity> bankTransferEntityList) {
        List<BankTransferEntity> transferEntities = new ArrayList<>();

        transferEntities.add(bankTransferEntityList.get(FIRST));

        transferEntities.add(BankTransferEntity.builder()
                .dateTransfer(LocalDate.now().minusDays(ONE_DAY))
                .suggestionStatus(SuggestionStatus.builder()
                        .description(SuggestionStatusEmum.PENDING_PROCESSING.name())
                        .build())
                .counterPartyId(UUID.randomUUID().toString())
                .build());

        transferEntities.add(BankTransferEntity.builder()
                .dateTransfer(LocalDate.now().minusDays(TWO_DAYS))
                .suggestionStatus(SuggestionStatus.builder()
                        .description(SuggestionStatusEmum.PENDING_PROCESSING.name())
                        .build())
                .counterPartyId(UUID.randomUUID().toString())
                .build());

        transferEntities.sort(Comparator.comparing(BankTransferEntity::getDateTransfer));

        return transferEntities;
    }
}