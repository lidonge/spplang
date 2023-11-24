domain invoice;

header baseIn{
    notnull String caller;
    unique String globalSerialNumber;
    unique String messageId;
}

header baseOut{
    notnull String appId(Default = "invoice");
    notnull String status(Default = "OK");
}

header stockHeaderIn extends baseIn{
    notnull String channelId = caller;
}

primary{
    Customer.id;
    Product.id;
    BaseAccount.id;
    Stock.id;
    Invoice.id;
}

map Invoice(snake_case, storerole){
    id=String(20) invoiceid;
    Amount.amount=double(12,6);
}

scope{
    local (in baseIn, out baseOut){
        queryAccount;
        decAccount;
        incAccount;
        makeInvoice;
    }
    remote (in baseIn, out baseOut){
        calculateAmount;
    }
    remote (in stockHeaderIn, out baseOut){
        asycn tcc decStock;
        checkStock;
    }
}

services{
    decAccount{
        account.balance -= amount.amount;
    }
}