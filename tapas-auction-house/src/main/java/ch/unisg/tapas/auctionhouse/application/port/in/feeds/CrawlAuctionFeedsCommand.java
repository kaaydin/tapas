package ch.unisg.tapas.auctionhouse.application.port.in.feeds;

import ch.unisg.tapas.common.SelfValidating;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@Value
public class CrawlAuctionFeedsCommand extends SelfValidating<CrawlAuctionFeedsCommand> {
    @NotNull
    private final URI entryPoint;

    @NotNull
    private final List<String> topicsToSubscribe;

    public CrawlAuctionFeedsCommand(URI entryPoint, List<String> topicsToSubscribe) {
        this.entryPoint = entryPoint;
        this.topicsToSubscribe = topicsToSubscribe;
        this.validateSelf();
    }
}
