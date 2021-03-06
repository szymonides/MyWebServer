package repositories;

import entities.User;
import java.util.LinkedList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * @author Szymon Skrzyński <skrzynski.szymon@gmail.com>
 */
public class UserRepository {

    private static UserRepository _instance;

    private LinkedList<User> _users;

    private UserRepository() {
        this._users = new LinkedList<User>();
    }

    public static UserRepository getInstance() {
        if (_instance == null) {
            synchronized (UserRepository.class) {
                UserRepository inst = _instance;
                if (inst == null) {
                    synchronized (UserRepository.class) {
                        _instance = new UserRepository();
                    }
                }
            }
        }

        return _instance;
    }

    public User getUser(long userId) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        User user = (User) session.get(User.class, userId);
        tx.commit();
        return user;
    }

}
