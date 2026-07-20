package ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.service;

import java.util.regex.Pattern;

import ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.dto.LoanResponse;
import ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.exception.BorrowerBlockedException;
import ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.exception.EquipmentAlreadyLoanedException;
import ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.exception.InvalidBorrowerEmailException;
import ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.exception.InvalidEquipmentCodeException;
import ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.exception.InvalidLoanDaysException;
import ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.model.EquipmentLoan;
import ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.model.EquipmentLoanStatus;
import ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.repository.LoanRepository;

public class LoanService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final LoanRepository loanRepository;
    private final EquipmentPolicyClient equipmentPolicyClient;

    public LoanService(LoanRepository loanRepository, EquipmentPolicyClient equipmentPolicyClient) {
        this.loanRepository = loanRepository;
        this.equipmentPolicyClient = equipmentPolicyClient;
    }

    public LoanResponse createLoan(String equipmentCode, String borrowerEmail, int loanDays) {
        validateEquipmentCode(equipmentCode);
        validateBorrowerEmail(borrowerEmail);
        validateLoanDays(loanDays);

        if (loanRepository.existsByEquipmentCodeAndStatus(equipmentCode, EquipmentLoanStatus.APPROVED)) {
            throw new EquipmentAlreadyLoanedException("El equipo ya se encuentra prestado");
        }

        if (equipmentPolicyClient.isBorrowerBlocked(borrowerEmail)) {
            throw new BorrowerBlockedException("El usuario se encuentra bloqueado por políticas institucionales");
        }

        EquipmentLoan loan = new EquipmentLoan();
        loan.setEquipmentCode(equipmentCode);
        loan.setBorrowerEmail(borrowerEmail);
        loan.setLoanDays(loanDays);
        loan.setStatus(EquipmentLoanStatus.APPROVED);

        EquipmentLoan savedLoan = loanRepository.save(loan);
        String approvalCode = "APPROVED-" + equipmentCode + "-" + loanDays;

        return new LoanResponse(savedLoan.getId(), approvalCode);
    }

    private void validateEquipmentCode(String equipmentCode) {
        if (equipmentCode == null || equipmentCode.isBlank()) {
            throw new InvalidEquipmentCodeException("El código del equipo no puede ser nulo ni vacío");
        }
    }

    private void validateBorrowerEmail(String borrowerEmail) {
        if (borrowerEmail == null || !EMAIL_PATTERN.matcher(borrowerEmail).matches()) {
            throw new InvalidBorrowerEmailException("El correo electrónico debe tener un formato válido");
        }
    }

    private void validateLoanDays(int loanDays) {
        if (loanDays < 1 || loanDays > 15) {
            throw new InvalidLoanDaysException("loanDays debe estar entre 1 y 15 días");
        }
    }
}