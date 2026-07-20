package ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.repository;

import ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.model.EquipmentLoan;
import ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.model.EquipmentLoanStatus;

public interface LoanRepository {

    EquipmentLoan save(EquipmentLoan loan);

    boolean existsByEquipmentCodeAndStatus(String equipmentCode, EquipmentLoanStatus status);

}