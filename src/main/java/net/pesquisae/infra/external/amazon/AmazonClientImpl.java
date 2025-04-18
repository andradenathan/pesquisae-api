package net.pesquisae.infra.external.amazon;

import net.pesquisae.domain.model.Marketplace;
import net.pesquisae.infra.external.common.HtmlClientImpl;
import org.springframework.stereotype.Component;

@Component
public class AmazonClientImpl extends HtmlClientImpl {
    public AmazonClientImpl() {
        super(Marketplace.AMAZON);
    }
}
