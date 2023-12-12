domain petstore;
header baseIn{
    notnull String caller;
    unique String globalSerialNumber;
    unique String messageId;
}

header baseOut{
    notnull String appId(Default = "petstore");
    notnull String status(Default = "OK");
}

scope{
    local (in baseIn, out baseOut){
        _default;
        makeOrder;
    }
}