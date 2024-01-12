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

@column(varchar_precision="80",nullable="true",
    decimal_precision="19",decimal_scale="2",
    type_boolean="INTEGER",type_String="VARCHAR")
@insideEntity(namingType="snake_case")
tables entities{
    @insideEntity(namingType="lowercase")
    table BaseAccount (Supplier){
        primarykey String(20) AcctId;
    }
    @insideEntity(namingType="lowercase",dupNamingType="pascallowercase")
    table INVOICE(Invoice){
        primarykey int id;
    }
}

map Invoice{
    id=notnull String(20) invoiceid;
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
        updateStocks;
    }
}

services{
    decAccount{
        account.balance -=amount.amount;
    }
}


@parameter(path="{accountno}/queryAccount",mapper="accountno:Account.id"){queryAccount;}

@Bian{
    Customer;
    @CR{Invoice;}
    @BQ{
        Stock;
        Amount;
    }
}