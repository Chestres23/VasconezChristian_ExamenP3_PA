package ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.exception;

public class EquipmentAlreadyLoanedException extends RuntimeException {

    public EquipmentAlreadyLoanedException(String message) {
        super(message);
    }
}