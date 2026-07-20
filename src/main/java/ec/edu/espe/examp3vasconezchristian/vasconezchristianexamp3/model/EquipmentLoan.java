package ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.model;

public class EquipmentLoan {

    private Long id;
    private String equipmentCode;
    private String borrowerEmail;
    private int loanDays;
    private EquipmentLoanStatus status;

    public EquipmentLoan() {
    }

    public EquipmentLoan(Long id, String equipmentCode, String borrowerEmail, int loanDays, EquipmentLoanStatus status) {
        this.id = id;
        this.equipmentCode = equipmentCode;
        this.borrowerEmail = borrowerEmail;
        this.loanDays = loanDays;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getBorrowerEmail() {
        return borrowerEmail;
    }

    public void setBorrowerEmail(String borrowerEmail) {
        this.borrowerEmail = borrowerEmail;
    }

    public int getLoanDays() {
        return loanDays;
    }

    public void setLoanDays(int loanDays) {
        this.loanDays = loanDays;
    }

    public EquipmentLoanStatus getStatus() {
        return status;
    }

    public void setStatus(EquipmentLoanStatus status) {
        this.status = status;
    }
}