package serverGameGiaoDich.Enum;

public enum PacketType {
    Buy, //0
    Sell, //1
    ResponseBuy, //2  
    ResponseSell, //3
    UpdateStore, //4
    ResponseUpdateStore, //5
    Bankrupt, //6
    ResponseFindOpponent, //7
    DayPlay, //8
    ResponsePlayerCanAttack, //9
    AttackPlayer, //10
    FindPlayerCanAttack, //11
    ResponsePlayerAttack, //12
    RegisterPlayer, //13
    ResponseResultAndResource, //14
    NamePlayer, //15
    ResponseNamePlayer, //16
    MessagePlayer, //17
    ResponseMessagePlayer, //18
    ResponseRegisterPlayer //19
}
