package ec.edu.espe.examp3vasconezchristian.vasconezchristianexamp3.dto;

public class LoanResponse {

    private Long loanId;
    private String approvalCode;

    public LoanResponse() {
    }

    public LoanResponse(Long loanId, String approvalCode) {
        this.loanId = loanId;
        this.approvalCode = approvalCode;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }
}