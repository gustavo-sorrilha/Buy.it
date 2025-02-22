package br.com.fiap.domain.service;

import br.com.fiap.BuyitApplicattion;
import br.com.fiap.domain.entity.Tipo_Variacao;
import br.com.fiap.domain.repository.Tipo_VariacaoRepository;
import br.com.fiap.infra.EntityManagerFactoryProvider;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Objects;

public class Tipo_VariacaoService implements Service<Tipo_Variacao, Long> {

    private static volatile Tipo_VariacaoService instance;

    private final Tipo_VariacaoRepository repo;

    private Tipo_VariacaoService(Tipo_VariacaoRepository repo) {
        this.repo = repo;
    }

    public static Tipo_VariacaoService build() {
        String persistenceUnit = BuyitApplicattion.PERSISTENCE_UNIT;
        Tipo_VariacaoService result = instance;
        if (Objects.nonNull(result)) return result;

        synchronized (Tipo_VariacaoService.class) {
            if (Objects.isNull(instance)) {
                EntityManagerFactory factory = EntityManagerFactoryProvider.build(persistenceUnit).provide();
                Tipo_VariacaoRepository avaliacaoRepository = Tipo_VariacaoRepository.build(factory.createEntityManager());
                instance = new Tipo_VariacaoService(avaliacaoRepository);
            }
            return instance;
        }
    }

    @Override
    public List<Tipo_Variacao> findAll() {
        return repo.findAll();
    }

    @Override
    public Tipo_Variacao findById(Long id) {
        return repo.findById(id);
    }

    public List<Tipo_Variacao> findByName(String name) {
        if (Objects.isNull(name)) return null;
        return repo.findByName(name);
    }

    @Override
    public Tipo_Variacao persist(Tipo_Variacao tipo_variacao) {
        if (Objects.isNull(tipo_variacao)) return null;
        return repo.persist(tipo_variacao);
    }

    @Override
    public Tipo_Variacao update(Tipo_Variacao tipo_variacao) {
        if (Objects.isNull(tipo_variacao)) return null;
        return repo.update(tipo_variacao);
    }

    @Override
    public boolean delete(Tipo_Variacao tipo_variacao) {
        if (Objects.isNull(tipo_variacao)) return false;
        return repo.delete(tipo_variacao);
    }
}
