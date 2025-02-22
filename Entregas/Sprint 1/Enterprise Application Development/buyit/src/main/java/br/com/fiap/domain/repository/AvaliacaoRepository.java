package br.com.fiap.domain.repository;

import br.com.fiap.domain.entity.Avaliacao;
import br.com.fiap.domain.entity.Pedido;
import br.com.fiap.domain.entity.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Objects;

public class AvaliacaoRepository implements Repository<Avaliacao, Long> {

    private static volatile AvaliacaoRepository instance;
    private final EntityManager manager;

    private AvaliacaoRepository(EntityManager manager) {
        this.manager = manager;
    }

    public static AvaliacaoRepository build(EntityManager manager) {
        AvaliacaoRepository result = instance;
        if (Objects.nonNull(result)) return result;

        synchronized (AvaliacaoRepository.class) {
            if (Objects.isNull(instance)) {
                instance = new AvaliacaoRepository(manager);
            }
            return instance;
        }
    }

    @Override
    public Avaliacao findById(Long id) {
        return manager.find(Avaliacao.class, id);
    }

    public List<Avaliacao> findByIdUsuario(Long id_usuario) {
        String jpql = "SELECT avaliacao FROM Avaliacao avaliacao WHERE avaliacao.id_usuario = :id_usuario";
        TypedQuery<Avaliacao> query = manager.createQuery(jpql, Avaliacao.class);
        query.setParameter("id_usuario", id_usuario);
        return query.getResultList();
    }

    public Avaliacao findByIdPedido(Long id) {
        return manager.find(Avaliacao.class, id);
    }

    @Override
    public List<Avaliacao> findAll() {
        return manager.createQuery("FROM Avaliacao", Avaliacao.class).getResultList();
    }

    @Override
    public Avaliacao persist(Avaliacao avaliacao) {
        EntityTransaction transaction = manager.getTransaction();
        try {
            avaliacao.setId_avaliacao(null);
            transaction.begin();
            Usuario usuario = manager.find(Usuario.class, avaliacao.getId_usuario().getId_usuario());
            Pedido pedido = manager.find(Pedido.class, avaliacao.getId_pedido().getId_pedido());
            avaliacao.setId_usuario(usuario);
            avaliacao.setId_pedido(pedido);
            manager.persist(avaliacao);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        return avaliacao;
    }

    @Override
    public Avaliacao update(Avaliacao avaliacao) {
        EntityTransaction transaction = manager.getTransaction();
        try {
            transaction.begin();
            Avaliacao avaliacao_buscada = findById(avaliacao.getId_avaliacao());
            if (Objects.nonNull(avaliacao_buscada)) {

                // ID_USUARIO
                if (Objects.nonNull(avaliacao.getId_usuario())) {
                    Usuario usuario = manager.find(Usuario.class, avaliacao.getId_usuario().getId_usuario());
                    avaliacao_buscada.setId_usuario(usuario);
                }

                // ID_PEDIDO
                if (Objects.nonNull(avaliacao.getId_pedido())) {
                    Pedido pedido = manager.find(Pedido.class, avaliacao.getId_pedido().getId_pedido());
                    avaliacao_buscada.setId_pedido(pedido);
                }

                // NOTA_PRECO_AVALIACAO
                avaliacao_buscada.setNota_preco_avaliacao(avaliacao.getNota_preco_avaliacao());

                // NOTA_QUALIDADE_AVALIACAO
                avaliacao_buscada.setNota_qualidade_avaliacao(avaliacao.getNota_qualidade_avaliacao());

                // NOTA_ENTREGA_AVALIACAO
                avaliacao_buscada.setNota_entrega_avaliacao(avaliacao.getNota_entrega_avaliacao());

                // DS_AVALIACAO
                avaliacao_buscada.setDs_avaliacao(avaliacao.getDs_avaliacao());

                manager.merge(avaliacao_buscada);
                transaction.commit();
            } else {
                transaction.rollback();
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        return avaliacao;
    }

    @Override
    public boolean delete(Avaliacao avaliacao) {
        EntityTransaction transaction = manager.getTransaction();
        try {
            transaction.begin();
            manager.remove(avaliacao);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            return false;
        }
    }
}