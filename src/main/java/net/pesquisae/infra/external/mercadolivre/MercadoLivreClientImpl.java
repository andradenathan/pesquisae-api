package net.pesquisae.infra.external.mercadolivre;

import net.pesquisae.domain.model.Marketplace;
import net.pesquisae.infra.external.common.HtmlClientImpl;
import org.springframework.stereotype.Component;

@Component
public class MercadoLivreClientImpl extends HtmlClientImpl {
    public MercadoLivreClientImpl() {
        super(Marketplace.MERCADO_LIVRE);
    }
}