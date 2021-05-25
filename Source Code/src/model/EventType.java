package model;

public enum EventType {
    BID_CREATED,
    BID_CLOSEDDOWN,
    BID_NEWRESPONSE,
    MESSAGE_PATCH,
    CONTRACT_CREATED,
    CONTRACT_SIGN,
    CONTRACT_ONE_PARTY_SIGN,
    CONTRACT_EXPIRE,
    CONTRACT_TERMINATE,
    CONTRACT_PATCH,
    CONTRACT_ONE_MONTH_BEFORE_EXPIRE,
    CONTRACT_DELETED,
    CONTRACT_RENEWED,
    CONTRACT_CESSATIONINFO_UPDATED,
    MONITOR_CHANGED,
    USER_MONITOR_BID
}