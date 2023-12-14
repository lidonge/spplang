domain ConsumerLoan;

scope{
    local {
        Initiate;
        Update;
        Control;
        Retrieve;
        RetrieveInterest;
        ExecuteServiceFees;
        RetrieveServiceFees;
        RetrieveDisbursement;
        RequestMaintenance;
        RetrieveMaintenance;
        UpdateWithdrawal;
        ExecuteWithdrawal;
        RequestWithdrawal;
        RetrieveWithdrawal;
        UpdateRepayment;
        ExecuteRepayment;
        RequestRepayment;
        RetrieveRepayment;
        InitiateRestructuring;
        UpdateRestructuring;
        ExchangeRestructuring;
        RequestRestructuring;
        RetrieveRestructuring;
        UpdateServiceFees;
        InitiateAmountBlock;
        RetrieveAmountBlock;
        UpdateAmountBlock;
        ExecuteBookingAuthorization;
        InitiateIssuedDevice;
        RetrieveIssuedDevice;
        UpdateIssuedDevice;
    }
}
@parameter(path="Initiate"){ Initiate;}
@parameter(path="{consumerloanid}/Update"){ Update;}
@parameter(path="{consumerloanid}/Control"){ Control;}
@parameter(path="{consumerloanid}/Retrieve"){ Retrieve;}
@parameter(path="{consumerloanid}/Interest/{interestid}/Retrieve"){ RetrieveInterest;}
@parameter(path="{consumerloanid}/ServiceFees/{servicefeesid}/Execute"){ ExecuteServiceFees;}
@parameter(path="{consumerloanid}/ServiceFees/{servicefeesid}/Retrieve"){ RetrieveServiceFees;}
@parameter(path="{consumerloanid}/Disbursement/{disbursementid}/Retrieve"){ RetrieveDisbursement;}
@parameter(path="{consumerloanid}/Maintenance/{maintenanceid}/Request"){ RequestMaintenance;}
@parameter(path="{consumerloanid}/Maintenance/{maintenanceid}/Retrieve"){ RetrieveMaintenance;}
@parameter(path="{consumerloanid}/Withdrawal/{withdrawalid}/Update"){ UpdateWithdrawal;}
@parameter(path="{consumerloanid}/Withdrawal/{withdrawalid}/Execute"){ ExecuteWithdrawal;}
@parameter(path="{consumerloanid}/Withdrawal/{withdrawalid}/Request"){ RequestWithdrawal;}
@parameter(path="{consumerloanid}/Withdrawal/{withdrawalid}/Retrieve"){ RetrieveWithdrawal;}
@parameter(path="{consumerloanid}/Repayment/{repaymentid}/Update"){ UpdateRepayment;}
@parameter(path="{consumerloanid}/Repayment/{repaymentid}/Execute"){ ExecuteRepayment;}
@parameter(path="{consumerloanid}/Repayment/{repaymentid}/Request"){ RequestRepayment;}
@parameter(path="{consumerloanid}/Repayment/{repaymentid}/Retrieve"){ RetrieveRepayment;}
@parameter(path="{consumerloanid}/Restructuring/Initiate"){ InitiateRestructuring;}
@parameter(path="{consumerloanid}/Restructuring/{restructuringid}/Update"){ UpdateRestructuring;}
@parameter(path="{consumerloanid}/Restructuring/{restructuringid}/Exchange"){ ExchangeRestructuring;}
@parameter(path="{consumerloanid}/Restructuring/{restructuringid}/Request"){ RequestRestructuring;}
@parameter(path="{consumerloanid}/Restructuring/{restructuringid}/Retrieve"){ RetrieveRestructuring;}
@parameter(path="{consumerloanid}/ServiceFees/{servicefeesid}/Update"){ UpdateServiceFees;}
@parameter(path="{consumerloanid}/AmountBlock/Initiate"){ InitiateAmountBlock;}
@parameter(path="{consumerloanid}/AmountBlock/{amountblockid}/Retrieve"){ RetrieveAmountBlock;}
@parameter(path="{consumerloanid}/AmountBlock/{amountblockid}/Update"){ UpdateAmountBlock;}
@parameter(path="{consumerloanid}/BookingAuthorization/{bookingauthorizationid}/Execute"){ ExecuteBookingAuthorization;}
@parameter(path="{consumerloanid}/IssuedDevice/Initiate"){ InitiateIssuedDevice;}
@parameter(path="{consumerloanid}/IssuedDevice/{issueddeviceid}/Retrieve"){ RetrieveIssuedDevice;}
@parameter(path="{consumerloanid}/IssuedDevice/{issueddeviceid}/Update"){ UpdateIssuedDevice;}


