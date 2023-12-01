package ch.unisg.tapasroster.roster.application.port.out;


public interface StartAuctionPort {
    void startAuction(String taskUri, String taskType);
}
