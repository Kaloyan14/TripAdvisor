package org.example;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        User user = new User("alice", new byte[32], "alice@example.com");
        em.persist(user);
        em.flush();

        Category catNature = new Category("Nature");
        Category catTourism = new Category("Tourism");
        em.persist(catNature);
        em.persist(catTourism);
        em.flush();

        Location loc1 = new Location("Grand Canyon", 36.1, -112.1, "A massive canyon.");
        Location loc2 = new Location("Niagara Falls", 43.1, -79.1, "Famous waterfalls.");
        em.persist(loc1);
        em.persist(loc2);
        em.flush();

        LocationCategory link1 = new LocationCategory(loc1, catNature);
        LocationCategory link2 = new LocationCategory(loc2, catTourism);
        em.persist(link1);
        em.flush();
        em.persist(link2);
        em.flush();

        Comment comment = new Comment(user, LocalDateTime.now(), loc1, "Stunning view!");
        em.persist(comment);
        em.flush();

        Like like = new Like(user, LocalDateTime.now(), loc1);
        em.persist(like);
        em.flush();

        Favorite favorite = new Favorite(user, LocalDateTime.now(), loc1);
        em.persist(favorite);
        em.flush();

        Images img = new Images(loc1, new byte[]{1, 2, 3});
        em.persist(img);
        em.flush();

        CommentLike cl = new CommentLike(user, LocalDateTime.now(), comment);
        em.persist(cl);
        em.flush();

        em.getTransaction().commit();

        System.out.println("\n--- Locations in 'Nature' Category ---");
        List<Location> results = em.createQuery("""
            SELECT lc.location
            FROM LocationCategory lc
            WHERE lc.category.name = :cat
            """, Location.class)
                .setParameter("cat", "Nature")
                .getResultList();

        for (Location l : results) {
            System.out.println("Location: " + l.getName());
        }

        em.getTransaction().commit();

        em.close();
        JPAUtil.close();
    }
}
