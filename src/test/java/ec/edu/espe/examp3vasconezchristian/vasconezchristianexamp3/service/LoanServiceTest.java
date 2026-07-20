package ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.dto.LoanResponse;
import ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.exception.EquipmentAlreadyLoanedException;
import ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.exception.InvalidBorrowerEmailException;
import ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.exception.InvalidLoanDaysException;
import ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.model.EquipmentLoan;
import ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.model.EquipmentLoanStatus;
import ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private EquipmentPolicyClient equipmentPolicyClient;

    @InjectMocks
    private LoanService loanService;

    @Captor
    private ArgumentCaptor<EquipmentLoan> loanCaptor;

    @Test
    void createLoan_successfulLoanCreation() {
        // Arrange
        when(loanRepository.existsByEquipmentCodeAndStatus("LAPTOP-VASCONEZCHRISTIAN", EquipmentLoanStatus.APPROVED)).thenReturn(false);
        when(equipmentPolicyClient.isBorrowerBlocked("cgvasconez2@espe.edu.ec")).thenReturn(false);
        when(loanRepository.save(any(EquipmentLoan.class))).thenAnswer(invocation -> {
            EquipmentLoan loan = invocation.getArgument(0);
            loan.setId(100L);
            return loan;
        });

        // Act
        LoanResponse response = loanService.createLoan("LAPTOP-VASCONEZCHRISTIAN", "cgvasconez2@espe.edu.ec", 5);

        // Assert
        assertEquals(100L, response.getLoanId());
        assertEquals("APPROVED-LAPTOP-VASCONEZ-5", response.getApprovalCode());
        verify(loanRepository).save(loanCaptor.capture());
        EquipmentLoan savedLoan = loanCaptor.getValue();
        assertEquals("LAPTOP-VASCONEZCHRISTIAN", savedLoan.getEquipmentCode());
        assertEquals("cgvasconez2@espe.edu.ec", savedLoan.getBorrowerEmail());
        assertEquals(5, savedLoan.getLoanDays());
        assertEquals(EquipmentLoanStatus.APPROVED, savedLoan.getStatus());
    }

    @Test
    void createLoan_invalidEmail_throwsExceptionAndNoInteractions() {
        // Arrange

        // Act
        InvalidBorrowerEmailException exception = assertThrows(
                InvalidBorrowerEmailException.class,
            () -> loanService.createLoan("LAPTOP-VASCONEZCHRISTIAN", "correo-invalido", 5)
        );

        // Assert
        assertEquals("El correo electrónico debe tener un formato válido", exception.getMessage());
        verifyNoInteractions(loanRepository, equipmentPolicyClient);
    }

    @Test
    void createLoan_invalidLoanDays_throwsExceptionAndNoInteractions() {
        // Arrange

        // Act
        InvalidLoanDaysException exception = assertThrows(
                InvalidLoanDaysException.class,
            () -> loanService.createLoan("LAPTOP-VASCONEZCHRISTIAN", "cgvasconez2@espe.edu.ec", 20)
        );

        // Assert
        assertEquals("loanDays debe estar entre 1 y 15 días", exception.getMessage());
        verifyNoInteractions(loanRepository, equipmentPolicyClient);
    }

    @Test
    void createLoan_equipmentAlreadyLoaned_verifiesRepositoryAndNeverPolicyClient() {
        // Arrange
        when(loanRepository.existsByEquipmentCodeAndStatus("LAPTOP-VASCONEZCHRISTIAN", EquipmentLoanStatus.APPROVED)).thenReturn(true);

        // Act
        EquipmentAlreadyLoanedException exception = assertThrows(
                EquipmentAlreadyLoanedException.class,
                () -> loanService.createLoan("LAPTOP-VASCONEZCHRISTIAN", "cgvasconez2@espe.edu.ec", 5)
        );

        // Assert
        assertEquals("El equipo ya se encuentra prestado", exception.getMessage());
        verify(loanRepository).existsByEquipmentCodeAndStatus("LAPTOP-VASCONEZCHRISTIAN", EquipmentLoanStatus.APPROVED);
        verify(equipmentPolicyClient, never()).isBorrowerBlocked(any());
        verify(loanRepository, never()).save(any());
    }
}